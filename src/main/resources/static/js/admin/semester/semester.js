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
        {data: "startDate"},
        {data: "endDate"},
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
});

$('.modal.create-semester').modal({
    onShow: function () {
        $('.modal.create-semester .range-start').calendar({
            type: 'date',
            endCalendar: $('.modal.create-semester .range-end')
        });
        $('.modal.create-semester .range-end').calendar({
            type: 'date',
            startCalendar: $('.modal.create-semester .range-start')
        });
    }
});

$('.modal.edit-semester').modal({
    onShow: function () {
        $('.modal.edit-semester .range-start').calendar({
            type: 'date',
            endCalendar: $('.modal.edit-semester .range-end')
        });
        $('.modal.edit-semester .range-end').calendar({
            type: 'date',
            startCalendar: $('.modal.edit-semester .range-start')
        });
    }
});