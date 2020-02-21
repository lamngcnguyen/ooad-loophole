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
                $('.form.edit-user').form('set values', {
                    id: data._id,
                    fullName: data.fullName,
                    username: data.username,
                    email: data.email,
                    isAdmin: userHasRole(data, 'admin'),
                    isTeacher: userHasRole(data, 'teacher')
                });
                showModal('.modal.edit-user');
            });
        var btnDelete = $('<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>')
            .click(function () {
                $('.form.delete-user').form('set values', {
                    id: data._id
                });
                showModal('.modal.delete-user');
            });
        var btnDeactivate = $('<button type="button" class="ui mini icon red button"><i class="ban icon"></i></button>')
            .click(function () {
                $('.form.deactivate-user').form('set values', {
                    id: data._id
                });
                showModal('.modal.deactivate-user');
            });
        actionCell.append(
            btnEdit, $('<span>&nbsp</span>'),
            btnDeactivate, $('<span>&nbsp</span>'),
            btnDelete
        );
    }
});

rowIndex = 0;
var studentTable = $(".student .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/students",
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
        {data: "studentId"},
        {data: "className"},
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
                showModal('.modal.edit-student');
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
        fullName: validationRules.fullName,
        username: validationRules.username,
        email: validationRules.email,
        oneRoleClaimed: validationRules.oneRoleClaimed('.form.create-user')
    }
});

$('.form.edit-user').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.edit-user');
        correctFormData('.form.edit-user', data);
        $('.create-user .form').api({
            action: 'update user',
            urlData: {
                id: data.id,
            },
            on: 'now',
            method: 'put',
            dataType: 'json',
            data: JSON.stringify(data),
            onFailure: function (response) {
                $('.edit.create-user').form('add errors', [response.message]);
            },
            onSuccess: function () {
                userTable.ajax().reload();
            }
        });
    },
    fields: {
        fullName: validationRules.fullName,
        username: validationRules.username,
        email: validationRules.email,
        oneRoleClaimed: validationRules.oneRoleClaimed('.form.edit-user')
    }
});

$('.form.delete-user').form();

$('.teacher-admin input.page-length').change(function () {
    userTable.page.len(this.value).draw();
});

$('.teacher-admin input.table-search').keyup(function () {
    userTable.search(this.value).draw();
});

$('.student input.page-length').change(function () {
    studentTable.page.len(this.value).draw();
});

$('.student input.table-search').keyup(function () {
    studentTable.search(this.value).draw();
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
    userTable.ajax.url(`/api/users/role/${roleName}`).load();
    userTable.draw();
}

function userHasRole(user, roleName) {
    for (var i = 0; i < user.roles.length; i++) {
        if (user.roles[i].role.toLowerCase() === roleName.toLowerCase()) return true;
    }
    return false;
}