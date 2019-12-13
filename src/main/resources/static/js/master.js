function showPopup(element) {
    $(element).fadeIn();
    $('.my-container').css('filter', 'brightness(80%)');
}

function hidePopup(element) {
    $(element).fadeOut();
    $('.my-container').css('filter', 'brightness(100%)');
}

$('.popup-close').on('click', function (e) {
    // $('.add-class-popup').fadeOut();
    // $('.my-container').css('filter', 'brightness(100%)');
    hidePopup('.add-class-popup');
});
