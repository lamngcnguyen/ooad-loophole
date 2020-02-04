var studentTable = $('.student-table tbody');
var importStudentTable = $('.import-student-table tbody');
var importStudents;
var iterationTable = $('.iteration-table tbody');
var attachmentTable = $('.attachment-table tbody');
var topicTable = $('.topic-table tbody');

$('#classSetting').on('click', function () {
    showPopup('.class-setting-popup');
});

$('#deleteClass').on('click', function () {
    showSpinner();
    deleteClass();
});

$('.class-setting-popup .popup-close').on('click', function () {
    hidePopup('.class-setting-popup');
});

$('#importStudents').on('click', function () {
    clearImportTable();
    showPopup('.import-student-popup');
});

$('.import-student-popup .popup-close').on('click', function () {
    hidePopup('.import-student-popup');
});

$('#addStudent').on('click', function () {
    showPopup('.add-student-popup');
});

$('.add-student-popup .popup-close').on('click', function () {
    hidePopup('.add-student-popup')
});

$('#addTopic').on('click', function () {
    showSpinner();
    $.when($.ajax({
        url: '/class/unassigned-groups',
        method: 'get',
        success: function (jqXHR) {
            var groupSelect = $('select#groupId').empty().append($('<option value="0">Not set</option>'));
            jqXHR.forEach(function (g) {
                var option = $('<option></option>').val(g._id).text(g.groupName);
                groupSelect.append(option);
            });
        },
        error: function () {
            alert('Unable to get unassigned group!');
        }
    })).done(function () {
        hideSpinner();
        attachmentTable.empty();
        showPopup('.add-topic-popup');
    });
});

$('.add-topic-popup .popup-close').on('click', function () {
    hidePopup('.add-topic-popup');
});

$('#submitTopic').on('click', function () {
    var groupName = $('input#topicName').val();
    var description = $('textarea#topicDescription').val();
    var groupId = $('select#groupId option:selected').val();
    var files = $('input#inputAddAttachments').get(0).files;

    var formData = new FormData();
    formData.append('topicName', groupName);
    if (description.length > 0)
        formData.append('description', description);
    if (groupId != 0)
        formData.append('groupId', groupId);
    $.each(files, function (i, f) {
        formData.append('files', f);
    });

    showSpinner();
    $.when($.ajax({
        url: '/class/topics',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        method: 'post',
        success: function (topic) {
            hidePopup('.add-topic-popup');
            insertTopic(topic);
        },
        error: function () {
            alert('Unable to add topic!');
        }
    })).done(function () {
        hideSpinner();
    });
});

$('#downloadTemplate').on('click', function () {
    createStudentListTemplate();
});

$('#importXLSX').on('click', function () {
    $('#xlsxUpload').click()
});

$('#saveImportStudents').on('click', function () {
    saveImportStudents()
});

$('#addAttachments').on('click', function () {
    $('#inputAddAttachments').click();
});

$('#inputAddAttachments').on('change', function () {
    var files = $(this).get(0).files;
    console.log(files);
    for (var i = 0; i < files.length; i++) {
        var value = files[i];
        var fileType = $('<td></td>').append(getFileTypeIcon(value.name));
        var name = $('<td></td>').text(value.name);
        attachmentTable.append($('<tr></tr>').append(fileType, name));
    }
});

document.getElementById('xlsxUpload').addEventListener('change', function (e) {
    parseStudents(e);
    this.value = '';
}, false);

function parseStudents(evt) {
    clearImportTable();
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
        XL_row_object.forEach(function (em) {
            // console.log(em);
            var values = Object.values(em);
            var row = $('<tr></tr>');
            var num = $('<td></td>').text(values[0]);
            var name = $('<td></td>').text(values[1]);
            var studentId = $('<td></td>').text(values[2]);
            var email = $('<td></td>').text(values[3]);
            var group = $('<td></td>').text(values[4]);
            row.append(num, name, studentId, email, group);
            importStudentTable.append(row);
            importStudents.push({
                'fullName': values[1],
                'studentId': values[2].toString(),
                'email': values[3],
                'groupName': values[4]
            });
        });
    };

    reader.onerror = function () {
        alert('Unable to process file!')
    };

    reader.readAsBinaryString(file);
}

function createStudentListTemplate() {
    var wb = XLSX.utils.book_new();
    var ws_name = "students";
    var ws_data = [
        ['#', 'Full name', 'Student ID', 'Email', 'Group'],
        ['1', 'Phong Ha Tuan', '16020263', '16020263@vnu.edu.vn', 'F & Đồng bọn'],
        ['2', 'Nguyen Ngoc Lam', '16022408', '16022408@vnu.edu.vn', 'F & Đồng bọn']
    ];
    var ws = XLSX.utils.aoa_to_sheet(ws_data);
    XLSX.utils.book_append_sheet(wb, ws, ws_name);
    XLSX.writeFile(wb, 'students.xlsx');
}

