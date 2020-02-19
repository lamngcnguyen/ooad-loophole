function correctFormData(formElement, data) {
    // correct checkbox value
    $(formElement + ' input[type="checkbox"]').each(function () {
        console.log($(this).prop('name'));
        data[$(this).prop('name')] = $(this).prop('checked');
    });
}