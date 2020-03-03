var userRowIndex = 0;
const userTable = $(".teacher-admin .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: false,
    lengthChange: true,
    ajax: "/api/users/role/admin",
    sDom: stringDom,
    language: languageOption,
    columns: [
        {
            data: null,
            render: function () {
                return ++userRowIndex;
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
        {data: "phoneNumber", defaultContent: 'Not set'},
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
        const actionCell = $(row).children().eq(1);
        const btnEdit = $('<button type="button" class="ui mini icon blue button" data-tooltip="Chỉnh sửa thông tin" data-inverted=""><i class="pencil icon"></i></button>')
            .click(function () {
                $('.form.edit-user').form('set values', {
                    id: data._id,
                    fullName: data.fullName,
                    username: data.username,
                    email: data.email,
                    phoneNumber: data.phoneNumber,
                    isAdmin: userHasRole(data, 'admin'),
                    isTeacher: userHasRole(data, 'teacher')
                });
                showModal('.modal.edit-user');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa tài khoản" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                $('.form.delete-user').form('set values', {
                    id: data._id
                });
                showModal('.modal.delete-user');
            });
        actionCell.append(btnEdit, btnDelete);
    }
});

$('.form.create-user').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-user');
        correctFormData('.form.create-user', data);
        $('.form.create-user').api({
            action: 'create user',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onFailure: function (response) {
                hideDimmer('.modal.edit-user');
                $('.form.create-user').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.create-user');
                hideModal('.modal.create-user');
                reloadUserTable();
            }
        });
    },
    fields: {
        fullName: validationRules.fullName,
        username: validationRules.username,
        email: validationRules.email,
        phoneNumber: validationRules.phoneNumber,
        oneRoleClaimed: validationRules.oneRoleClaimed('.form.create-user')
    }
});

$('.form.edit-user').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.edit-user');
        correctFormData('.form.edit-user', data);
        $('.form.edit-user').api({
            action: 'update user',
            urlData: {
                id: data.id,
            },
            on: 'now',
            method: 'put',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onFailure: function (response) {
                hideDimmer('.modal.edit-user');
                $('.form.edit-user').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.edit-user');
                hideModal('.modal.edit-user');
                reloadUserTable();
            }
        });
    },
    fields: {
        fullName: validationRules.fullName,
        username: validationRules.username,
        email: validationRules.email,
        phoneNumber: validationRules.phoneNumber,
        oneRoleClaimed: validationRules.oneRoleClaimed('.form.edit-user')
    }
});

$('.form.delete-user').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.delete-user');
        $('.form.delete-user').api({
            action: 'delete user',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-user');
                hideModal('.modal.delete-user');
                reloadUserTable();
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-user');
                $('.form.delete-user').form('add errors', [res]);
            }
        });
    },
});

$('.teacher-admin .page-length input').change(function () {
    userTable.page.len(this.value).draw();
});

$('.teacher-admin .table-search input').keyup(function () {
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
    userRowIndex = 0;
    userTable.ajax.url(`/api/users/role/${roleName}`).load();
    userTable.draw();
}

function userHasRole(user, roleName) {
    for (let i = 0; i < user.roles.length; i++) {
        if (user.roles[i].role.toLowerCase() === roleName.toLowerCase()) return true;
    }
    return false;
}

function reloadUserTable() {
    userRowIndex = 0;
    userTable.ajax.reload();
}