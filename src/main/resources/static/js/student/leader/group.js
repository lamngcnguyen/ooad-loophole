$('.dropdown.unassigned-student-dropdown').dropdown({
    showOnFocus: false,
}).api({
    action: 'get students with no group',
    method: 'get',
    urlData: {classId: $('.class-id-input').val()},
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (student) {
            values.push({
                value: student._id,
                name: student.fullName + ' - ' + student.studentId,
                text: student.fullName
            })
        });
        $(element).dropdown('change values', values);
    }
});

$('.form.invite-student').form({
    onSuccess: function (evt, data) {
        data['members'] = $('.form.invite-student').form('get value', 'members').split(',');
        $.api({
            action: 'invite student',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
            },
            onFailure: function (response) {
                $('.form.invite-student').form('add errors', [response]);
            },
            onSuccess: function () {
                hideModal('.modal.invite-student');
                $('.my-center.flex').toast({
                    message: 'Gửi lời mời thành công',
                    position: 'bottom right',
                    class: 'teal'
                })
            }
        })
    },
    fields: {
        members: validationRules.members,
    }
});