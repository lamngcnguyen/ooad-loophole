var importTable = $('.ui.table.import-student').DataTable({
    ordering: true,
    searching: false,
    pageLength: 5,
    autoWidth: true,
    lengthChange: true,
    sDom: stringDom,
    language: languageOption,
    columns: [
        {data: '#'},
        {data: 'studentId'},
        {data: 'fullName'},
    ],
    columnDefs: [
        {targets: [0, 1], className: "center aligned"}
    ]
});

function createStudentImportTemplate() {
    var wb = XLSX.utils.book_new();
    var ws_name = "students";
    var ws_data = [
        ['#', 'Student ID', 'Full name'],
        ['1', '16020263', 'Ha Tuan Phong'],
        ['2', '16022408', 'Nguyen Ngoc Lam']
    ];
    var ws = XLSX.utils.aoa_to_sheet(ws_data);
    XLSX.utils.book_append_sheet(wb, ws, ws_name);
    XLSX.writeFile(wb, 'students.xlsx');
}

function processStudentImportTemplate(evt) {
    var file = evt.target.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        var data = e.target.result;
        try {
            var workbook = XLSX.read(data, {
                type: 'binary'
            });
        } catch (ex) {
            alert('Unable to process file!');
            return;
        }
        var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]]);
        var studentData = [];
        XL_row_object.forEach(function (row) {
            var values = Object.values(row);
            studentData.push({
                '#': values[0],
                'studentId': values[1],
                'fullName': values[2],
            })
        });
        importTable.clear().rows.add(studentData).draw();
    };

    reader.onerror = function () {
        alert('Unable to process file!')
    };
    reader.readAsBinaryString(file);
}

function openImportStudentDialog() {
    $('input[name="uploadExcel"]').click();
}

document.getElementsByName('uploadExcel')[0].addEventListener('change', function (e) {
    processStudentImportTemplate(e);
    this.value = '';
}, false);

$('.form.import-student').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.import-student');
        var studentData = importTable.data().toArray();
        if (studentData.length === 0) {
            $('.form.import-student').form('add errors', ['Chưa có sinh viên']);
            return;
        }
        studentData.forEach(function (s) {
            s['classId'] = data.classId;
        });
        $('.form.import-student').api({
            action: 'import student',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(studentData),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onFailure: function (response) {
                console.log('failed');
                hideDimmer('.modal.import-student');
                $('.form.import-student').form('add errors', [response])
            },
            onSuccess: function () {
                console.log('success');
                hideDimmer('.modal.import-student');
                hideModal('.modal.import-student');
                studentRowIndex = 0;
                studentTable.ajax.reload();
            },
        });
    },
    fields: {
        classId: validationRules.classId,
    }
});