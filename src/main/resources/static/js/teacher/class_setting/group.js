let groupRowIndex = 0;
const topicTable = $(".group .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: false,
    lengthChange: true,
    ajax: "/api/classes/" + classId + "/groups",
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
        {data: "groupName"},
        {data: "leaderId"},
        {
            data: "members",
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