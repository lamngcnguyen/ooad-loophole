$('.modal.create-assignment').modal({
    onShow: function () {
        $('.modal.create-assignment .deadline-picker').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            }
        });
    }
});

$('.form.create-assignment').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-assignment');
        correctFormData('.form.create-assignment', data);
        data['classId'] = classId;
        $.api({
            action: 'create assignment',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;char=utf-8');
            },
            onSuccess: function () {
                hideDimmer('.modal.create-assignment');
                hideModal('.modal.create-assignment');

            },
            onFailure: function (response) {
                hideDimmer('.modal.create-assignment');
                $('.form.create-assignment').form('add errors', [response]);
            }
        })
    }
});