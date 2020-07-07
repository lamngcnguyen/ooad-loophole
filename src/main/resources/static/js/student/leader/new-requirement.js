const groupId = $("input[name='groupId']").val();
const requirementId = $("input[name='requirementId']").val();

$(document).ready(function () {
    $('.selection.dropdown').dropdown();
    $('.dropdown.parent-requirement').dropdown({
        showOnFocus: false,
    }).api({
        action: 'get requirements',
        method: 'get',
        urlData: {groupId: groupId},
        on: 'now',
        onSuccess(response, element, xhr) {
            const values = [];
            xhr.responseJSON.data.forEach(function (requirement) {
                values.push({
                    value: requirement._id,
                    name: requirement.name,
                    text: requirement.name
                })
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('restore default value');
        }
    });
})