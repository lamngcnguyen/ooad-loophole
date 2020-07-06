var topicRowIndex = 0;
const topicTable = $(".topic .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: false,
    lengthChange: true,
    ajax: "/api/classes/" + classId + "/topics",
    sDom: stringDom,
    language: languageOption,
    columns: [
        {
            data: null,
            render: function () {
                return ++topicRowIndex;
            }
        },
        {
            data: null,
            render: function () {
                return "";
            }
        },
        {data: "name"},
        {data: "descriptions", defaultContent: "Not set"},
        {data: "groupName", defaultContent: "Not set"},
        {
            data: "files",
            defaultContent: "No attachments",
            render: function (files) {
                var html = $('<div class="ui middle aligned list"></div>');
                $.each(files, function (index, file) {
                    var item = $(`<a class="item" href="/api/files/spec/topic/${file._id}" target="_blank"></a>`);
                    item.append(`<div class="ui avatar image"><i class="large file ${getFileIconClass(file.fileExtension)} icon"></i></div>`);
                    item.append(`<div class="content"><div class="header">${file.fileName}</div></div>`);
                    html.append(item);
                });
                return html.prop('outerHTML');
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1], className: "center aligned"}
    ],
    createdRow: function (row, data) {
        const actionCell = $(row).children().eq(1);
        const btnEdit = $('<button type="button" class="ui mini icon blue button" data-tooltip="Chỉnh sửa thông tin" data-inverted=""><i class="pencil icon"></i></button>')
            .click(function () {
                $('.form.edit-topic').form('set values', {
                    id: data._id,
                    name: data.name,
                    descriptions: data.descriptions,
                    groupId: data.groupId
                });
                showModal('.modal.edit-topic');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa đề tài" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                $('.form.delete-topic').form('set values', {
                    id: data._id,
                });
                showModal('.modal.delete-topic');
            });
        actionCell.append(btnEdit, btnDelete);
    }
});

$('.topic .page-length input').change(function () {
    studentTable.page.len(this.value).draw();
});

$('.topic .table-search input').keyup(function () {
    studentTable.search(this.value).draw();
});

function openSpecFileDialog() {
    $('input[name="topicSpecFiles"]').click();
}

$('input[name="topicSpecFiles"]').change(function () {
    createUploadStatus(this.files);
});

function createUploadStatus(files) {
    for (var i = 0; i < files.length; i++) {
        const file = files[i];
        const tr = $('.jquery-template .spec-upload-status').clone();
        tr.find('.file-name').text(file.name);
        $('.table.attachments').append(tr);

        const loader = tr.find('.loader');
        const uploadSuccess = tr.find('.upload-success');
        const uploadFailure = tr.find('.upload-failure');
        const btnDelete = tr.find('div.delete-file');
        const btnRetry = tr.find('div.retry-upload');
        btnRetry.click(function () {
            loader.show();
            uploadFailure.hide();
            requestSpecUpload(tr, file, loader, uploadSuccess, uploadFailure, btnDelete, btnRetry);
        });
        requestSpecUpload(tr, file, loader, uploadSuccess, uploadFailure, btnDelete, btnRetry);
    }
}

function requestSpecUpload(tr, file, loader, uploadSuccess, uploadFailure, btnDelete, btnRetry) {
    const formData = new FormData();
    formData.append('file', file);
    tr.api({
        action: 'upload spec',
        method: 'post',
        on: 'now',
        cache: false,
        contentType: false,
        processData: false,
        timeout: 3000,
        beforeSend: (settings) => {
            settings.data = formData;
            return settings;
        },
        onSuccess: function (res, el, xhr) {
            loader.hide();
            uploadSuccess.show();
            var fileInfo = xhr.responseJSON;
            el.find('input[name="id"]').val(fileInfo._id);
            btnRetry.hide();
            btnDelete.click(function () {
                $('.form.delete-spec').form('set values', {
                    id: fileInfo._id
                });
                showOverlapModal('.modal.delete-spec')
            });
            btnDelete.show();
        },
        onFailure: function (res, el, xhr) {
            loader.hide();
            uploadFailure.show();
            btnDelete.hide();
            btnRetry.show()
        },
        onAbort: function () {
            loader.hide();
            uploadFailure.show();
            btnDelete.hide();
            btnRetry.show()
        }
    });
}

