$('.ui.dropdown').dropdown();

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
    var image = this.files[this.files.length - 1];
    var imageExtension = image.name.substring(image.name.lastIndexOf('.'));
    var newFileName = $('input[name="userId"]').val() + imageExtension;
    var formData = new FormData();
    formData.append('file', image, newFileName);
    $.ajax({
        url: `/api/users/avatar?t=${new Date().getTime()}`,
        method: 'post',
        data: formData,
        contentType: false,
        processData: false,
        success: function () {
            location.reload();
        }
    });
});