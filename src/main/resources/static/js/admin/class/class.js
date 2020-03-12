let rowIndex = 0;
const classTable = $(".ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/classes/",
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
        {data: "className"},
        {data: "teacherName"},
        {data: "semesterName"},
        {
            data: "scheduledDayOfWeek",
            render: function (dayOfWeek) {
                return "Thứ " + (dayOfWeek + 1);
            }
        },
        {data: "studentCount"}
    ],
    columnDefs: [
        {targets: [0, 1, -1, -2], className: "center aligned"}
    ],
    createdRow: function (row, data) {
        const actionCell = $(row).children().eq(1);
        const btnEdit = $('<button type="button" class="ui mini icon blue button" data-tooltip="Chỉnh sửa thông tin" data-inverted=""><i class="pencil icon"></i></button>')
            .click(function () {
                $('.form.edit-class').form('set values', {
                    id: data._id,
                    className: data.className,
                    teacherId: data.teacherId,
                    semesterId: data.semesterId,
                    scheduledDayOfWeek: data.scheduledDayOfWeek
                });
                showModal('.modal.edit-class');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa lớp" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                $('.form.delete-class').form('set values', {
                    id: data._id
                });
                showModal('.modal.delete-class');
            });
        actionCell.append(
            btnEdit, btnDelete
        );
    }
});

$('.form.create-class').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.create-class');
        $.api({
            action: 'create class',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
            },
            onFailure: function (response) {
                $('.form.create-class').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.create-class');
                hideModal('.modal.create-class');
                reloadClassTable()
            }
        })
    },
    fields: {
        className: validationRules.className,
        teacherId: validationRules.teacherId,
        semesterId: validationRules.semesterId,
        scheduledDayOfWeek: validationRules.scheduledDayOfWeek
    }
});

$('.form.edit-class').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.edit-class');
        correctFormData('.form.edit-class', data);
        $.api({
            action: 'update class',
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
                hideDimmer('.modal.edit-class');
                $('.form.edit-class').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.edit-class');
                hideModal('.modal.edit-class');
                reloadClassTable();
            }
        });
    },
    fields: {
        className: validationRules.className,
        teacherId: validationRules.teacherId,
        semesterId: validationRules.semesterId,
        scheduledDayOfWeek: validationRules.scheduledDayOfWeek
    }
});

$('.form.delete-class').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.delete-class');
        $.api({
            action: 'delete class',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-class');
                hideModal('.modal.delete-class');
                reloadClassTable();
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-class');
                $('.form.delete-class').form('add errors', [res]);
            }
        });
    },
});

$('.dropdown.teacher').dropdown({
    showOnFocus: false,
}).api({
    action: 'get teachers',
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (teacher) {
            values.push({
                value: teacher._id,
                name: teacher.fullName + " - " + teacher.email,
                text: teacher.fullName,
            })
        });
        $(element).dropdown('change values', values);
    }
});

$('.dropdown.semester').dropdown({
    showOnFocus: false,
}).api({
    action: 'get semesters',
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (semester) {
            values.push({
                value: semester._id,
                name: semester.name,
                text: semester.name,
            })
        });
        $(element).dropdown('change values', values);
    }
});

$('.class .table-search input').keyup(function () {
    classTable.search(this.value).draw();
});

$('.class .page-length input').change(function () {
    classTable.page.len(this.value).draw();
});

function reloadClassTable() {
    rowIndex = 0;
    classTable.ajax.reload();
}