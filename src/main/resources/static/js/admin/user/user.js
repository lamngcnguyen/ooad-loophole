var rowIndex = 0;
var userTable = $(".teacher-admin .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/users/role/admin",
    sDom: stringDom,
    language: languageOption,
    columns: [
        {
            data: null,
            render: function () {
                return ++rowIndex;
            }
        },
        {
            data: null,
            render: function () {
                return "";
            }
        },
        {data: "fullName"},
        {data: "username"},
        {data: "email"},
        {
            data: "active",
            render: function (isActive) {
                if (isActive) return "<i class='green check icon'></i>";
                else return "<i class='red ban icon'></i>"
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1, -1], className: "center aligned"}
    ],
    createdRow: function (row, data) {
        var actionCell = $(row).children().eq(1);
        var btnEdit = $('<button type="button" class="ui mini icon blue button"><i class="pencil icon"></i></button>')
            .click(function () {
                showModal('.modal.edit-user');
            });
        var btnDelete = $('<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>')
            .click(function () {
                showModal('.modal.delete-user');
            });
        var btnDeactivate = $('<button type="button" class="ui mini icon red button"><i class="ban icon"></i></button>')
            .click(function () {
                showModal('.modal.deactivate-user');
            });
        actionCell.append(
            btnEdit, $('<span>&nbsp</span>'),
            btnDeactivate, $('<span>&nbsp</span>'),
            btnDelete
        );
    }
});

$.fn.form.settings.rules.oneRoleClaimed = function () {
    var isAdmin = $('#checkboxAdmin').prop('checked');
    var isTeacher = $('#checkboxTeacher').prop('checked');
    return isAdmin || isTeacher;
};

$('.form.create-user').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-user');
        correctFormData('.form.create-user', data);
        $('.create-user .form').api({
            action: 'create user',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            onFailure: function (response) {
                $('.form.create-user').form('add errors', [response.message]);
            },
            onSuccess: function () {
                userTable.ajax().reload();
            }
        });
    },
    fields: {
        fullName: {
            identifier: 'fullName',
            rules: [
                {
                    type: 'regExp[/^[a-zA-Z\\-]+$/]',
                    prompt: 'Họ tên không hợp lệ'
                }
            ]
        },
        username: {
            identifier: 'username',
            rules: [
                {
                    type: 'regExp[/^[a-zA-Z0-9]+$/]',
                    prompt: 'Tên đăng nhập không hợp lệ'
                }
            ]
        },
        email: {
            identifier: 'email',
            rules: [
                {
                    type: 'email',
                    prompt: 'Email không hợp lệ'
                }
            ]
        },
        oneRoleClaimed: {
            identifier: 'role-checkbox',
            rules: [{
                type: 'oneRoleClaimed',
                prompt: 'Vui lòng chọn ít nhất một quyền'
            }]
        }
    }
});

$('#pageLengthSelect input').change(function () {
    userTable.page.len(this.value).draw();
});

$('#searchTableInput input').keyup(function () {
    userTable.search(this.value).draw();
});

$('.item.teacher-admin').click(function () {
    $(this).addClass('active');
    $('.tab.teacher-admin').addClass('active');
    $('.item.student').removeClass('active');
    $('.tab.student').removeClass('active');
});

$('.item.student').click(function () {
    $(this).addClass('active');
    $('.tab.student').addClass('active');
    $('.item.teacher-admin').removeClass('active');
    $('.tab.teacher-admin').removeClass('active');
});

function filterRole(roleName) {
    rowIndex = 0;
    userTable.ajax.url('/api/users/role/' + roleName).load();
    userTable.draw();
}
