var classTable = $('.class-table tbody');

$('.btn-add-class button').on('click', function () {
    showPopup('.add-class-popup');
});

$('.popup-close').on('click', function () {
    hidePopup('.add-class-popup');
});

$('#submitClass').on('click', function () {
    createNewClass();
});

function getMyClasses() {
    $.ajax({
        url: '/class/my-class',
        method: 'GET',
        success: function (jqXHR) {
            jqXHR.forEach(function (c, i) {
                addClass(c, i + 1);
            })
        },
        error: function () {
            alert('oof');
        }
    })
}

function addClass(c, index) {
    var idCell = $('<td class="id-cell"></td>').text(c.classId);
    var numCell = $('<td></td>').text(index);
    var nameCell = $('<td class="class-ref"></td>').text(c.className).click(function () {
        window.location.href = '/teacher/class/' + c.className;
    });
    var actionCell = $('<td><i class="fa fa-ellipsis-h td-action-icon"></i>&nbsp</td>');
    var studentCell = $('<td></td>').text(c.numberOfStudents);
    var startDateCell = $('<td></td>').text(c.startDate);
    var endDateCell = $('<td></td>').text(c.endDate);

    var row = $('<tr></tr>')
        .append(idCell, numCell, nameCell, actionCell, studentCell, startDateCell, endDateCell);
    classTable.append(row);
}

function createNewClass() {
    showSpinner();
    var className = $('input#firstName').val();
    var startDate = $('input#startDate').val();
    var endDate = $('input#endDate').val();
    var dayOfWeek = $('select#dayOfWeek option:selected').val();

    $.ajax({
        url: '/class?name=' + className + '&startDate=' + startDate + '&endDate=' + endDate + '&dayOfWeek=' + dayOfWeek,
        method: 'post',
        success: function () {
            window.location.href = '/teacher/class/' + className;
        },
        error: function () {
            alert('Cannot create new class!');
        }
    })
}

$(document).ready(function () {
    getMyClasses();
});