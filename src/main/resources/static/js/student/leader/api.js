$.fn.api.settings.api = {
    'invite student': '/api/groups/invite',
    'get group members': '/api/groups/{id}/members',
    'get students with no group': '/api/classes/{classId}/students/withoutGroup',
};

const validationRules = {
    members: {
        identifier: 'members',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập thành viên!'
            }
        ]
    },
};