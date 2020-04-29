$.fn.api.settings.api = {
    'get students': '/api/classes/{classId}/students',
    'get students with no group': '/api/classes/{classId}/students/withoutGroup',
    'create student': '/api/students',
    'update student': '/api/students/{id}',
    'import student': '/api/students/import',
    'delete student': '/api/students/{id}',
    'get groups': '/api/classes/{classId}/groups',
    'create group': '/api/groups',
    'edit group': '/api/groups',
    'delete group': '/api/groups/{id}',
    'assign group member': '/api/groups/add',
    'get topics': '/api/classes/{classId}/topics',
    'create topic': '/api/topics',
    'edit topic': '/api/topics/{topicId}',
    'delete topic': '/api/topics/{topicId}',
    'upload spec': '/api/files/spec/topic',
    'delete spec': '/api/files/spec/topic/{id}',
    'upload multiple spec': '/api/files/spec/topic/multi',
    'delete multiple spec': '/api/files/spec/topic/multi',
    'assign topic spec': '/api/files/spec/topic/assign/{id}',
    'assign topic multiple spec': '/api/files/spec/topic/multi/assign/{id}',
    'get assignments': '/api/classes/{classId}/assignments',
    'get assignment': '/api/assignments/{id}',
    'create assignment': '/api/assignments',
    'edit assignment': '/api/assignments/{id}',
    'delete assignment': '/api/assignments/{id}'
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