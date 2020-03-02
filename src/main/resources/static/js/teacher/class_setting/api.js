$.fn.api.settings.api = {
    'get students': '/api/classes/{classId}/students',
    'create student': '/api/students',
    'update student': '/api/students/{id}',
    'import student': '/api/students/import',
    'delete student': '/api/students/{id}',
};

const VN_ALPHABET = 'ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ';

var validationRules = {
    fullName: {
        identifier: 'fullName',
        rules: [
            {
                type: `regExp[/^[a-zA-Z\\s_${VN_ALPHABET}]+$/]`,
                prompt: 'Họ tên không hợp lệ'
            }
        ]
    },
    studentId: {
        identifier: 'studentId',
        rules: [{
            type: 'integer',
            prompt: 'MSSV không hợp lệ'
        }],
    },
    classId: {
        identifier: 'classId',
        rules: [
            {
                type: 'empty',
                prompt: 'Vui lòng chọn lớp học'
            }
        ]
    },
};