var rowIndex = 0;
var table = $(".ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/users",
    sDom: "<'ui stackable grid'" +
        "<'row dt-table'" +
        "<'sixteen wide column'tr>" +
        ">" +
        "<'row'" +
        "<'seven wide column'i>" +
        "<'right aligned nine wide column'p>" +
        ">" +
        ">",
    language: {
        "emptyTable": "Không có dữ liệu",
        "info": "Hiển thị từ _START_ đến _END_ trong _TOTAL_ mục",
        "infoEmpty": "Hiển thị từ 0 đến 0 trong 0 mục",
        "infoFiltered": "(lọc từ tổng cộng _MAX_ mục)",
        "loadingRecords": "Đang tải...",
        "zeroRecords": "Không tìm thấy kết quả phù hợp",
        "paginate": {
            "first": "Đầu",
            "last": "Cuối",
            "next": "Tiếp",
            "previous": "Trước"
        }
    },
    columns: [
        {
            data: null,
            render: function () {
                return ++rowIndex;
            }
        },
        {
            data: null,
            render: function () {
                return '<button type="button" class="ui mini icon blue button"><i class="pencil icon"></i></button>' +
                    '&nbsp<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>'
            }
        },
        {data: "fullName"},
        {data: "username"},
        {data: "email"},
        {
            data: "status",
            render: function (isActive) {
                if (isActive) return "<i class='green check icon'></i>";
                else return "<i class='red ban icon'></i>"
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1, -1], className: "center aligned"}
    ],
    rowCallback: function (row, data) {
        var buttons = $(row).find('button');
        $(buttons[0]).click(function () {
            showEditUserModal(data);
        });
        $(buttons[1]).click(function () {
            showDeleteUserModal(data);
        });
    }
});

$('#pageLengthSelect input').change(function () {
    table.page.len(this.value).draw();
});

$('#searchTableInput input').keyup(function () {
    table.search(this.value).draw();
});

function showCreateUserModal() {
    $('.create-user-modal').modal('show');
}

function closeCreateUserModal() {
    $('.create-user-modal').modal('hide');
}

function showEditUserModal(data) {
    console.log(data);
    $('.edit-user-modal').modal('show');
}

function closeEditUserModal() {
    $('.edit-user-modal').modal('hide');
}

function showDeleteUserModal(data) {
    console.log(data);
    $('.delete-user-modal').modal('show');
}

function closeDeleteUserModal() {
    $('.delete-user-modal').modal('hide');
}

function showImportUserModal() {
    $('.import-user-modal').modal('show');
}

function closeImportUserModal() {
    $('.import-user-modal').modal('hide');
}