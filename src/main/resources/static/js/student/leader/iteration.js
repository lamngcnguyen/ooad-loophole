const groupId = $("input[name='groupId']").val();
const classId = $("input[name='classId']").val();
const navMenu = $('.tabular.menu');
const segmentContainer = $('#segmentContainer');
const newIterationItem = $('.item.new');
const btnNewIteration = $('#newIteration');
let defaultIterationLength;
let maxIterationLength;

$(document).ready(function () {
    getClassConfigs();
})

function getClassConfigs() {
    $.api({
        action: 'get class configs',
        on: 'now',
        method: 'get',
        urlData: {
            classId: classId,
        },
        onSuccess: function (res) {
            defaultIterationLength = res.defaultIterationLength;
            maxIterationLength = res.maxIterationLength;
        }
    })
}

function showIteration(id) {
    $('.item').removeClass('active');
    $(`#item_${id}`).addClass('active');
    $('.segment.iteration').hide();
    $(`#iteration_${id}`).show();
    $('.form.delete-iteration').form('set value', 'id', id);
    $('input[name=iterationId]').val(id);
    getRepoFile(id);
}

btnNewIteration.click(function () {
    $(this).hide();
    $('#nameInput').show();
    $('#nameInput input').focus();
});

$('.new-iteration').on('keydown', function (e) {
    if (e.which === 13 || e.keyCode === 13) {
        createIterationForm()
    }
    if (e.which === 27 || e.keyCode === 27) {
        cancelIterationInput()
    }
})

function createIterationForm() {
    const name = $('#nameInput input').val().trim();
    if (name === '') {
        $('body').toast({
            message: 'Name must not be empty',
            class: 'red'
        });
        return;
    }
    const navItem = $(`<a class="item" id="item_0" onclick="showIteration(0)">${name}</a>`);
    const segment = $('<div class="ui iteration segment" id="iteration_0"></div>');
    const form = $('#templates .iteration.form').clone();
    form.find('.header input').val(name);
    form.find('.start-date-picker').calendar({
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        },
        endCalendar: form.find('.end-date-picker'),
        onChange: function (date) {
            const maxDate = new Date(date);
            const defaultDate = new Date(date);
            maxDate.setDate(date.getDate() + maxIterationLength);
            defaultDate.setDate(date.getDate() + defaultIterationLength);
            form.find('.end-date-picker').calendar('set maxDate', maxDate);
            form.find('.end-date-picker').calendar('set date', defaultDate);
        }
    });
    form.find('.end-date-picker').calendar({
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        },
        startCalendar: form.find('.start-date-picker')
    });
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

function showEditIterationForm(id) {
    $.api({
        action: 'get iteration',
        on: 'now',
        method: 'get',
        urlData: {
            id: id,
        },
        onFailure: function (response) {
            console.log(response)
        },
        onSuccess: function (response) {
            editIterationForm(response)
        }
    })
}

function editIterationForm(data) {
    const iterationId = data._id;
    const name = data.name;
    const startDate = data.startDateTime;
    const endDate = data.endDateTime;
    const segment = $('#iteration_' + iterationId);
    const form = $('#templates .iteration.form').clone();
    segment.children('.iteration.grid').hide();
    form.find('.grey.button').unbind().click(function () {
        segment.children('.iteration.grid').show();
        form.hide();
    })
    form.find('.header input').val(name);
    form.find('.start-date-picker').calendar({
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        },
        endCalendar: form.find('.end-date-picker'),
        onChange: function (date) {
            const maxDate = new Date(date);
            const defaultDate = new Date(date);
            maxDate.setDate(date.getDate() + maxIterationLength);
            defaultDate.setDate(date.getDate() + defaultIterationLength);
            form.find('.end-date-picker').calendar('set maxDate', maxDate);
            form.find('.end-date-picker').calendar('set date', defaultDate);
        }
    });
    form.find('.end-date-picker').calendar({
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        },
        startCalendar: form.find('.start-date-picker')
    });
    form.find('.start-date-picker').calendar('set date', new Date(startDate));
    form.find('.end-date-picker').calendar('set date', new Date(endDate));
    form.find('textarea[name=objective]').val(data.objective);
    form.form({
        onSuccess: function (evt, data) {
            data['name'] = $('input[name=name]').val();
            data['groupId'] = groupId;
            data['startDate'] = $('input[name=startDate]').val();
            data['endDate'] = $('input[name=endDate]').val();
            $.api({
                action: 'edit iteration',
                on: 'now',
                method: 'put',
                urlData: {
                    id: iterationId,
                },
                dataType: 'json',
                data: JSON.stringify(data),
                beforeXHR: (xhr) => {
                    xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
                },
                onFailure: function (response) {
                    $(form).form('add errors', [response])
                },
                onSuccess: function (response) {
                    segment.remove();
                    $('#item_' + response._id).text(response.name);
                    createIterationGrid(response)
                }
            })
            btnNewIteration.show();
            newIterationItem.show();
            $('#nameInput').hide();
            $('#nameInput input').val('');
        }
    });
    segment.append(form);
}

