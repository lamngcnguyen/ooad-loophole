let studentRowIndex = 0;

const studentTable = $(".student .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: false,
    lengthChange: true,
    ajax: "/api/classes/" + classId + "/students",
    sDom: stringDom,
    language: languageOption,
    columns: [
        {
            data: null,
            render: function () {
                return ++studentRowIndex;
            }
        },
        {
            data: null,
            render: function () {
                return "";
            }
        },
        {data: "studentId"},
        {data: "fullName"},
        {data: "phoneNumber", defaultContent: "Not set"},
        {
            data: "isActive",
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
                $('.form.edit-student').form('set values', {
                    id: data._id,
                    fullName: data.fullName,
                    studentId: data.studentId,
                });
                showModal('.modal.edit-student');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa sinh viên" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                $('.form.delete-student').form('set values', {
                    id: data._id
                });
                showModal('.modal.delete-student');
            });
        actionCell.append(btnEdit, btnDelete);
    }
});

$('.student .page-length input').change(function () {
    studentTable.page.len(this.value).draw();
});

$('.student .table-search input').keyup(function () {
    studentTable.search(this.value).draw();
});

$('.dropdown.assigned-class').dropdown({
    showOnFocus: false,
}).api({
    action: 'get classes',
    on: 'now',
    onSuccess(response, element, xhr) {
        var values = [];
        xhr.responseJSON.data.forEach(function (c) {
            values.push({
                value: c._id,
                name: c.className,
                text: c.className,
                description: c.className
            });
        });
        console.log(values);
        $(element).dropdown('change values', values);
    }
});

$('.form.create-student').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.create-student');
        data['classId'] = classId;
        $.api({
            action: 'create student',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;char=utf-8');
            },
            onSuccess: function () {
                hideDimmer('.modal.create-student');
                hideModal('.modal.create-student');
                reloadStudentTable()
            },
            onFailure: function (response) {
                hideDimmer('.modal.create-student');
                $('.form.create-student').form('add errors', [response]);
            }
        });
    },
    fields: {
        fullName: validationRules.fullName,
        studentId: validationRules.studentId,
        classId: validationRules.classId,
    }
});

$('.form.edit-student').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.edit-student');
        data['classId'] = classId;
        $.api({
            action: 'update student',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'put',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;char=utf-8');
            },
            onSuccess: function () {
                hideDimmer('.modal.edit-student');
                hideModal('.modal.edit-student');
                reloadStudentTable()
            },
            onFailure: function (response) {
                hideDimmer('.modal.edit-student');
                $('.form.create-student').form('add errors', [response]);
            }
        });
    },
    fields: {
        fullName: validationRules.fullName,
        studentId: validationRules.studentId,
        classId: validationRules.classId,
    }
});

$('.form.delete-student').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.delete-student');
        $.api({
            action: 'delete student',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-student');
                hideModal('.modal.delete-student');
                reloadStudentTable();
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-student');
                $('.form.delete-student').form('add errors', [res]);
            }
        });
    },
});

function reloadStudentTable() {
    studentRowIndex = 0;
    studentTable.ajax.reload();
}