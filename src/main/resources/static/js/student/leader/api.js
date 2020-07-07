$.fn.api.settings.api = {
    'get topics': '/api/classes/{classId}/topics/unassigned',
    'select topic': '/api/topics/{topicId}/assign',
    'get topic details': '/api/topics/{topicId}',
    'get topic files': '/api/files/spec/topics/{topicId}',
    'invite student': '/api/groups/invite',
    'get group members': '/api/groups/{id}/members',
    'remove group member': '/api/groups/{id}/members',
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
    'edit file': '/api/files/repo',
    'delete file': '/api/files/repo/{id}',
    'get previous versions': '/api/files/repo/{id}/previous',
    'get deleted files': '/api/files/repo/{iterationId}/iteration/deleted',
    'restore files': '/api/files/repo/{id}/restore',
    'create item': '/api/board/item',
    'accept request': '/api/request/accept',
    'deny request': '/api/request/deny',
    'get requirements': '/api/requirement/group/{groupId}',
    'edit requirement': '/api/requirement/{id}'
};

const validationRules = {
    members: {
        identifier: 'members',
        rules: [
            {
                type: 'empty',
                prompt: 'Members selection is empty!'
            }
        ]
    },
};