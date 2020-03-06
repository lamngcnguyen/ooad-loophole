$.fn.api.settings.api = {
    'get students': '/api/classes/{classId}/students',
    'get students with no groups': '/api/classes/{classId}/students',
    'create student': '/api/students',
    'update student': '/api/students/{id}',
    'import student': '/api/students/import',
    'delete student': '/api/students/{id}',
    'get groups': '/api/classes/{classId}/groups',
    'create group': '/api/groups/',
    'assign group member': '/api/groups/add',
    'get topics': '/api/classes/{classId}/topics',
    'create topic': '/api/topics/',
    'delete topic': '/api/topics/{topicId}',
    'upload spec': '/api/files/spec',
    'upload multiple spec': '/api/files/spec/multiple',
    'assign topic spec': '/api/files/spec/assign/{id}',
    'assign topic multiple spec': '/api/files/spec/multi/assign/{id}'
};

const UPLOAD_TIMEOUT = 10000;
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
    topicName: {
        identifier: 'name',
        rules: [
            {
                type: 'empty',
                prompt: 'Vui lòng điền tên đề tài'
            }
        ]
    }
};