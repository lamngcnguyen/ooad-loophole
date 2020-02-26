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
        {data: "studentCount"},
        {
            data: "active",
            render: function (isActive) {
                if (isActive) return "<i class='green check icon'></i>";
                else return "<i class='red ban icon'></i>"
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1, -1, -2], className: "center aligned"}
    ],
    createdRow: function (row) {
        const actionCell = $(row).children().eq(1);
        const btnEdit = $('<button type="button" class="ui mini icon blue button"><i class="pencil icon"></i></button>')
            .click(function () {
                showModal('.modal.edit-class');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>')
            .click(function () {
                showModal('.modal.delete-class');
            });
        const btnDeactivate = $('<button type="button" class="ui mini icon red button"><i class="ban icon"></i></button>')
            .click(function () {
                showModal('.modal.deactivate-class');
            });
        actionCell.append(
            btnEdit, $('<span>&nbsp</span>'),
            btnDeactivate, $('<span>&nbsp</span>'),
            btnDelete
        );
    }
});

$('.form.create-class').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-class');
        $('.create-class .form').api({
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
                classTable.ajax.reload();
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