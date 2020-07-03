$('.dropdown.topic-dropdown').dropdown({
    showOnFocus: false,
}).api({
    action: 'get topics',
    method: 'get',
    urlData: {classId: $('.class-id-input').val()},
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (group) {
            values.push({
                value: group._id,
                name: group.name,
                text: group.name,
            })
        });
        $(element).dropdown('change values', values);
    }
});

$('.form.select-topic').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'select topic',
            on: 'now',
            method: 'post',
            urlData: {
                topicId: data.topicId
            },
            data: {
                groupId: $('.group-id-input').val()
            },
            onFailure: function (response) {
                $('.form.select-topic').form('add errors', [response]);
            },
            onSuccess: function () {
                window.location.reload();
            }
        })
    }
})

function showTopicDetails() {
    const topicId = $('.form.select-topic').form('get value', 'topicId');
    if (topicId !== "") {
        $.api({
            action: 'get topic details',
            on: 'now',
            method: 'get',
            urlData: {
                topicId: topicId
            },
            onFailure: function (response) {
                $('.form.select-topic').form('add errors', [response]);
            },
            onSuccess: function (topic) {
                showModal('.modal.topic-details');
                $('.modal.topic-details .topic-name').text(topic.name);
                $('.modal.topic-details .description-text').text(topic.descriptions);
                loadTopicFiles(topic)
            }
        })
    } else {
        $('.my-center.flex').toast({
            message: 'No topic selected',
            position: 'bottom right',
            class: 'red'
        })
    }
}

function loadTopicFiles(topic) {
    const topicFileTable = $('.topic-table');
    topicFileTable.empty();
    let fileCount = 0;
    topic.files.forEach(function (file) {
        const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
        fileCount++;
        const fileCell = $('#templates .file-cell').clone();
        fileCell.prop('id', 'file_' + file._id);
        fileCell.find('.number').text(fileCount);
        fileCell.find('.name').text(file.fileName);
        fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
        fileCell.find('.download-file').prop('href', '/api/files/spec/topic/' + file._id);
        topicFileTable.append(fileCell);
        // $('#templates table').remove(fileCell);
    })
}

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

$('.form.remove-member').form({
    onSuccess: function (evt, data) {
        const memberId = data['id'];
        $.api({
            action: 'remove group member',
            on: 'now',
            method: 'delete',
            urlData: {id: $('.group-id-input').val()},
            data: {memberId: memberId},
            onFailure: function (response) {
                $('.form.remove-member').form('add errors', [response]);
            },
            onSuccess: function () {
                hideModal('.modal.remove-member');
                $(`.item#${memberId}`).remove();
                $('.my-center.flex').toast({
                    message: 'Member removed',
                    position: 'bottom right',
                    class: 'green'
                })
            }
        })
    }
})