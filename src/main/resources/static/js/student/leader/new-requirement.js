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
                if(requirement._id !== requirementId) {
                    values.push({
                        value: requirement._id,
                        name: requirement.name,
                        text: requirement.name
                    })
                }
            });
            $(element).dropdown('change values', values);
            $(element).dropdown('restore default value');
        }
    });
})

$('.form.requirement-form').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'edit requirement',
            urlData: {
                id: requirementId,
            },
            on: 'now',
            method: 'put',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
            },
            onFailure: function (response) {
                $('body').toast({
                    position: 'bottom right',
                    message: response,
                    class: 'red'
                });
            },
            onSuccess: function (logs) {
                $('body').toast({
                    position: 'bottom right',
                    message: 'Work item updated',
                    class: 'green'
                });
                // logs.forEach(function (log) {
                //     const logMenu = $('.log-menu');
                //     const logSegment = $('.log-segment');
                //     const logItem = $(logMenu.children().get(1)).clone();
                //     logItem.on('click', function () {
                //         $('a.item').removeClass('active');
                //         $('.log-table').hide();
                //         $('a.item#' + $(this).attr('id')).addClass('active');
                //         $('#log_' + $(this).attr('id')).show();
                //     })
                //     logItem.prop('id', log._id);
                //     logItem.removeClass('active');
                //     logItem.find('.type-text').text(log.type);
                //     logItem.find('.timestamp').text(moment(log.timeStamp, 'YYYYMMDD_HHmm').toDate().toLocaleString("en-GB").slice(0, -3).replace(",", ""));
                //     logItem.insertAfter($('.log-menu-header'));
                //     const logTable = $(logSegment.children().get(2)).clone().css('display', 'none');
                //     logTable.prop('id', `log_${log._id}`);
                //     logTable.find('.member-name').text(log.student.fullName);
                //     logTable.find('.description-text').text(log.description);
                //     logSegment.append(logTable);
                //     logItem.click();
                // })
            }
        });
    }
});