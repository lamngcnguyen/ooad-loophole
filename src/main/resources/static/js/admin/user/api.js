$.fn.api.settings.api = {
    'create user': '/api/users',
    'delete user': '/api/users/{id}',
    'update user': '/api/users/{id}',
    'create student': '/api/students',
    'update student': '/api/students/{id}',
    'import student': '/api/students/import',
    'delete student': '/api/students/{id}',
    'search class': '/api/classes/search?keyword={query}',
    'get classes': '/api/classes',
};

$.fn.form.settings.rules.oneRoleClaimed = function (value, formId) {
    var isAdmin = $(`${formId} input[name="isAdmin"]`).prop('checked');
    var isTeacher = $(`${formId} input[name="isTeacher"]`).prop('checked');
    return isAdmin || isTeacher;
};

const VN_ALPHABET = 'ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ';

var validationRules = {
    fullName: {
        identifier: 'fullName',
        rules: [
            {
                type: `regExp[/^[a-zA-Z\\s_${VN_ALPHABET}\s]+$/]`,
                prompt: 'Full name invalid'
            }
        ]
    },
    username: {
        identifier: 'username',
        rules: [
            {
                type: 'regExp[/^[a-zA-Z0-9-_]+$/]',
                prompt: 'Username invalid'
            }
        ]
    },
    email: {
        identifier: 'email',
        rules: [
            {
                type: 'email',
                prompt: 'Email invalid'
            }
        ]
    },
    oneRoleClaimed: function (formId) {
        return {
            identifier: 'role-checkbox',
            rules: [{
                type: `oneRoleClaimed[${formId}]`,
                prompt: 'Please select at least 1 role'
            }]
        }
    },
    studentId: {
        identifier: 'studentId',
        rules: [{
            type: 'integer',
            prompt: 'Student ID invalid'
        }],
    },
    classId: {
        identifier: 'classId',
        rules: [
            {
                type: 'empty',
                prompt: 'Please select a class'
            }
        ]
    },
    phoneNumber: {
        identifier: 'phoneNumber',
        rules: [
            {
                type: 'number',
                prompt: 'Phone number invalid'
            },
            {
                type: 'minLength[9]',
                prompt: 'Phone number must have at least {ruleValue} digits'
            }
        ]
    }
};