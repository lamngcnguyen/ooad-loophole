$.fn.api.settings.api = {
    'get semesters': '/api/semesters/',
    'create semester': '/api/semesters/',
    'update semester': '/api/semesters/{id}',
    'delete semester': '/api/semesters/{id}'
};

const validationRules = {
    name: {
        identifier: 'name',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập tên học kì!'
            }
        ]
    },
    startDate: {
        identifier: 'startDate',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập ngày bắt đầu!'
            }
        ]
    },
    endDate: {
        identifier: 'endDate',
        rules: [
            {
                type: 'empty',
                prompt: 'Chưa nhập ngày kết thúc!'
            }
        ]
    }
};