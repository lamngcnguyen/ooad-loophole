const btnNewRequirement = $('#newRequirement');
const newRequirementItem = $('.item.new');
const segmentContainer = $('#segmentContainer');
const navMenu = $('.ui.large.bulleted.relaxed.list');

let reqData = []

function showRequirement(id) {
    if (!$('#iteration_' + id).is(':visible')) {
        showRequirementDetails(id)
    }
}

function remove(id) {
    if (!window.confirm('Are you sure to delete this one?')) return console.log('NOT DELETE')
    $.api({
        action: 'delete requirement',
        on: 'now',
        method: 'delete',
        dataType: 'json',
        url: '/api/requirement/' + id,
        beforeXHR: (xhr) => {
            xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
        },
        onFailure: function (response) {
            $(form).form('add errors', [response])
            alert(response)
        },
        success: function () {
            window.location.reload()
        },
        onSuccess: function (response) {
            window.location.reload()
        }
    })
}

function renderOne(item) {
    return `<div class="ui segment" id="segmentContainer">
        <div class="ui grid">
            <div class="row">
                <div class="column">
                    <div class="ui header"><i class="blue file alternate icon"></i>${item.name}
                    </div>
                    <div class="ui divider"></div>
                    <div class="ui segment" style="padding-top: 30px">
                        <a class="ui top attached icon grey label">
                            <i class="check icon"></i> Description
                        </a>
                        <span>${item.description || ''}</span>
                    </div>
                    <div class="ui divider"></div>
                    <div>
                    <div class="ui negative button" onclick="remove('${item._id}')">Delete</i></div>
                    </div>
<!--                    <div class="ui header">-->
<!--                        <i class="blue copy icon"></i>Attachments-->
<!--                    </div>-->
<!--                    <div class="ui tiny icon labeled green button"><i class="upload icon"></i>Upload-->
<!--                    </div>-->
<!--                    <div class="ui bulleted relaxed list">-->
<!--                        <a class="item">-->
<!--                            spec.docx-->
<!--                        </a>-->
<!--                        <a class="item">-->
<!--                            spec2.docx-->
<!--                        </a>-->
<!--                    </div>-->
<!--                    <div class="ui divider"></div>-->
<!--                    <div class="ui header"><i class="blue linkify icon"></i>Work items</div>-->
<!--                    <div class="ui grid">-->
<!--                        <div class="row">-->
<!--                            <div class="column">-->
<!--                                <div class="ui raised cards">-->
<!--                                    <a class="card">-->
<!--                                        <div class="content">-->
<!--                                            <div class="header">Work item #1</div>-->
<!--                                            <div class="description">-->
<!--                                                Assigned to:-->
<!--                                                <div class="ui avatar image">-->
<!--                                                    <img th:src="@{/images/default.png}">-->
<!--                                                </div>-->
<!--                                                Vo Le Minh Tam-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="extra content">-->
<!--                                            State:<span style="float: right">Approved</span>-->
<!--                                        </div>-->
<!--                                    </a>-->
<!--                                    <a class="card">-->
<!--                                        <div class="content">-->
<!--                                            <div class="header">Work item #2</div>-->
<!--                                            <div class="description">-->
<!--                                                Assigned to:-->
<!--                                                <div class="ui avatar image">-->
<!--                                                    <img th:src="@{/images/default.png}">-->
<!--                                                </div>-->
<!--                                                Vo Le Minh Tam-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="extra content">-->
<!--                                            State:<span style="float: right">Approved</span>-->
<!--                                        </div>-->
<!--                                    </a>-->
<!--                                </div>-->
<!--                            </div>-->
<!--                        </div>-->
<!--                    </div>-->
<!--                    <div class="ui divider"></div>-->
<!--                    <div class="ui header"><i class="blue clock icon"></i>Change history</div>-->
<!--                    <div class="ui grid">-->
<!--                        <div class="two column row">-->
<!--                            <div class="six wide column">-->
<!--                                <div class="ui segment">-->
<!--                                    <div class="ui vertical fluid tabular menu">-->
<!--                                        <div class="item">-->
<!--                                            <div class="content">-->
<!--                                                <div class="header">Change log 22/1</div>-->
<!--                                                <div class="description">-->
<!--                                                    Updated by: <img class="ui avatar image"-->
<!--                                                                     th:src="@{/images/default.png}">-->
<!--                                                    Vo Le Minh Tam-->
<!--                                                </div>-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                        <div class="active item">-->
<!--                                            <div class="content">-->
<!--                                                <div class="header">Change log 23/1</div>-->
<!--                                                <div class="description">-->
<!--                                                    Updated by: <img class="ui avatar image"-->
<!--                                                                     th:src="@{/images/default.png}">-->
<!--                                                    Vo Le Minh Tam-->
<!--                                                </div>-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </div>-->
<!--                            <div class="ten wide column">-->
<!--                                <div class="ui segment full height">-->
<!--                                    <div class="ui grid">-->
<!--                                        <div class="row">-->
<!--                                            <div class="column">-->
<!--                                                <div class="ui header">Change log 22/1</div>-->
<!--                                                <div class="ui divider"></div>-->
<!--                                                <div>-->
<!--                                                    <span style="font-weight: bold">Title: </span><span-->
<!--                                                        class="requirement title">something</span>-->
<!--                                                </div>-->
<!--                                                <p><span-->
<!--                                                        style="font-weight: bold">Description: </span>As-->
<!--                                                    visitor I get the most-searched hotels in the-->
<!--                                                    neighborhood right away, while other hotels are-->
<!--                                                    loading in the background, so I more quickly get-->
<!--                                                    results;</p>-->
<!--                                            </div>-->
<!--                                        </div>-->
<!--                                    </div>-->
<!--                                </div>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>`
}

