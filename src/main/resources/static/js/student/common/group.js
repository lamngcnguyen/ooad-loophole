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
        member.find('.member-description').text(`Mã số sinh viên: ${c.studentId}`);
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