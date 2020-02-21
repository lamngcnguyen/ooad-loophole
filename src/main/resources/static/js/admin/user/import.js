function createStudentImportTemplate() {
    var wb = XLSX.utils.book_new();
    var ws_name = "students";
    var ws_data = [
        ['#', 'Full name', 'Student ID', 'Email', 'Group'],
        ['1', 'Phong Ha Tuan', '16020263', '16020263@vnu.edu.vn', 'F & Đồng bọn'],
        ['2', 'Nguyen Ngoc Lam', '16022408', '16022408@vnu.edu.vn', 'F & Đồng bọn']
    ];
    var ws = XLSX.utils.aoa_to_sheet(ws_data);
    XLSX.utils.book_append_sheet(wb, ws, ws_name);
    XLSX.writeFile(wb, 'students.xlsx');
}