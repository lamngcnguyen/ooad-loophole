var rowIndex = 0;
var stringDom = "<'ui stackable grid'" +
    "<'row dt-table'" +
    "<'sixteen wide column'tr>" +
    ">" +
    "<'row'" +
    "<'seven wide column'i>" +
    "<'right aligned nine wide column'p>" +
    ">" +
    ">";

var languageOption = {
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
};

var userTable = $(".teacher-admin .ui.table").DataTable({
    ordering: true,
    searching: true,
    paging: true,
    autoWidth: true,
    lengthChange: true,
    ajax: "/api/users/role/admin",
    sDom: stringDom,
    language: languageOption,
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
                return "";
            }
        },
        {data: "fullName"},
        {data: "username"},
        {data: "email"},
        {
            data: "active",
            render: function (isActive) {
                if (isActive) return "<i class='green check icon'></i>";
                else return "<i class='red ban icon'></i>"
            }
        }
    ],
    columnDefs: [
        {targets: [0, 1, -1], className: "center aligned"}
    ],
    createdRow: function (row, data) {
        var actionCell = $(row).children().eq(1);
        var btnEdit = $('<button type="button" class="ui mini icon blue button"><i class="pencil icon"></i></button>')
            .click(function () {
                showEditUserModal(data);
            });
        var btnDelete = $('<button type="button" class="ui mini icon grey button"><i class="trash icon"></i></button>')
            .click(function () {
                showDeleteUserModal(data);
            });
        actionCell.append(btnEdit, $('<span>&nbsp</span>'), btnDelete);
    }
});

$('#pageLengthSelect input').change(function () {
    userTable.page.len(this.value).draw();
});

$('#searchTableInput input').keyup(function () {
    userTable.search(this.value).draw();
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

function showImportStudentModal() {
    $('.import-student-modal').modal('show');
}

function closeImportUserModal() {
    $('.import-user-modal').modal('hide');
}

$('.item.teacher-admin').click(function () {
    $(this).addClass('active');
    $('.tab.teacher-admin').addClass('active');
    $('.item.student').removeClass('active');
    $('.tab.student').removeClass('active');
});

$('.item.student').click(function () {
    $(this).addClass('active');
    $('.tab.student').addClass('active');
    $('.item.teacher-admin').removeClass('active');
    $('.tab.teacher-admin').removeClass('active');
});

function filterRole(roleName) {
    rowIndex = 0;
    userTable.ajax.url('/api/users/role/' + roleName).load();
    userTable.draw();
}

