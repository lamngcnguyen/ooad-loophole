const groupId = $("input[name='groupId']").val();
const workItemId = $("input[name='workItemId']").val();

$(document).ready(function () {
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
            onSuccess: function () {
                $('body').toast({
                    position: 'bottom right',
                    message: 'Work item updated',
                    class: 'green'
                });
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