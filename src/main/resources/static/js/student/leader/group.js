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
                $('.my-center.flex').toast({
                    message: 'Gửi lời mời thành công',
                    class: 'teal'
                })
            }
        })
    },
});

function showToast() {
    $('.my-center').toast({
        message: 'Gửi lời mời thành công',
        class: 'teal',
        selector: {
            container: '.my-center'
        }
    })
}