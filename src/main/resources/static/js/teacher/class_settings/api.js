$.fn.api.settings.api = {
    'update class': '/api/classes/{id}',
    'get class config': '/api/classes/{classId}/settings',
    'get class disciplines config': '/api/classes/{classId}/settings/disciplines',
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
    className: {
        identifier: 'className',
        rules: [
            {
                type: 'empty',
                prompt: 'Class name empty!'
            }
        ]
    },
    scheduledDayOfWeek: {
        identifier: 'scheduledDayOfWeek',
        rules: [
            {
                type: 'empty',
                prompt: 'Date field is empty!'
            }
        ]
    }
}