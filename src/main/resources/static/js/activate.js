$.fn.api.settings.api = {
    'activate': '/api/users/activate-account'
};

$('.form.activate').form({
    fields: {
        password: {
            identifier: 'password',
            rules: [{
                type: 'minLength[8]',
                prompt: 'Password must have at least 8 characters'
            }]
        },
        confirmPassword: {
            identifier: 'confirmPassword',
            rules: [{
                type: 'match[password]',
                prompt: 'Passwords not matched!'
            }]
        }
    },
    onSuccess: function (evt, data) {
        console.log("called");
        $.api({
            action: 'activate',
            data: {
                token: data.token,
                userId: data.userId,
                password: data.password
            },
            method: 'post',
            on: 'now',
            onSuccess: function () {
                window.location.href = '/login';
            },
            onFailure: function (res) {
                $('.form').form('add errors', [res]);
            }
        })
    }
});