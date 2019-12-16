function showPopup(element) {
    $(element).css("display", "flex").hide().fadeIn();
    $('.my-container').css('filter', 'brightness(80%)');
}

function hidePopup(element) {
    $(element).fadeOut();
    $('.my-container').css('filter', 'brightness(100%)');
}

function showSpinner() {
    $('.loader-container').show();
    $('.my-container').css('filter', 'brightness(80%)');
}

function hideSpinner() {
    $('.loader-container').hide();
    $('.my-container').css('filter', 'brightness(100%)');
}


$('.popup-close').on('click', function () {
    hidePopup('.add-class-popup');
});

function getFileTypeIcon(fileName) {
    var type = fileName.substring(fileName.lastIndexOf('.') + 1);
    var icon;
    switch (type) {
        case 'pdf':
            icon = 'pdf';
            break;
        case 'doc':
        case 'docx':
            icon = 'word';
            break;
        case 'png':
        case 'jpeg':
        case 'jpg':
            icon = 'image';
            break;
        case 'xlsx':
        case 'xls':
            icon = 'xls';
            break;
        case 'xml':
            icon = 'xml';
            break;
        case 'java':
            icon = 'code';
            break;
        case 'zip':
            icon = 'zip';
            break;
        default:
            icon = 'document';
            break;
    }
    var src = "/assets/repo-icons/" + icon + '.png';
    return $('<img class="file-type-icon">').attr('src', src);
}