const groupId = $("input[name='groupId']").val();
const navMenu = $('.tabular.menu');
const segmentContainer = $('#segmentContainer');
const newIterationItem = $('.item.new');
const btnNewIteration = $('#newIteration');

function showIteration(id) {
    $('.item').removeClass('active');
    $(`#item_${id}`).addClass('active');
    $('.segment.iteration').hide();
    $(`#iteration_${id}`).show();
}

btnNewIteration.click(function () {
    $(this).hide();
    $('#nameInput').show();
    $('#nameInput input').focus();
});

function createIterationForm() {
    var name = $('#nameInput input').val().trim();
    if (name === '') {
        $('body').toast({
            message: 'Name must not be empty',
            class: 'red'
        });
        return;
    }
    var navItem = $(`<a class="item" id="item_0" onclick="showIteration(0)">${name}</a>`);
    var segment = $('<div class="ui iteration segment" id="iteration_0"></div>');
    var form = $('#templates .iteration.form').clone();
    form.children('.header').text(name);
    form.form({
        onSuccess: function () {
            // call api
            // api success function below
            createIterationGrid({
                id: 2,
                name: name
            });
            btnNewIteration.show();
            newIterationItem.show();
            $('#nameInput').hide();
            $('#nameInput input').val('');
        }
    });
    segment.append(form);

    segmentContainer.append(segment);
    newIterationItem.before(navItem);
    newIterationItem.hide();
    showIteration(0);
}

function createIterationGrid(data) {
    var segment = $('#iteration_0');
    segment.empty();
    segment.attr('id', `iteration_${data.id}`);
    var navItem = $('#item_0');
    navItem.unbind().click(function () {
        showIteration(data.id);
    });
    navItem.attr('id', `item_${data.id}`);
    var grid = $('#templates .iteration.grid').clone();
    grid.find('.header').text(data.name);
    segment.append(grid);
}

function cancelIterationForm() {
    $('#iteration_0').remove();
    btnNewIteration.show();
    $('#nameInput').hide();
    $('#nameInput input').val('');
    newIterationItem.show();
}

function loadIterations() {
    $.api({
        action: 'get iterations',
        on: 'now',
        method: 'get',
        urlData: {
            groupId: groupId,
        },
        onSuccess: function (res, element, xhr) {
            xhr.responseJSON.data.forEach(function (iteration) {
                const iterationItem = $(`<a class="item" id="${iteration._id}">${iteration.name}</a>`)
                    .click(function () {
                        showIteration(iteration._id);
                    })
                navMenu.append(iterationItem);
                const firstElement = navMenu.children().get(0);
                firstElement.click();
            })
        }
    })
}

$(document).ready(function () {
    loadIterations();
})