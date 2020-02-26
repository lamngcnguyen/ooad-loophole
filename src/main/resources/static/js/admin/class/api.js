$.fn.api.settings.api = {
    'get teachers': '/api/users/role/teacher',
    'get semesters': '/api/semesters/',
    'create class': '/api/classes/',
    'update class': '/api/classes/'
};

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
    teacherId: {
        identifier: 'teacherId',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập giáo viên!'
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