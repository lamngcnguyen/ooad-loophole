var fileRowIndex = 0;
const fileTable = $(".file .ui.table").DataTable({
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