let rowIndex = 0;
const semesterTable = $('.ui.table').DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/semesters/",
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
        {data: "name"},
        {
            data: "startDate",
            render: function (startDate) {
                const date = new Date(startDate);
                return date.toLocaleDateString("en-GB")
            }
        },
        {
            data: "endDate",
            render: function (endDate) {
                const date = new Date(endDate);
                return date.toLocaleDateString("en-GB")
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
                $('.form.edit-semester').form('set values', {
                    id: data._id,
                    name: data.name,
                    startDate: data.startDate,
                    endDate: data.endDate,
                });
                showModal('.modal.edit-semester');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa học kì" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                showModal('.modal.delete-semester');
            });
        actionCell.append(
            btnEdit, btnDelete
        );
    }
});

$('.form.create-semester').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-semester');
        correctFormData('.form.create-semester', data);
        $('.create-semester .form').api({
            action: 'create semester',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
            },
            onFailure: function (response) {
                $('.form.create-semester').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.create-semester');
                hideModal('.modal.create-semester');
                reloadSemesterTable()
            }
        })
    },
    fields: {
        name: validationRules.name,
        startDate: validationRules.startDate,
        endDate: validationRules.endDate
    }
});

$('.form.edit-semester').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.edit-semester');
        correctFormData('.form.edit-semester', data);
        $('.form.edit-semester').api({
            action: 'update semester',
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
                hideDimmer('.modal.edit-semester');
                $('.form.edit-class').form('add errors', [response]);
            },
            onSuccess: function () {
                hideDimmer('.modal.edit-semester');
                hideModal('.modal.edit-semester');
                reloadSemesterTable();
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

$('.modal.create-semester').modal({
    onShow: function () {
        $('.modal.create-semester .range-start').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            },
            endCalendar: $('.modal.create-semester .range-end')
        });
        $('.modal.create-semester .range-end').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            },
            startCalendar: $('.modal.create-semester .range-start')
        });
    }
});

$('.modal.edit-semester').modal({
    onShow: function () {
        $('.modal.edit-semester .range-start').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            },
            endCalendar: $('.modal.edit-semester .range-end')
        });
        $('.modal.edit-semester .range-end').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            },
            startCalendar: $('.modal.edit-semester .range-start')
        });
    }
});

$('.semester .table-search input').keyup(function () {
    semesterTable.search(this.value).draw();
});

$('.semester .page-length input').change(function () {
    semesterTable.page.len(this.value).draw();
});

function reloadSemesterTable() {
    rowIndex = 0;
    semesterTable.ajax.reload();
}