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
            .prop('data-value', c._id);
        member.find('.header').text(c.fullName);
        member.find('.member-description').text(`Mã số sinh viên: ${c.studentId}`);
        member.find('img').attr('src', `/api/users/avatar/${c.userId}`);
        $('.members-view:last-child').append(member);
    })
}

$(document).ready(function () {
    $.api({
        action: 'get group members',
        on: 'now',
        method: 'get',
        urlData: {id: Cookies.get("userId")},
        onSuccess: function (res, element, xhr) {
            loadGroupMember(xhr.responseJSON.data);
        },
        onFailure: function (res) {
            console.log(res);
        }
    })
});