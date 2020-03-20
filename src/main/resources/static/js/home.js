$('.ui.dropdown').dropdown();

$('.dropdown.notification').dropdown({
    onShow: function () {
        $('.label.unseen').hide();
    }
});

function toggleSidebar() {
    $('.ui.sidebar')
        .sidebar('toggle');
}

$('.my-avatar.image').dimmer({
    on: 'hover'
});

function openAvatarDialog() {
    $('input[name="avatar"]').click();
}

$('input[name="avatar"]').change(function () {
    $(".profile-loader").addClass("active");
    const image = this.files[this.files.length - 1];
    const imageExtension = image.name.substring(image.name.lastIndexOf('.'));
    const newFileName = $('input[name="userId"]').val() + imageExtension;
    const formData = new FormData();
    formData.append('file', image, newFileName);
    $.ajax({
        url: `/api/users/avatar`,
        method: 'post',
        data: formData,
        contentType: false,
        processData: false,
        success: function () {
            location.reload();
        }
    });
});

function getFileIconClass(type) {
    // console.log(type);
    switch (type) {
        case 'pdf':
            return 'red pdf';
        case 'png':
        case 'jpg':
        case 'jpeg':
            return 'teal image';
        case 'xls':
        case 'xlsx':
            return 'green excel';
        case 'ppt':
        case 'pptx':
            return 'orange powerpoint';
        case 'doc':
        case 'docx':
            return 'blue word';
        default:
            return '';
    }
}