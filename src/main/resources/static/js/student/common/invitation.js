const userId = $("#user-id").val();
const invitationId = $("#invitationId").val();

function acceptInvitation() {
    $.api({
        action: 'accept invitation',
        on: 'now',
        method: 'post',
        data: {
            userId: userId,
            invitationId: invitationId
        },
        onSuccess: function () {
            window.location.href = '/student'
        },
        onFailure: function (response) {
            $('.form.invitation-form').form('add errors', [response]);
        }
    });
}

function denyInvitation() {
    $.api({
        action: 'deny invitation',
        on: 'now',
        method: 'post',
        data: {
            userId: userId,
            invitationId: invitationId
        },
        onSuccess: function () {
            window.location.href = '/student'
        },
        onFailure: function (response) {
            $('.form.invitation-form').form('add errors', [response]);
        }
    });
}