$('.modal.create-topic').modal({
    onShow: function () {
        $('.form.create-topic').form('clear');
        $('.form.create-topic table').find('tr:gt(1)').remove();
    },
    onHide: function () {
        var specIds = [];
        $('.create-topic .spec-upload-status input[name="id"]').each(function (i, el) {
            specIds.push($(el).val());
        });
        clearTempSpecs(specIds, '.modal.create-topic');
    }
});

$('.form.create-topic').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.create-topic');
        data['classId'] = classId;
        $.api({
            action: 'create topic',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onSuccess: function (res) {
                var topicId = res._id;
                var specIds = [];
                $('.create-topic .spec-upload-status input[name="id"]').each(function (i, el) {
                    specIds.push($(el).val());
                });
                assignSpecsToTopic(topicId, specIds, '.modal.create-topic');
            },
            onFailure: function (res) {
                hideDimmer('.modal.create-topic');
                $('.form.create-topic').form('add errors', [res]);
            }
        });
    },
    fields: {
        name: validationRules.topicName,
    }
});

$('.modal.edit-topic').modal({
    onShow: function () {
        $('.form.edit-topic table').find('tr:gt(1)').remove();
    },
});

$('.form.edit-topic').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.edit-topic');
        data['classId'] = classId;
        $.api({
            action: 'edit topic',
            on: 'now',
            method: 'put',
            urlData: {
                'topicId': data.id
            },
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onSuccess: function (res) {
                var topicId = res._id;
                var specIds = [];
                $('.create-topic .spec-upload-status input[name="id"]').each(function (i, el) {
                    specIds.push($(el).val());
                });
                assignSpecsToTopic(topicId, specIds, '.modal.edit-topic');
            },
            onFailure: function (res) {
                hideDimmer('.modal.edit-topic');
                $('.form.edit-topic').form('add errors', [res]);
            }
        });
    },
    fields: {
        name: validationRules.topicName,
    }
});

function assignSpecsToTopic(topicId, specIds, modal) {
    $.api({
        action: 'assign topic multiple spec',
        on: 'now',
        method: 'put',
        urlData: {
            id: topicId,
        },
        dataType: 'json',
        data: JSON.stringify(specIds),
        beforeXHR: function (xhr) {
            xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
        },
        onSuccess: function () {
            hideDimmer(modal);
            hideModal(modal);
            reloadTopicTable();
        },
        onFailure: function (res) {
            hideDimmer(modal);
            $(`${modal} .form`).form('add errors', [res]);
        }
    });
}

function clearTempSpecs(specIds, modal) {
    if(specIds !== []) {
        $.api({
            action: 'delete multiple spec',
            on: 'now',
            method: 'delete',
            dataType: 'json',
            data: JSON.stringify(specIds),
            beforeXHR: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onSuccess: function () {
                hideDimmer(modal);
            },
            onFailure: function (res) {
                hideDimmer(modal);
                $(`${modal} .form`).form('add errors', [res]);
            }
        });
    }
}

$('.form.delete-topic').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.delete-topic');
        $.api({
            action: 'delete topic',
            on: 'now',
            method: 'delete',
            urlData: {
                topicId: data.id
            },
            onSuccess: function () {
                hideDimmer('.modal.delete-topic');
                hideModal('.modal.delete-topic');
                reloadTopicTable();
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-topic');
                $('.form.delete-topic').form('add errors', [res]);
            }
        });
    }
});

$(document).ready(function () {
    $('.dropdown.assigned-group').dropdown().api({
        action: 'get groups',
        on: 'now',
        urlData: {
            'classId': classId
        },
        onSuccess(res, el, xhr) {
            const values = [];
            xhr.responseJSON.data.forEach(function (group) {
                values.push({
                    value: group._id,
                    name: group.groupName
                })
            });
            $(el).dropdown('change values', values);
        }
    });
})

function reloadTopicTable() {
    topicRowIndex = 0;
    topicTable.ajax.reload();
}
