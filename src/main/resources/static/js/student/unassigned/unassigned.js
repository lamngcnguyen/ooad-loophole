$('.form.create-group').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'create group',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
            },
            onFailure: function (response) {
                $('.form.create-group').form('add errors', [response]);
            },
            onSuccess: function () {
                location.reload()
            }
        })
    },
    fields: {
        name: validationRules.name,
        startDate: validationRules.startDate,
        endDate: validationRules.endDate
    }
});