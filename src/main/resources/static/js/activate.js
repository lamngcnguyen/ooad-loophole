$.fn.api.settings.api = {
    'activate': '/api/users/activate-account'
};

$('.form.activate').form({
    fields: {
        password: {
            identifier: 'password',
            rules: [{
                type: 'minLength[8]',
                prompt: 'Mật khẩu phải có ít nhất 8 kí tự'
            }]
        },
        confirmPassword: {
            identifier: 'confirmPassword',
            rules: [{
                type: 'match[password]',
                prompt: 'Mật khẩu không khớp!'
            }]
        }
    },
    onSuccess: function (evt, data) {
        $('.form').api({
            action: 'activate',
            urlData: {
                token: data.token,
                userId: data.userId,
                password: data.password
            },
            method: 'post',
            on: 'now',
            onSuccess: function () {
                window.href.location = '/login';
            },
            onFailure: function (res) {
                $('.form').form('add errors', res.message);
            }
        })
    }
});