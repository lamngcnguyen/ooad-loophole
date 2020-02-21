$.fn.api.settings.api = {
    'create user': '/api/users',
    'delete user': '/api/users/{id}',
    'update user': '/api/users/{id}',
    'set user active': '/api/users/{id}/status?active={isActive}',
    'create student': '/api/students',
    'update student': '/api/students/{id}'
};

$.fn.form.settings.rules.oneRoleClaimed = function (value, formId) {
    var isAdmin = $(`${formId} input[name="isAdmin"]`).prop('checked');
    var isTeacher = $(`${formId} input[name="isTeacher"]`).prop('checked');
    return isAdmin || isTeacher;
};

const VN_ALPHABET = 'ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ';

var validationRules = {
    fullName: {
        identifier: 'fullName',
        rules: [
            {
                type: `regExp[/^[a-zA-Z\\s_${VN_ALPHABET}]+$/]`,
                prompt: 'Họ tên không hợp lệ'
            }
        ]
    },
    username: {
        identifier: 'username',
        rules: [
            {
                type: 'regExp[/^[a-zA-Z0-9-_]+$/]',
                prompt: 'Tên đăng nhập không hợp lệ'
            }
        ]
    },
    email: {
        identifier: 'email',
        rules: [
            {
                type: 'email',
                prompt: 'Email không hợp lệ'
            }
        ]
    },
    oneRoleClaimed: function (formId) {
        return {
            identifier: 'role-checkbox',
            rules: [{
                type: `oneRoleClaimed[${formId}]`,
                prompt: 'Vui lòng chọn ít nhất một quyền'
            }]
        }
    }
};