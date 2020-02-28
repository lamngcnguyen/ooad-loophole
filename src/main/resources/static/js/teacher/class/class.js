$(document).ready(function () {
    $('.dropdown.semester').dropdown({
        onChange: function (value) {
            filterClassBySemester(value);
        }
    }).api({
        action: 'get semesters',
        on: 'now',
        onSuccess(response, element, xhr) {
            var values = [];
            xhr.responseJSON.data.forEach(function (s) {
                values.push({
                    value: s._id,
                    name: s.name,
                    text: s.name
                });
            });
            values.push({
                value: '',
                name: 'All',
                text: 'All'
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('set selected', values[0].value);
        }
    });
});

function createClassCards(data) {
    $('.card-view').find('.class.row').remove();
    $('.card-view').append($('<div class="class four column row"></div>'));
    var colCount = 1;
    data.forEach(function (c) {
        var card = $('.card-template').clone()
            .removeClass('card-template')
            .css('display', 'block')
            .addClass(card_colors[colCount - 1])
            .prop('href', `/teacher/class/${c.className}`);
        card.find('.header').text(c.className);
        card.find('.class-description').text(`Thứ ${c.scheduledDayOfWeek + 1}`);
        card.find('.student-count').text(c.studentCount);
        card.find('.semester').text(c.semesterName);
        card.find('.button.student').addClass(card_colors[colCount - 1]).click(function () {
            return false;
        });
        card.find('.setting-redirect').on('click', function () {
            window.open(`/teacher/class/${c.className}/settings`);
            return false;
        });
        card.find('img').attr('src', `/images/card-backgrounds/${colCount}.jpg`);
        $('.card-view .class.row:last-child')
            .append($('<div class="column">').append(card));
        if (colCount === 4) {
            colCount = 1;
            $('.card-view').append($('<div class="class four column row"></div>'));
        } else {
            colCount++;
        }
    });
}

function filterClassBySemester(semesterId) {
    $('.card-view').api({
        action: (semesterId.length === 0) ? 'get my classes' : 'filter my classes',
        on: 'now',
        method: 'get',
        urlData: {
            id: $('input[name="teacherId"]').val(),
            semesterId: semesterId,
        },
        onSuccess: function (res, element, xhr) {
            createClassCards(xhr.responseJSON.data);
        },
        onFailure: function (res) {
            console.log(res);
        },
    });
}