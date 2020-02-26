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
    createdRow: function (row) {
        const actionCell = $(row).children().eq(1);
        const btnEdit = $('<button type="button" class="ui mini icon blue button"><i class="pencil icon"></i></button>')
            .click(function () {
                showModal('.modal.edit-semester');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>')
            .click(function () {
                showModal('.modal.delete-semester');
            });
        actionCell.append(
            btnEdit, $('<span>&nbsp</span>'),
            btnDelete
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
                semesterTable.ajax.reload();
            }
        })
    },
    fields: {
        name: validationRules.name,
        startDate: validationRules.startDate,
        endDate: validationRules.endDate
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
                    const month = ('0' + (date.getMonth()+1)).slice(-2);
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
                    const month = ('0' + (date.getMonth()+1)).slice(-2);
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
                    const month = ('0' + (date.getMonth()+1)).slice(-2);
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
                    const month = ('0' + (date.getMonth()+1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            },
            startCalendar: $('.modal.edit-semester .range-start')
        });
    }
});