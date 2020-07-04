const assignmentId = $("input[name='assignmentId']").val()
var groupId = null;

$(document).ready(function () {
    //$('.tabular.menu').children().get(0).click()
    $('.group.selection.dropdown').dropdown({
        onChange: function (value) {
            if (value.length > 0) {
                groupId = value;
                showTab(value);
                resetTemplate();
            }
        }
    });
    loadGradingTemplate();
});

function showTab(groupId) {
    //$('.menu .item').removeClass('blue active');
    $('.tab').removeClass('active');
    //$(`.menu .item#${groupId}`).addClass('blue active');
    $(`#group_${groupId}`).addClass('active');
    getRepoFile(groupId);
    getDocumentationFile(groupId);
    getDiagramFile(groupId);
}

function getRepoFile(groupId) {
    $.api({
        action: 'get code',
        on: 'now',
        method: 'get',
        urlData: {
            id: assignmentId,
            groupId: groupId
        },
        onSuccess: function (res, element, xhr) {
            const repoTable = $('#group_' + groupId).find('.repo-table');
            repoTable.empty();
            let fileCount = 0;
            xhr.responseJSON.data.forEach(function (file) {
                const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
                fileCount++;
                const fileCell = $('#templates .file-cell').clone();
                fileCell.prop('id', 'file_' + file._id);
                fileCell.find('.number').text(fileCount);
                fileCell.find('.name').text(file.fileName);
                fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
                fileCell.find('.download-file').prop('href', '/api/files/repo/' + file._id);
                repoTable.append(fileCell);
            })
        }
    })
}

function getDocumentationFile(groupId) {
    $.api({
        action: 'get doc',
        on: 'now',
        method: 'get',
        urlData: {
            id: assignmentId,
            groupId: groupId
        },
        onSuccess: function (res, element, xhr) {
            const docTable = $('#group_' + groupId).find('.doc-table');
            docTable.empty();
            let fileCount = 0;
            xhr.responseJSON.data.forEach(function (file) {
                const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
                fileCount++;
                const fileCell = $('#templates .file-cell').clone();
                fileCell.prop('id', 'file_' + file._id);
                fileCell.find('.number').text(fileCount);
                fileCell.find('.name').text(file.fileName);
                fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
                fileCell.find('.download-file').prop('href', '/api/files/repo/' + file._id);
                docTable.append(fileCell);
            })
        }
    })
}

function getDiagramFile(groupId) {
    $.api({
        action: 'get diagram',
        on: 'now',
        method: 'get',
        urlData: {
            id: assignmentId,
            groupId: groupId
        },
        onSuccess: function (res, element, xhr) {
            const diagramTable = $('#group_' + groupId).find('.diagram-table');
            diagramTable.empty();
            let fileCount = 0;
            xhr.responseJSON.data.forEach(function (file) {
                const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
                fileCount++;
                const fileCell = $('#templates .file-cell').clone();
                fileCell.prop('id', 'file_' + file._id);
                fileCell.find('.number').text(fileCount);
                fileCell.find('.name').text(file.fileName);
                fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
                fileCell.find('.download-file').prop('href', '/api/files/repo/' + file._id);
                diagramTable.append(fileCell);
            })
        }
    })
}

function loadGradingTemplate() {
    var templateId = $('input[name="gradingTemplateId"]').val();
    $.ajax({
        url: '/api/grading-template/' + templateId,
        method: 'get',
        success: function (res) {
            showGradingTemplate(res);
        },
        error: function () {
            alert('unable to get evaluation template');
        }
    });
}

function showGradingTemplate(data) {
    var table = $('.table.template');
    var total = 0;
    $.each(data.criteria, function (i, c) {
        var tr = $('<tr></tr>');
        var tdName = $('<td></td>').text(c.name + ' ').append($('<span class="ui blue text"></span>').text('(' + c.weight + ')'));
        var tdSlider = $('<td class="center aligned"></td>');
        var tdValue = $('<td class="center aligned level-score"></td>').text(c.levels[parseInt(c.levels.length / 2)].weight * c.weight);
        var slider = $('<div class="ui large blue labeled slider"></div>');
        slider.slider({
            min: 0,
            max: c.levels.length - 1,
            step: 1,
            start: parseInt(c.levels.length / 2),
            interpretLabel: function (value) {
                return c.levels[value].levelName + ' ' + `<span class="ui blue text">(${c.levels[value].weight})</span>`
            },
            onMove: function () {
                tdValue.text(c.weight * c.levels[slider.slider('get value')].weight);
                sum();
            }
        });
        tdSlider.append($('<div style="width: 80%; margin-left: 10%;"></div>').append(slider));
        tr.append(tdName, tdSlider, tdValue);
        table.append(tr);
        sum();
    });
}

function sum() {
    var total = 0.0;
    $('.level-score').each(function (i, e) {
        total += parseFloat($(e).text());
    });
    $('.template-total').text(total);
}

function submitEvaluation() {
    var assignmentId = $('input[name="assignmentId"]').val();
    var grade = $('.template-total').text();
    $.ajax({
        url: '/api/assignments/grade',
        method: 'post',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            assignmentId: assignmentId,
            groupId: groupId,
            value: parseFloat(grade)
        }),
        success: function () {
            alert('success saving evaluation score');
        },
        error: function () {
            alert('error saving evaluation score');
        }
    })
}

function resetTemplate() {
    $('.slider').each(function (i, e) {
        var el = $(e);
        var numLabel = el.slider('get numLabels');
        el.slider('set value', parseInt(numLabel / 2));
    });
}