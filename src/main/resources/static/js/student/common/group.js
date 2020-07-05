function showTab(tabName) {
    $('.menu .item').removeClass('blue active');
    $('.tab').removeClass('active');
    $(`.menu .item.${tabName}`).addClass('blue active');
    $(`.tab.${tabName}`).addClass('active');
}

function loadGroupMember(data) {
    data.forEach(function (c) {
        const member = $('.group-member-template').clone()
            .removeClass('group-member-template')
            .css('display', 'flex')
            .prop('id', c._id);
        member.find('.header').text(c.fullName);
        member.find('.member-description').text(`Student ID: ${c.studentId}`);
        member.find('img').attr('src', `/api/users/avatar/${c.userId}`);
        if (Cookies.get('userId') !== c.userId) {
            member.find('.remove-member').click(function () {
                $('.form.remove-member').form('set values', {
                    id: c._id,
                });
                showModal('.modal.remove-member');
            });
        } else {
            member.find('.remove-member').remove();
        }
        $('.members-view:last-child').append(member);
    })
}

$('.form.leave-group').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'remove group member',
            on: 'now',
            method: 'delete',
            urlData: {id: $('.group-id-input').val()},
            data: {memberId: $('.student-id-input').val()},
            onFailure: function (response) {
                $('.form.leave-group').form('add errors', [response]);
            },
            onSuccess: function () {
                window.location.reload();
            }
        })
    }
})

$(document).ready(function () {
    $.api({
        action: 'get group members',
        on: 'now',
        method: 'get',
        urlData: {id: $('.group-id-input').val()},
        onSuccess: function (res, element, xhr) {
            loadGroupMember(xhr.responseJSON.data);
        },
        onFailure: function (res) {
            console.log(res);
        }
    })
});