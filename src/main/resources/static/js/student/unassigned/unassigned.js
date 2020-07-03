$('.form.create-group').form({
    onSuccess: function (evt, data) {
        //TODO: Invite student
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
});

$('.dropdown.group-dropdown').dropdown({
    showOnFocus: false,
}).api({
    action: 'get groups',
    method: 'get',
    urlData: {classId: $('.class-id-input').val()},
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (group) {
            values.push({
                value: group._id,
                name: group.groupName,
                text: group.groupName,
            })
        });
        $(element).dropdown('change values', values);
    }
});

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

$('.form.create-request').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'create request',
            on: 'now',
            method: 'post',
            data: {
                userId: $('.student-id-input').val(),
                groupId: data['groupId']
            },
            onFailure: function (response) {
                $('.form.create-request').form('add errors', [response]);
            },
            onSuccess: function () {
                $('body').toast({
                    message: 'Request sent',
                    position: 'bottom right',
                    class: 'green'
                })
            }
        })
    },
});
