$.fn.api.settings.api = {
    'create requirement': '/api/requirement?name={name}',
    'get requirements': '/api/requirement/group/{groupId}'
};

var groupId = $('input[name="groupId"]').val();

$(document).ready(function () {
    $.api({
        action: 'get requirements',
        on: 'now',
        method: 'get',
        urlData: {
            groupId: groupId
        },
        onSuccess: function (jqXHR) {
            loadRequirements(jqXHR);
        },
        onFailure: function (res) {
            alert(res);
        }
    });

    $('.form.create-requirement').form({
        onSuccess: function (evt, data) {
            $.api({
                action: 'create requirement',
                on: 'now',
                method: 'post',
                urlData: {
                    name: data.title
                },
                onSuccess: function (res) {
                    window.location.href = "/student/requirement/" + res._id;
                },
                onFailure: function (response) {
                    $('.form.create-class').form('add errors', [response]);
                },
            });
        },
        fields: {
            title: {
                identifier: 'title',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Title must not be empty!'
                    }
                ]
            },
        }
    });
});

function loadRequirements(res) {
    var reqList = $('.req-list');
    var hasParents = [];
    var numbering = 1;
    $.each(res.data, function (i, r) {
        if (r.parentReq != null) {
            hasParents.push(r);
            return;
        }
        var segment = $('#templates .explorer-item').clone();
        segment.attr('data-id', r._id);
        segment.attr('data-type', r.type);
        segment.attr('data-numbering', numbering);
        segment.find('.numbering').text(numbering + ".");
        segment.find('.title').text(r.name);
        segment.find('a.header').click(function () {
            showRequirementInfo(r, segment.find('.numbering').text());
            segment.find('.ui.list').transition({
                animation: 'slide down',
                onHide: function () {
                    segment.find('.ui.list').css('display', 'none');
                }
            });
        });
        reqList.append(segment);
        numbering++;
    });

    $.each(hasParents, function (i, r) {
        var parentId = r.parentReq._id;
        var parentSegment = $(`.explorer-item[data-id="${parentId}"]`);
        var segment = $('#templates .explorer-sub-item').clone();
        segment.attr('data-id', r._id);
        segment.attr('data-type', r.type);
        segment.find('.title').text(r.name);
        segment.find('.sub-numbering').text(parentSegment.find('.numbering').text() + (parentSegment.find('.explorer-sub-item').length + 1));
        segment.find('a.header').click(function () {
            showRequirementInfo(r, segment.find('.sub-numbering').text());
        });
        parentSegment.find('.ui.list').append(segment);
    });

    $(reqList.find('.explorer-item')[0]).click();
}

function showRequirementInfo(req, index) {
    var segment = $('#reqInfo');
    segment.find('.req-index').text(index);
    segment.find('.req-title').text(req.name);
    segment.find('.edit-link').attr('href', '/student/requirement/' + req._id);
    segment.find('.date-created').text("9/7/2020");
    segment.find('.created-by').text("Võ Lê Minh Tâm");
    if (req.parentReq != null) {
        segment.find('.parent-req').attr('href', '/student/requirement/' + req.parentReq._id).text(req.parentReq.name);
    }
    segment.find('.req-description').text(req.description);
}

$('input[type="radio"]').change(function () {
    filter($(this).attr('data-type'));
});

function filter(value) {
    var reqList = $('.req-list');
    if (value == 'none') {
        reqList.find('.explorer-item, .explorer-sub-item').show();
    } else if (value == 'functional') {
        reqList.find('.explorer-item[data-type="functional"], .explorer-sub-item[data-type="functional"]').show();
        reqList.find('.explorer-item[data-type="non-functional"], .explorer-sub-item[data-type="non-functional"]').hide();
    } else {
        reqList.find('.explorer-item[data-type="functional"], .explorer-sub-item[data-type="functional"]').hide();
        reqList.find('.explorer-item[data-type="non-functional"], .explorer-sub-item[data-type="non-functional"]').show();
    }
}

function transition() {

}