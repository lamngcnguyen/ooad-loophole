let groupRowIndex = 0;
const groupTable = $(".group .ui.table").DataTable({
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
        {
            data: "leader",
            render: function (leader) {
                let html = $("<div class='ui middle aligned animated list'></div>");
                let item = $("<div class='item'></div>");
                item.append("<img class='ui avatar image' src='/api/users/avatar/" + leader.userId + "'>");
                item.append("<div class='content'><div class='header'>" + leader.fullName + "</div></div>");
                html.append(item);
                return html.prop('outerHTML');
            }
        },
        {
            data: "members",
            defaultContent: 'No members',
            render: function (members) {
                let html = $("<div class='ui middle aligned animated list'></div>");
                $.each(members, function (index, member) {
                    let item = $("<div class='item'></div>");
                    item.append("<img class='ui avatar image' src='/api/users/avatar/" + member.userId + "'>");
                    item.append("<div class='content'><div class='header'>" + member.fullName + "</div></div>");
                    html.append(item)
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
                $('.form.edit-group').form('set values', {
                    id: data._id,
                    name: data.name,
                    descriptions: data.descriptions,
                    groupId: data.groupId
                });
                showModal('.modal.edit-group');
            });
        const btnDelete = $('<button type="button" class="ui mini icon grey button" data-tooltip="Xóa đề tài" data-inverted=""><i class="trash icon"></i></button>')
            .click(function () {
                showModal('.modal.delete-group');
            });
        actionCell.append(btnEdit, btnDelete);
    }
});

$('.group .page-length input').change(function () {
    groupTable.page.len(this.value).draw();
});

$('.group .table-search input').keyup(function () {
    groupTable.search(this.value).draw();
});

$('.form.create-group').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-group');
        data['classId'] = classId;
        $('.form.create-group').api({
        })
    }
});