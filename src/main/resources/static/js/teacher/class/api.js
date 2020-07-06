$.fn.api.settings.api = {
    'get my classes': '/api/classes/teacher/{id}',
    'filter my classes': '/api/classes/teacher/{id}/semester/{semesterId}',
    'get semesters': '/api/semesters',
    'create class': '/api/classes',
};

var card_colors = [
    'red', 'blue', 'violet', 'black'
];

const validationRules = {
    className: {
        identifier: 'className',
        rules: [
            {
                type: 'empty',
                prompt: 'Class field is empty!'
            }
        ]
    },
    semesterId: {
        identifier: 'semesterId',
        rules: [
            {
                type: 'empty',
                prompt: 'Semester field is empty!'
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
};

