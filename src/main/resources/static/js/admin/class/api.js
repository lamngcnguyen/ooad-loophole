$.fn.api.settings.api = {
    'get teachers': '/api/users/role/teacher',
    'get semesters': '/api/semesters',
    'create class': '/api/classes',
    'update class': '/api/classes/{id}',
    'delete class': '/api/classes/{id}'
};

const validationRules = {
    className: {
        identifier: 'className',
        rules: [
            {
                type: 'empty',
                prompt: 'Class name empty!'
            }
        ]
    },
    teacherId: {
        identifier: 'teacherId',
        rules: [
            {
                type: 'empty',
                prompt: 'Teacher not selected!'
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