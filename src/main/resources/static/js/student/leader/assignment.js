$(document).ready(function () {
    $('.tabular.menu').children(':first').click()
})
$('.assignment.item').click(function () {
    $('.assignment.item').removeClass('active');
    $(this).addClass('active');
    showAssignment($(this).attr('id'));
})

function showAssignment(id) {
    $('.segment.assignment').hide();
    $(`#${id}.segment.assignment`).show();
}