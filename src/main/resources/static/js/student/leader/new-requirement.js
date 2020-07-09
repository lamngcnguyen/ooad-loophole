const groupId = $("input[name='groupId']").val();
const requirementId = $("input[name='requirementId']").val();

$(document).ready(function () {
    $('textarea[name="description"]').text($('input[name="desc-alt"]').val());
    $('.selection.dropdown').dropdown();
    $('.dropdown.parent-requirement').dropdown({
        showOnFocus: false,
    }).api({
        action: 'get requirements',
        method: 'get',
        urlData: {groupId: groupId},
        on: 'now',
        onSuccess(response, element, xhr) {
            const values = [];
            xhr.responseJSON.data.forEach(function (requirement) {
                if (requirement._id !== requirementId) {
                    values.push({
                        value: requirement._id,
                        name: requirement.name,
                        text: requirement.name
                    })
                }
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('restore default value');
        }
    });
});

$('.form.requirement-form').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'edit requirement',
            urlData: {
                id: requirementId,
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
                    position: 'bottom right',
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
                // logs.forEach(function (log) {
                //     const logMenu = $('.log-menu');
                //     const logSegment = $('.log-segment');
                //     const logItem = $(logMenu.children().get(1)).clone();
                //     logItem.on('click', function () {
                //         $('a.item').removeClass('active');
                //         $('.log-table').hide();
                //         $('a.item#' + $(this).attr('id')).addClass('active');
                //         $('#log_' + $(this).attr('id')).show();
                //     })
                //     logItem.prop('id', log._id);
                //     logItem.removeClass('active');
                //     logItem.find('.type-text').text(log.type);
                //     logItem.find('.timestamp').text(moment(log.timeStamp, 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
                //     logItem.insertAfter($('.log-menu-header'));
                //     const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
                //     logTable.prop('id', `log_${log._id}`);
                //     logTable.find('.member-name').text(log.student.fullName);
                //     logTable.find('.description-text').text(log.description);
                //     logSegment.append(logTable);
                //     logItem.click();
                // })
            }
        });
    }
});

function uploadFile() {
    console.log('called')
    const file = $('#upload-file').prop('files')[0];
    const fd = new FormData();
    fd.append('file', file, file.name);
    fd.append('reqId', requirementId);
    $.api({
        action: 'upload req file',
        on: 'now',
        method: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        onSuccess: function (file) {
            const fileTable = $('.requirement-table');
            const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
            const fileCell = $('#templates .file-cell').clone();
            fileCell.prop('id', file._id);
            fileCell.find('.number').text($(fileTable).children().length + 1);
            fileCell.find('.name').text(file.fileName);
            fileCell.find('.uploader').text(file.uploader.fullName);
            fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
            fileCell.find('.download-file').prop('href', '/api/files/spec/req/' + file._id);
            fileCell.find('.delete-file').click(function () {
                $('.form.delete-file').form('set value', 'id', file._id);
                showModal('.modal.delete-file');
            });
            fileCell.insertBefore($('.upload-row'));
            // const logMenu = $('.log-menu');
            // const logSegment = $('.log-segment');
            // const logItem = $(logMenu.children().get(1)).clone();
            // logItem.on('click', function () {
            //     $('a.item').removeClass('active');
            //     $('.log-table').hide();
            //     $('a.item#' + $(this).attr('id')).addClass('active');
            //     $('#log_' + $(this).attr('id')).show();
            // })
            // logItem.prop('id', file._id);
            // logItem.removeClass('active');
            // logItem.find('.type-text').text("File uploaded");
            // logItem.find('.timestamp').text(moment(new Date(), 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
            // logItem.insertAfter($('.log-menu-header'));
            // const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
            // logTable.prop('id', `log_${file._id}`);
            // logTable.find('.member-name').text($('.full-name').text());
            // logTable.find('.description-text').text("Uploaded " + file.fileName);
            // logSegment.append(logTable);
            // logItem.click();
        },
        onFailure: function (response) {
            $('body').toast({
                message: response,
                class: 'red'
            });
        }
    });
}

$('.button.delete-file').on('click', function () {
    showModal('.modal.delete-file');
    $('.form.delete-file').form('set value', 'id', $(this).attr('id'));
})

$('.form.delete-file').form({
    onSuccess: function (evt, data) {
        const fileName = $(`#file_${data.id} .file-name`).text();
        showDimmer('.modal.delete-file');
        $.api({
            action: 'delete req file',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-file');
                hideModal('.modal.delete-file');
                $(`#${data.id}`).remove();
                // const logMenu = $('.log-menu');
                // const logSegment = $('.log-segment');
                // const logItem = $(logMenu.children().get(1)).clone();
                // logItem.on('click', function () {
                //     $('a.item').removeClass('active');
                //     $('.log-table').hide();
                //     $('a.item#' + $(this).attr('id')).addClass('active');
                //     $('#log_' + $(this).attr('id')).show();
                // })
                // logItem.prop('id', data.id);
                // logItem.removeClass('active');
                // logItem.find('.type-text').text("File deleted");
                // logItem.find('.timestamp').text(moment(new Date(), 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
                // logItem.insertAfter($('.log-menu-header'));
                // const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
                // logTable.prop('id', `log_${data.id}`);
                // logTable.find('.member-name').text($('.full-name').text());
                // logTable.find('.description-text').text("Deleted " + fileName);
                // logSegment.append(logTable);
                // logItem.click()
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-file');
                $('.form.delete-file').form('add errors', [res]);
            }
        });
    },
});