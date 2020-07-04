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

function uploadFile() {
    const file = $('#upload-file').prop('files')[0];
    const iterationId = $('input[name=iterationId]').val();
    const fd = new FormData();
    fd.append('file', file, file.name);
    fd.append('path', '');
    fd.append('iterationId', iterationId);
    $.api({
        action: 'upload code',
        on: 'now',
        method: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        onSuccess: function (file) {
            const repoTable = $('#iteration_' + file.iterationId).find('.repo-table');
            console.log($(repoTable).length);
            const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
            const fileCell = $('#templates .code-cell').clone();
            fileCell.prop('id', 'file_' + file._id);
            fileCell.find('.number').text($(repoTable).children().length + 1);
            fileCell.find('.name').text(file.fileName);
            fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
            fileCell.find('.download-file').prop('href', '/api/files/repo/' + file._id);
            fileCell.find('.preview-file').remove();
            fileCell.find('.delete-file').click(function () {
                $('.form.delete-file').form('set value', 'id', file._id);
                showModal('.modal.delete-file');
            });
            repoTable.append(fileCell);
        },
        onFailure: function (xhr) {
            $('.form').form('add errors', [xhr]);
        }
    });
}