function getRequirement(id) {
    $.api({
        action: 'get requirement',
        on: 'now',
        method: 'get',
        url: '/api/requirement/' + id,
        onSuccess: function (res, element, xhr) {
            const str = renderOne(xhr.responseJSON)
            $('#reqContainer').html(str)
        }
    })
}

function showRequirementDetails(id) {
    getRequirement(id);
    $('.item').removeClass('active');
    $(`#item_${id}`).addClass('active');
    $('.segment.requirement').hide();
    $(`#requirement_${id}`).show();
}

btnNewRequirement.click(function () {
    let options = reqData.reduce((result, item) => result + `<option value="${item._id}">${item.name}</option>`)
    $(this).hide();
    $('#nameInput').show();
    $('#nameInput input').focus();
    $('#segmentContainer').html(`<div id="templates">
        <form id="new-form">
        <div class="ui requirement form" id="new-requirement">
            <div class="ui form">
                <div class="ten wide required field">
                    <label>Title</label>
                    <input type="text" name="name" placeholder="Title">
                </div>
                <div class="required field">
                    <label>Description</label>
                    <textarea name="description" placeholder="Description"></textarea>
                </div>
                <div class="required field">
                    <label>Parent</label>
                    <select class="ui dropdown" name="parent">
                    <option value="">Select one</option>
                    ${options}
                    </div>
                    </select>
            </div>
                <div class="ui blue icon labeled submit button" onClick="createRequirementForm()"><i class="save icon"></i>
                    Save
                </div>
                <div class="ui red icon labeled button right floated" onclick="cancelRequirementForm()"><i
                        class="trash icon"></i>
                    Cancel
                </div></div></form>
        </div>`)

});

$('#new-requirement').on('keydown', function (e) {
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
    const form = $('#templates .requirement.form').clone();
    const data = $('#new-form').serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    if (data.parent) data.parentReq = reqData.find((i) => i._id === data.parent)
    $.api({
        action: 'create requirement',
        on: 'now',
        method: 'post',
        dataType: 'json',
        url: '/api/requirement',
        data: JSON.stringify(data),
        beforeXHR: (xhr) => {
            xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8')
        },
        onFailure: function (response) {
            $(form).form('add errors', [response])
            alert(response)
        },
        onSuccess: function (response) {
            window.location.reload()
        }
    })
}

function cancelRequirementForm() {
    $('#requirement_0').remove();
    $('#item_0').remove();
    btnNewRequirement.show();
    $('#nameInput').hide();
    $('#nameInput input').val('');
    newRequirementItem.show();
    $('#segmentContainer .ui.grid').show();
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
        showRequirement(data._id);
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

function renderItem(item, level) {
    let children = ""
    const style = level === 0 ? "margin-bottom: 1rem" : `padding-left: 2rem`

    item.children.forEach(itm => children += renderItem(itm, level + 1))
    return `<li style="${style}">
        <div class="dot"></div>
        <span onClick="showRequirement('${item.data._id}')">${item.data.name}</span>
        <ul>${children}</ul>
    </li>`
}

function formatItem(cur, reqs) {
    let children = []
    reqs.forEach(function (req) {
        if (req.parentReq && req.parentReq._id === cur._id) {
            children.push(formatItem(req, reqs))
        }
    })

    return {
        data: cur,
        children,
    }
}

function loadIterations() {
    $.api({
        action: 'get requirement',
        on: 'now',
        method: 'get',
        url: '/api/requirement/all',
        onSuccess: function (res, element, xhr) {
            let data = []
            reqData = xhr.responseJSON
            xhr.responseJSON.forEach(function (req, i) {
                if (req.level === 0) data.push(formatItem(req, xhr.responseJSON))
            });
            let str = ""
            data.forEach(function (item) {
                str += renderItem(item, 0)
            })
            str = `<ul>${str}</ul>`
            navMenu.append(str)

            showRequirementDetails(xhr.responseJSON[0]._id);
        }
    })
}

$(document).ready(function () {
    loadIterations();
})