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
                return ++studentRowIndex;
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
            data: "fileNames",
            defaultContent: "Not set",
            render: function (files) {
                return "bruh";
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
                showModal('.modal.delete-topic');
            });
        actionCell.append(btnEdit, btnDelete);
    }
});

function openSpecFileDialog() {
    $('input[name="specFiles"]').click();
}

$('input[name="specFiles"]').change(function () {
    uploadSpecFiles(this.files);
});

function uploadSpecFiles(files) {
    for (var i = 0; i < files.length; i++) {
        const file = files[i];
        const tr = $('<tr></tr>');
        tr.append($('<input type="hidden" name="id">'),
            $(`<td>${file.name}</td>`),
            $('<td class="center aligned">' +
                '<div class="ui active centered inline red loader"></div>' +
                '<div class="upload-success" style="display: none;"><i class="large green check icon"></i></div>' +
                '<div class="upload-failure" style="display: none"><i class="large red close icon"></i></div>' +
                '</td>'),
            $('<td class="center aligned">' +
                '<div class="ui grey icon button delete-file"><i class="trash icon"></i></div>' +
                '<div class="ui blue icon button retry-upload" style="display: none"><i class="redo icon"></i></div>' +
                '</td>')
        );
        $('.table.attachments').append(tr);
        const formData = new FormData();
        formData.append('file', file);
        tr.api({
            action: 'upload spec',
            method: 'post',
            on: 'now',
            cache: false,
            contentType: false,
            processData: false,
            beforeSend: (settings) => {
                settings.data = formData;
                return settings;
            },
            onSuccess(res, el, xhr) {
                el.find('.loader').hide();
                el.find('.upload-success').show();
                var fileInfo = xhr.responseJSON;
                el.find('input[name="id"]').val(fileInfo._id);
                el.find('div.delete-file').click(function () {
                    console.log('delete');
                });
            },
            onFailure(res, el, xhr) {
                el.find('.loader').hide();
                el.find('.upload-failure').show();
                el.find('div.delete-file').hide();
                el.find('div.retry-upload').click(function () {
                    console.log('retry')
                }).show();
            }
        });
    }
}

$('.modal.create-topic').modal({
    onShow: function () {
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
    },
});

$('.form.create-topic').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-topic');
        data['classId'] = classId;
        $('.form.create-topic').api({
            action: 'create topic',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onSuccess: function (res) {
                console.log(res);
                reloadTopicTable();
            },
            onFailure: function (res) {
                $('.form.create-topic').form('add errors', [res]);
            }
        });
    },
    fields: {
        name: validationRules.topicName,
    }
});

function reloadTopicTable() {
    topicRowIndex = 0;
    topicTable.ajax.reload();
}