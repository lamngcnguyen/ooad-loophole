function showModal(el) {
    $(el).modal('show')
}

function hideModal(el) {
    $(el).modal('hide')
}

function validateForm(el) {
    $(el).form('is valid');
}

function resetForm(el) {
    $(el).form('reset');
}

function clearForm(el) {
    $(el).form('clear');
}

function showDimmer(el) {
    $(`${el} .dimmer`).addClass('active');
}

function hideDimmer(el) {
    $(`${el} .dimmer`).removeClass('active');
}