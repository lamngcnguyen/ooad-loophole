function correctFormData(formElement, data) {
    // correct checkbox value
    $(formElement + ' input[type="checkbox"]').each(function () {
        console.log($(this).prop('name'));
        data[$(this).prop('name')] = $(this).prop('checked');
    });
    $(formElement + ' .calendar input').each(function () {
        console.log($(this).calendar('get date'));
        data[$(this).prop('name')] = $(this).val();
    })
}