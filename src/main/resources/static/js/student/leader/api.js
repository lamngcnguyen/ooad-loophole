$.fn.api.settings.api = {
    'invite student': '/api/groups/invite',
    'get group members': '/api/groups/{id}/members',
    'get students with no group': '/api/classes/{classId}/students/withoutGroup',
    'get iterations': '/api/iterations/{groupId}/group',
    'create iteration': '/api/iterations',
    'delete iteration': '/api/iterations/{id}',
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