function createIterationGrid(data) {
    const startDate = new Date(data.startDateTime);
    const endDate = new Date(data.endDateTime);

    //create iteration segment
    let segment;
    if ($('#iteration_0').length !== 0) {
        segment = $('#iteration_0');
        segment.empty();
    } else {
        segment = $('<div class="ui iteration segment"></div>');
    }
    segment.attr('id', `iteration_${data._id}`);
    const navItem = $('#item_0');
    navItem.unbind().click(function () {
        showIteration(data._id);
    });
    navItem.attr('id', `item_${data._id}`);
    navItem.text(data.name);
    navItem.removeAttr('onclick');
    $('.form.delete-iteration').form('set value', 'id', data._id);

    //create iteration data grid
    const grid = $('#templates .iteration.grid').clone();
    grid.find('.iteration-name').text(data.name);
    grid.find('.form-date').text(startDate.toLocaleDateString("en-GB"));
    grid.find('.to-date').text(endDate.toLocaleDateString("en-GB"));
    grid.find('.objective-text').text(data.objective);
    grid.find('.edit-iteration').click(function () {
        showEditIterationForm(data._id)
    });
    segment.append(grid);
    segmentContainer.append(segment);
}

function cancelIterationForm() {
    $('#iteration_0').remove();
    $('#item_0').remove();
    btnNewIteration.show();
    $('#nameInput').hide();
    $('#nameInput input').val('');
    newIterationItem.show();
    const firstElement = navMenu.children().get(0);
    firstElement.click();
}

function cancelIterationInput() {
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
                createIterationGrid(iteration);
                const iterationItem = $(`<a class="item" id="item_${iteration._id}">${iteration.name}</a>`)
                    .click(function () {
                        showIteration(iteration._id);
                    })
                if (newIterationItem.length !== 0) {
                    newIterationItem.before(iterationItem);
                } else {
                    navMenu.append(iterationItem);
                }
            })
            const firstElement = navMenu.children().get(0);
            firstElement.click();
        }
    })
}

$('.form.delete-iteration').form({
    onSuccess: function (evt, data) {
        showDimmer('.modal.delete-iteration');
        $.api({
            action: 'delete iteration',
            urlData: {
                id: data.id
            },
            on: 'now',
            method: 'delete',
            onSuccess: function () {
                hideDimmer('.modal.delete-iteration');
                hideModal('.modal.delete-iteration');
                $(`#item_${data.id}`).prev().click();
                $(`#item_${data.id}`).remove();
                $(`#iteration_${data.id}`).remove();
            },
            onFailure: function (res) {
                hideDimmer('.modal.delete-iteration');
                $('.form.delete-iteration').form('add errors', [res]);
            }
        });
    },
});

function getRepoFile(iterationId) {
    $.api({
        action: 'get repo file',
        on: 'now',
        method: 'get',
        urlData: {
            iterationId: iterationId
        },
        onSuccess: function (res, element, xhr) {
            $('.repo-table').empty();
            let fileCount = 0;
            xhr.responseJSON.data.forEach(function (file) {
                const date = moment(file.fileTimeStamp, 'YYYYMMDD_HHmmss').toDate();
                fileCount++;
                const fileCell = $('#templates .code-cell').clone();
                fileCell.find('.number').text(fileCount);
                fileCell.find('.name').text(file.fileName);
                fileCell.find('.timestamp').text(date.toLocaleString("en-GB"));
                fileCell.find('.delete-file').click(function () {
                    console.log(file._id);
                });
                $('.repo-table').append(fileCell);
            })
        }
    })
}

function uploadCode() {
    const file = $('#upload-code').prop('files')[0];
    const iterationId = $('input[name=iterationId]').val();
    console.log(file.name);
    const fd = new FormData();
    fd.append('file', file, file.name);
    fd.append('path', '');
    fd.append('iterationId', iterationId);
    $.api({
        action: 'upload code',
        on: 'now',
        method: 'POST',
        data: fd,
        contentType: false,
        processData: false,
        onSuccess: function (response) {
            console.log(response)
        },
        onFailure: function (xhr) {
            $('.form').form('add errors', [xhr]);
        }
    });
}

$(document).ready(function () {
    loadIterations();
})