function getStudents() {
    $.ajax({
        url: '/class/students',
        method: 'GET',
        success: function (jqXHR) {
            jqXHR.forEach(function (s, index) {
                insertStudent(s, index + 1);
            })
        },
        error: function () {
            alert('Cannot get student list!');
        }
    });
}

function insertStudent(s, index) {
    var id = $('<td class="id-cell"></td>').text(s.userId);
    var num = $('<td class="num-cell"></td>').text(index);
    var fullName = $('<td></td>').text(s.fullName);
    var studentId = $('<td></td>').text(s.studentId);
    var email = $('<td></td>').text(s.email);
    var groupId = $('<td class="id-cell group-id"></td>').text(s.groupId);
    var groupName = $('<td></td>').text(s.groupName);
    var actions = $('<td><i class="fa fa-pencil fa-fw td-action-icon"></i>&nbsp<i class="fa fa-trash fa-fw td-action-icon"></i>&nbsp</td>');
    var row = $('<tr></tr>').append(id, num, fullName, studentId, email, groupId, groupName, actions);

    studentTable.append(row);
}

function saveImportStudents() {
    showSpinner();
    $.ajax({
        url: '/class/students/import',
        method: 'POST',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            'students': importStudents
        }),
        success: function () {
            window.location.reload();
        },
        error: function () {
            alert('Cannot update student list!');
        }
    })
}

function clearImportTable() {
    importStudentTable.empty();
    importStudents = [];
}

function deleteClass() {
    $.ajax({
        url: '/class',
        method: 'delete',
        success: function () {
            window.location.href = '/teacher/class';
        },
        error: function () {
            alert('Unable to delete class!');
        }
    })
}

function getIterations() {
    $.ajax({
        url: '/class/iterations',
        method: 'get',
        success: function (jqXHR) {
            jqXHR.forEach(function (i, index) {
                var row = $('<tr></tr>');
                var num = $('<td class="num-cell"></td>').text(index + 1);
                var startDateTime = $('<td></td>').text(i.startDateTime);
                var endDateTime = $('<td></td>').text(i.endDateTime);
                var message = $('<td></td>').text(i.message);

                var status;
                var today = new Date();
                var sd = new Date(i.startDateTime);
                var ed = new Date(i.endDateTime);
                if (sd.getTime() > today.getTime()) {
                    status = $('<td class="status status-waiting"></td>').text("WAITING");
                } else if (ed.getTime() <= today.getTime()) {
                    status = $('<td class="status status-finish">').text("FINISHED");
                    row.addClass('finished-iteration');
                } else {
                    status = $('<td class="status status-in-progress">').text("IN PROGRESS");
                }
                iterationTable.append(row.append(num, startDateTime, endDateTime, message, status));
            });
        },
        error: function () {
            alert('Unable to get iterations!');
        }
    })
}

function getTopics() {
    $.ajax({
        url: '/class/topics',
        method: 'get',
        success: function (jqXHR) {
            jqXHR.forEach(function (t, index) {
                insertTopic(t, index + 1);
            })
        },
        error: function () {
            alert('Unable to get topics');
        }
    });
}

function insertTopic(topic, index) {
    var num = $('<td class="num-cell"></td>').text(index);
    var description = $('<td></td>').text(topic.descriptions);
    var name = $('<td></td>').text(topic.name);
    var group = $('<td></td>').text(topic.groupName);
    var list = $('<ul style="list-style-type: none; margin: 0; padding: 0;"></ul>');
    if (topic.specificationFiles != null)
        topic.specificationFiles.forEach(function (f) {
            var fileName = f.fileName;
            var fileTypeIcon = getFileTypeIcon(fileName);
            var link = $('<a></a>')
                .attr('href', '/file/download?filePath=' + encodeURIComponent(f.path))
                .attr('target', '_blank')
                .append(fileTypeIcon).append(fileName);
            list.append($('<li></li>').append(link));
        });
    var specFiles = $('<td></td>').append(list);
    topicTable.append($('<tr></tr>').append(num, name, description, specFiles, group));
}

$('#submitStudent').on('click', function () {
    showSpinner();
    var studentName = $('input#studentName').val();
    var studentId = $('input#studentId').val();
    var studentGroup = $('input#studentGroup').val();
    var studentEmail = $('input#studentEmail').val();

    var formData = new FormData();
    formData.append('fullName', studentName);
    formData.append("studentId", studentId);
    formData.append("email", studentEmail);
    formData.append("groupName", studentGroup);
    $.when($.ajax({
        url: '/class/students',
        data: formData,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function (student) {
            insertStudent(student, $('.student-table').find('tr').length);
            hidePopup('.add-student-popup')
        },
        error: function () {
            alert('Unable to add student!')
        }
    })).done(function () {
        hideSpinner();
    })
});

$(document).ready(function () {
    showSpinner();
    $.when(getStudents(), getIterations(), getTopics()).done(function () {
        hideSpinner();
    });
});