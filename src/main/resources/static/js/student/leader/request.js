const userId = $("#user-id").val();
const requestId = $("#requestId").val();

function acceptRequest() {
    $.api({
        action: 'accept request',
        on: 'now',
        method: 'post',
        data: {
            userId: userId,
            requestId: requestId
        },
        onSuccess: function () {
            window.location.href = '/student/group'
        },
        onFailure: function (response) {
            $('.form.request-form').form('add errors', [response]);
        }
    });
}

function denyRequest() {
    $.api({
        action: 'deny request',
        on: 'now',
        method: 'post',
        data: {
            userId: userId,
            requestId: requestId
        },
        onSuccess: function () {
            window.location.href = '/student/group'
        },
        onFailure: function (response) {
            $('.form.request-form').form('add errors', [response]);
        }
    });
}