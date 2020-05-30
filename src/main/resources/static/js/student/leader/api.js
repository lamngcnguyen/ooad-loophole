$.fn.api.settings.api = {
    'invite student': '/api/groups/invite',
    'get group members': '/api/groups/{id}/members',
    'get students with no group': '/api/classes/{classId}/students/withoutGroup',
    'get iterations': '/api/iterations/{groupId}/group',
    'get iteration': '/api/iterations/{id}',
    'create iteration': '/api/iterations',
    'edit iteration': '/api/iterations/{id}',
    'delete iteration': '/api/iterations/{id}',
    'get class configs': '/api/classes/{classId}/settings',
    'upload code': '/api/files/repo/code',
    'get code': '/api/files/repo/{iterationId}/iteration/code',
    'upload diagram': '/api/files/repo/diagram',
    'get diagram': '/api/files/repo/{iterationId}/iteration/diagram',
    'upload doc': '/api/files/repo/doc',
    'get doc': '/api/files/repo/{iterationId}/iteration/doc',
    'delete file': '/api/files/repo/{id}',
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