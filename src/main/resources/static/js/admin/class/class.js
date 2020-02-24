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
            data: "active",
            render: function (isActive) {
                if (isActive) return "<i class='green check icon'></i>";
                else return "<i class='red ban icon'></i>"
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1, -1], className: "center aligned"}
    ]
});

$('.form.create-class').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-class');
        correctFormData('.form.create-class', data);
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
                $('.form.create-class').form('add errors', [response.message]);
            },
            onSuccess: function () {
                hideDimmer('.modal.create-class');
                hideModal('.modal.create-class');
                classTable.ajax.reload();
            }
        })
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
        console.log(values);
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
        console.log(values);
        $(element).dropdown('change values', values);
    }
});