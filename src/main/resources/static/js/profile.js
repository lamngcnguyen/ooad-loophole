$.fn.api.settings.api = {
    'upload avatar': '/api/users/role/teacher',
    'update user': '/api/semesters'
};

const VN_ALPHABET = 'ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ';

const validationRules = {
    fullName: {
        identifier: 'fullName',
        rules: [
            {
                type: `regExp[/^[a-zA-Z\\s_${VN_ALPHABET}]+$/]`,
                prompt: 'Họ tên không hợp lệ'
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
    phoneNumber: {
        identifier: 'phoneNumber',
        rules: [
            {
                type: 'number',
                prompt: 'SĐT không hợp lệ'
            },
            {
                type: 'minLength[9]',
                prompt: 'SĐT phải có ít nhất {ruleValue} chữ số'
            }
        ]
    }
};

var userId = $('#userId').val();

$('.my-avatar.image').dimmer({
    on: 'hover'
});

function openAvatarUploadDialog() {
    $('#avatarUpload').click();
}

function uploadAvatar() {
    var file = $('#avatarUpload').prop('files')[0];
    console.log(file.name);
    var fd = new FormData();
    var fileExtension = file.name.substring(file.name.lastIndexOf('.'));
    fd.append('icon', file, userId + fileExtension);
    $.api({
        action: 'upload avatar',
        urlData: {
            'userId': userId
        },
        on: 'now',
        method: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        onSuccess: function () {
            window.location.reload();
        },
        onFailure: function (xhr) {
            $('.form').form('add errors', [xhr]);
        }
    });
}

$(document).ready(function () {
    $('.form').form({
        onSuccess: function (e, data) {
            $('.button').addClass('loading');
            $.api({
                on: 'now',
                action: 'update user',
                method: 'PUT',
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                onSuccess: function () {
                    window.location.reload();
                },
                onFailure: function (xhr) {
                    $('.form').form('add errors', [xhr]);
                    $('.button').removeClass('loading');
                },
            })
        },
        fields: {
            fullName: validationRules.fullName,
            email: validationRules.email,
            phoneNumber: validationRules.phoneNumber
        }
    })
});