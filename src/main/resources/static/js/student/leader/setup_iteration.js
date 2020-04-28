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
        alert('name must not be empty');
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