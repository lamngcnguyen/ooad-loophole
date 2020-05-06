$.fn.api.settings.api = {
    'get class config': '/api/classes/{classId}/settings',
    'group setting': '/api/classes/{classId}/settings/group',
    'iteration setting': '/api/classes/{classId}/settings/iteration'
}

const validationRules = {
    groupMin: {
        identifier: 'groupMin',
        rules: [
            {
                type: 'integer',
                prompt: 'Minimum group size must be a number'
            }
        ]
    },
    groupMax: {
        identifier: 'groupMax',
        rules: [
            {
                type: 'integer',
                prompt: 'Maximum group size must be a number'
            }
        ]
    },
    defaultLength: {
        identifier: 'defaultLength',
        rules: [
            {
                type: 'integer',
                prompt: 'Default iteration length must be a number'
            }
        ]
    },
    maxLength: {
        identifier: 'maxLength',
        rules: [
            {
                type: 'integer',
                prompt: 'Maximum iteration length must be a number'
            }
        ]
    },
}