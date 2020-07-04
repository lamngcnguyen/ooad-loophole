const assignmentId = $("input[name='assignmentId']").val()

$(document).ready(function () {
    $('.tabular.menu').children().get(0).click()
})

function showTab(groupId) {
    $('.menu .item').removeClass('blue active');
    $('.tab').removeClass('active');
    $(`.menu .item#${groupId}`).addClass('blue active');
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