const groupId = $("input[name='groupId']").val();
const workItemId = $("input[name='workItemId']").val();

$(document).ready(function () {
    $('.log-menu').children().get(1).click();
    $('.selection.dropdown').dropdown();
    $('.dropdown.assignee-dropdown').dropdown({
        showOnFocus: false,
    }).api({
        action: 'get group members',
        method: 'get',
        urlData: {groupId: groupId},
        on: 'now',
        onSuccess(response, element, xhr) {
            const values = [];
            xhr.responseJSON.data.forEach(function (member) {
                values.push({
                    value: member._id,
                    name: member.fullName,
                    text: member.fullName,
                    image: `/api/users/avatar/${member.userId}`,
                    imageClass: "ui avatar image"
                })
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('restore default value');
        }
    });
    $('.dropdown.iteration-dropdown').dropdown({
        showOnFocus: false,
    }).api({
        action: 'get iterations',
        method: 'get',
        urlData: {groupId: groupId},
        on: 'now',
        onSuccess(response, element, xhr) {
            const values = [];
            xhr.responseJSON.data.forEach(function (member) {
                values.push({
                    value: member._id,
                    name: member.name,
                    text: member.name,
                })
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('restore default value');
        }
    });
});

$('.form.work-item').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'edit work item',
            urlData: {
                id: workItemId,
            },
            on: 'now',
            method: 'put',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onFailure: function (response) {
                $('body').toast({
                    message: response,
                    class: 'red'
                });
            },
            onSuccess: function (logs) {
                $('body').toast({
                    position: 'bottom right',
                    message: 'Work item updated',
                    class: 'green'
                });
                logs.forEach(function (log) {
                    const logMenu = $('.log-menu');
                    const logSegment = $('.log-segment');
                    const logItem = $(logMenu.children().get(1)).clone();
                    logItem.on('click', function () {
                        $('a.item').removeClass('active');
                        $('.log-table').hide();
                        $('a.item#' + $(this).attr('id')).addClass('active');
                        $('#log_' + $(this).attr('id')).show();
                    })
                    logItem.prop('id', log._id);
                    logItem.removeClass('active');
                    logItem.find('.type-text').text(log.type);
                    logItem.find('.timestamp').text(moment(log.timeStamp, 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
                    logMenu.append(logItem);
                    const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
                    logTable.prop('id', `log_${log._id}`);
                    logTable.find('.member-name').text(log.student.fullName);
                    logTable.find('.description-text').text(log.description);
                    logSegment.append(logTable);
                })
            }
        });
    }
});

$('input#upload-file').on('change', function () {
    uploadFile();
})

$('.button.delete-file').on('click', function () {
    showModal('.modal.delete-file');
    $('.form.delete-file').form('set value', 'id', $(this).attr('id'));
})

function uploadFile() {
    console.log('called')
    const file = $('#upload-file').prop('files')[0];
    const fd = new FormData();
    fd.append('file', file, file.name);
    fd.append('workItemId', workItemId);
    $.api({
        action: 'upload file',
        on: 'now',
        method: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        onSuccess: function (file) {
            const fileTable = $('.file-table');
            const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
            const fileCell = $('#templates .file-cell').clone();
            fileCell.prop('id', 'file_' + file._id);
            fileCell.find('.number').text($(fileTable).children().length + 1);
            fileCell.find('.name').text(file.fileName);
            fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
            fileCell.find('.download-file').prop('href', '/api/files/repo/' + file._id);
            fileCell.find('.delete-file').click(function () {
                $('.form.delete-file').form('set value', 'id', file._id);
                showModal('.modal.delete-file');
            });
            fileTable.prepend(fileCell);
            const logMenu = $('.log-menu');
            const logSegment = $('.log-segment');
            const logItem = $(logMenu.children().get(1)).clone();
            logItem.on('click', function () {
                $('a.item').removeClass('active');
                $('.log-table').hide();
                $('a.item#' + $(this).attr('id')).addClass('active');
                $('#log_' + $(this).attr('id')).show();
            })
            logItem.prop('id', file._id);
            logItem.removeClass('active');
            logItem.find('.type-text').text("File uploaded");
            logItem.find('.timestamp').text(moment(new Date(), 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
            logMenu.append(logItem);
            const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
            logTable.prop('id', `log_${file._id}`);
            logTable.find('.member-name').text($('.full-name').text());
            logTable.find('.description-text').text("Uploaded " + file.fileName);
            logSegment.append(logTable);
        },
        onFailure: function (response) {
            $('body').toast({
                message: response,
                class: 'red'
            });
        }
    });
}

$('.form.delete-file').form({
    onSuccess: function (evt, data) {
        const fileName = $(`#file_${data.id} .file-name`).text()
        showDimmer('.modal.delete-file');
        $.api({
            action: 'delete file',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-file');
                hideModal('.modal.delete-file');
                $(`#file_${data.id}`).remove();
                const logMenu = $('.log-menu');
                const logSegment = $('.log-segment');
                const logItem = $(logMenu.children().get(1)).clone();
                logItem.on('click', function () {
                    $('a.item').removeClass('active');
                    $('.log-table').hide();
                    $('a.item#' + $(this).attr('id')).addClass('active');
                    $('#log_' + $(this).attr('id')).show();
                })
                logItem.prop('id', data.id);
                logItem.removeClass('active');
                logItem.find('.type-text').text("File deleted");
                logItem.find('.timestamp').text(moment(new Date(), 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
                logMenu.append(logItem);
                const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
                logTable.prop('id', `log_${data.id}`);
                logTable.find('.member-name').text($('.full-name').text());
                logTable.find('.description-text').text("Deleted " + fileName);
                logSegment.append(logTable);
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-file');
                $('.form.delete-file').form('add errors', [res]);
            }
        });
    },
});

$('.log-item').on('click', function () {
    $('a.item').removeClass('active');
    $('.log-table').hide();
    $('a.item#' + $(this).attr('id')).addClass('active');
    $('#log_' + $(this).attr('id')).show();
});