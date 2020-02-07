const studentTable = $('.student-table tbody');
const importStudentTable = $('.import-student-table tbody');
let importStudents;
const iterationTable = $('.iteration-table tbody');
const attachmentTable = $('.attachment-table tbody');
const topicTable = $('.topic-table tbody');

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

$('.edit-student-popup .popup-close').on('click', function () {
    hidePopup('.edit-student-popup')
});

$('#addTopic').on('click', function () {
    showSpinner();
    $.when($.ajax({
        url: '/class/unassigned-groups',
        method: 'get',
        success: function (jqXHR) {
            const groupSelect = $('select#groupId').empty().append($('<option value="0">Not set</option>'));
            jqXHR.forEach(function (g) {
                const option = $('<option></option>').val(g._id).text(g.groupName);
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
    const groupName = $('input#topicName').val();
    const description = $('textarea#topicDescription').val();
    const groupId = $('select#groupId option:selected').val();
    const files = $('input#inputAddAttachments').get(0).files;

    const formData = new FormData();
    formData.append('topicName', groupName);
    if (description.length > 0)
        formData.append('description', description);
    if (groupId !== 0)
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
    const files = $(this).get(0).files;
    console.log(files);
    for (let i = 0; i < files.length; i++) {
        const value = files[i];
        const fileType = $('<td></td>').append(getFileTypeIcon(value.name));
        const name = $('<td></td>').text(value.name);
        attachmentTable.append($('<tr></tr>').append(fileType, name));
    }
});

document.getElementById('xlsxUpload').addEventListener('change', function (e) {
    parseStudents(e);
    this.value = '';
}, false);

function parseStudents(evt) {
    clearImportTable();
    const file = evt.target.files[0];
    const reader = new FileReader();
    reader.onload = function (e) {
        const data = e.target.result;
        try {
            const workbook = XLSX.read(data, {
                type: 'binary'
            });
        } catch (ex) {
            alert('Unable to process file!');
            return;
        }
        const XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]]);
        XL_row_object.forEach(function (em) {
            // console.log(em);
            const values = Object.values(em);
            const row = $('<tr></tr>');
            const num = $('<td></td>').text(values[0]);
            const name = $('<td></td>').text(values[1]);
            const studentId = $('<td></td>').text(values[2]);
            const email = $('<td></td>').text(values[3]);
            const group = $('<td></td>').text(values[4]);
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
    const wb = XLSX.utils.book_new();
    const ws_name = "students";
    const ws_data = [
        ['#', 'Full name', 'Student ID', 'Email', 'Group'],
        ['1', 'Phong Ha Tuan', '16020263', '16020263@vnu.edu.vn', 'F & Đồng bọn'],
        ['2', 'Nguyen Ngoc Lam', '16022408', '16022408@vnu.edu.vn', 'F & Đồng bọn']
    ];
    const ws = XLSX.utils.aoa_to_sheet(ws_data);
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
    // const id = $('<td class="id-cell"></td>').text(s.userId);
    const num = $('<td class="num-cell"></td>').text(index);
    const fullName = $('<td></td>').text(s.fullName);
    const studentId = $('<td></td>').text(s.studentId);
    const email = $('<td></td>').text(s.email);
    const groupId = $('<td class="id-cell group-id"></td>').text(s.groupId);
    const groupName = $('<td></td>').text(s.groupName);
    const actions = $('<td><i class="fa fa-pencil fa-fw td-action-icon" id="edit-student" onclick="return openStudentEditor(\'' + s.userId + '\')"></i>&nbsp<i class="fa fa-trash fa-fw td-action-icon"></i>&nbsp</td>');
    const row = $('<tr data-id="' + s.userId + '"></tr>').append(num, fullName, studentId, email, groupId, groupName, actions);
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
                const row = $('<tr></tr>');
                const num = $('<td class="num-cell"></td>').text(index + 1);
                const startDateTime = $('<td></td>').text(i.startDateTime);
                const endDateTime = $('<td></td>').text(i.endDateTime);
                const message = $('<td></td>').text(i.message);

                let status;
                const today = new Date();
                const sd = new Date(i.startDateTime);
                const ed = new Date(i.endDateTime);
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
    const num = $('<td class="num-cell"></td>').text(index);
    const description = $('<td></td>').text(topic.descriptions);
    const name = $('<td></td>').text(topic.name);
    const group = $('<td></td>').text(topic.groupName);
    const list = $('<ul style="list-style-type: none; margin: 0; padding: 0;"></ul>');
    if (topic.specificationFiles != null)
        topic.specificationFiles.forEach(function (f) {
            const fileName = f.fileName;
            const fileTypeIcon = getFileTypeIcon(fileName);
            const link = $('<a></a>')
                .attr('href', '/file/download?filePath=' + encodeURIComponent(f.path))
                .attr('target', '_blank')
                .append(fileTypeIcon).append(fileName);
            list.append($('<li></li>').append(link));
        });
    const specFiles = $('<td></td>').append(list);
    topicTable.append($('<tr></tr>').append(num, name, description, specFiles, group));
}

$('#submitStudent').on('click', function () {
    showSpinner();
    const studentName = $('input#studentName');
    const studentId = $('input#studentId');
    const studentGroup = $('input#studentGroup');
    const studentEmail = $('input#studentEmail');

    const formData = new FormData();
    formData.append('fullName', studentName.val());
    formData.append("studentId", studentId.val());
    formData.append("email", studentEmail.val());
    formData.append("groupName", studentGroup.val());
    $.when($.ajax({
        url: '/class/students',
        data: formData,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function (student) {
            insertStudent(student, $('.student-table').find('tr').length);
            hidePopup('.add-student-popup');
            studentName.val("");
            studentId.val("");
            studentEmail.val("");
            studentGroup.val("");
        },
        error: function () {
            alert('Unable to add student!')
        }
    })).done(function () {
        hideSpinner();
    })
});

function openStudentEditor(studentId) {
    const data = $('.student-table tbody tr[data-id="' + studentId + '"]').map(function () {
        const array = [];
        $('td', this).each(function () {
            const d = $(this).val() || $(this).text();
            array.push(d);
        });
        return array;
    });
    showPopup('.edit-student-popup');
    editStudent(data, studentId);
}

function editStudent(dataArray, _id) {
    $('#editStudentName').val(dataArray[1]);
    $('#editStudentId').val(dataArray[2]);
    $('#editStudentEmail').val(dataArray[3]);
    $('#editStudentGroup').val(dataArray[5]);
    $('#editStudent').off().on('click', function () {
        console.log("click");
        if ($('#editStudentName').val() === dataArray[1] &&
            $('#editStudentId').val() === dataArray[2] &&
            $('#editStudentEmail').val() === dataArray[3] &&
            $('#editStudentGroup').val() === dataArray[5]) {
            hidePopup('.edit-student-popup')
        } else {
            showSpinner();
            const studentName = $('input#editStudentName');
            const studentId = $('input#editStudentId');
            const studentGroup = $('input#editStudentGroup');
            const studentEmail = $('input#editStudentEmail');

            console.log("submitting");
            const formData = new FormData();
            formData.append('fullName', studentName.val());
            formData.append("studentId", studentId.val());
            formData.append("email", studentEmail.val());
            formData.append("groupName", studentGroup.val());
            $.when($.ajax({
                url: '/student/' + _id,
                data: formData,
                processData: false,
                contentType: false,
                type: 'PUT',
                success: function () {
                    studentName.val("");
                    studentId.val("");
                    studentEmail.val("");
                    studentGroup.val("");
                    $('.student-table tbody').empty();
                    getStudents();
                    hidePopup('.edit-student-popup')
                },
                error: function () {
                    alert('Unable to edit student info!')
                }
            })).done(function () {
                hideSpinner();
            })
        }
    })
}

$(document).ready(function () {
    showSpinner();
    $.when(getStudents(), getIterations(), getTopics()).done(function () {
        hideSpinner();
    });
});