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
                prompt: 'Chưa nhập tên lớp!'
            }
        ]
    },
    semesterId: {
        identifier: 'semesterId',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập học kì!'
            }
        ]
    },
    scheduledDayOfWeek: {
        identifier: 'scheduledDayOfWeek',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập ngày học!'
            }
        ]
    }
};

