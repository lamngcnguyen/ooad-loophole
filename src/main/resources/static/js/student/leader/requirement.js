const btnNewRequirement = $('#newRequirement');
const newRequirementItem = $('.item.new');
const segmentContainer = $('#segmentContainer')

function showRequirement(id) {
    if (!$('#iteration_' + id).is(':visible')) {
        showRequirementDetails(id)
    }
}

function showRequirementDetails(id) {
    $('.item').removeClass('active');
    $(`#item_${id}`).addClass('active');
    $('.segment.requirement').hide();
    $(`#requirement_${id}`).show();
}

btnNewRequirement.click(function () {
    $(this).hide();
    $('#nameInput').show();
    $('#nameInput input').focus();
});

$('.new-requirement').on('keydown', function (e) {
    if (e.which === 13 || e.keyCode === 13) {
        createRequirementForm()
    }
    if (e.which === 27 || e.keyCode === 27) {
        cancelRequirementInput()
    }
})

function cancelRequirementInput() {
    btnNewRequirement.show();
    $('#nameInput').hide();
    $('#nameInput input').val('');
    newRequirementItem.show();
}

function createRequirementForm() {
    const name = $('#nameInput input').val().trim();
    if (name === '') {
        $('body').toast({
            message: 'Name must not be empty',
            class: 'red'
        });
        return;
    }
    const navItem = $(`<a class="item" id="item_0" onclick="showIteration(0)">${name}</a>`);
    const segment = $('<div class="ui requirement segment" id="requirement_0"></div>');
    const form = $('#templates .requirement.form').clone();
    form.find('.header input').val(name);
    form.form({
        onSuccess: function (evt, data) {
            data['name'] = $('input[name=name]').val();
            data['groupId'] = groupId;
            data['startDate'] = $('input[name=startDate]').val();
            data['endDate'] = $('input[name=endDate]').val();
            $.api({
                action: 'create iteration',
                on: 'now',
                method: 'post',
                dataType: 'json',
                data: JSON.stringify(data),
                beforeXHR: (xhr) => {
                    xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
                },
                onFailure: function (response) {
                    $(form).form('add errors', [response])
                },
                onSuccess: function (response) {
                    createIterationGrid(response)
                }
            })
            btnNewRequirement.show();
            newRequirementItem.show();
            $('#nameInput').hide();
            $('#nameInput input').val('');
        }
    });
    segment.append(form);

    segmentContainer.children().hide();
    segmentContainer.append(segment);
    newRequirementItem.before(navItem);
    newRequirementItem.hide();
    showRequirement(0);
}

function cancelRequirementForm() {
    $('#requirement_0').remove();
    $('#item_0').remove();
    btnNewRequirement.show();
    $('#nameInput').hide();
    $('#nameInput input').val('');
    newRequirementItem.show();
    // const firstElement = navMenu.children().get(0);
    // firstElement.click();
    $('#segmentContainer .ui.grid').show();
}