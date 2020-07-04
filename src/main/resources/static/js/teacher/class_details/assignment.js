$('.modal.create-assignment').modal({
    onShow: function () {
        $('.modal.create-assignment .deadline-picker').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            }
        });
    }
});

$('.modal.edit-assignment').modal({
    onShow: function () {
        $('.modal.edit-assignment .deadline-picker').calendar({
            type: 'date',
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    const day = ('0' + date.getDate()).slice(-2);
                    const month = ('0' + (date.getMonth() + 1)).slice(-2);
                    const year = date.getFullYear();
                    return day + '/' + month + '/' + year;
                }
            }
        });
    }
});

$('.form.create-assignment').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.create-assignment');
        correctFormData('.form.create-assignment', data);
        data['classId'] = classId;
        $.api({
            action: 'create assignment',
            on: 'now',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;char=utf-8');
            },
            onSuccess: function (assignment) {
                hideDimmer('.modal.create-assignment');
                hideModal('.modal.create-assignment');
                const assignmentMenu = $('.assignment-menu');
                if (assignmentMenu.children().length === 0) {
                    loadAssignments();
                } else {
                    const assignmentItem = $(`<a class="item assignment-item" id="${assignment._id}">${assignment.name}</a>`)
                        .click(function () {
                            $('.assignment-menu').children().removeClass("blue active");
                            $(this).addClass('blue active');
                            loadAssignment(assignment._id);
                        });
                    assignmentMenu.append(assignmentItem);
                }
            },
            onFailure: function (response) {
                hideDimmer('.modal.create-assignment');
                $('.form.create-assignment').form('add errors', [response]);
            }
        })
    }
});

$('.form.edit-assignment').form({
    onSuccess: function (evt, data) {
        showDimmer('.form.edit-assignment');
        correctFormData('.form.edit-assignment', data);
        data['classId'] = classId;
        $.api({
            action: 'edit assignment',
            on: 'now',
            method: 'put',
            urlData: {
                id: data.id
            },
            dataType: 'json',
            data: JSON.stringify(data),
            beforeXHR: (xhr) => {
                xhr.setRequestHeader('Content-Type', 'application/json;char=utf-8');
            },
            onSuccess: function (assignment) {
                hideDimmer('.modal.edit-assignment');
                hideModal('.modal.edit-assignment');
                $(`#${assignment._id}`).text(assignment.name);
                loadAssignment(assignment._id)
            },
            onFailure: function (response) {
                hideDimmer('.modal.edit-assignment');
                $('.form.edit-assignment').form('add errors', [response]);
            }
        })
    }
});

$('.form.delete-assignment').form({
    onSuccess: function (evt, data) {
        const id = data.id;
        showDimmer('.form.delete-assignment');
        $.api({
            action: 'delete assignment',
            on: 'now',
            method: 'delete',
            urlData: {
                id: id
            },
            onSuccess: function () {
                const assignmentMenu = $('.assignment-menu');
                hideDimmer('.modal.delete-assignment');
                hideModal('.modal.delete-assignment');
                $(`#${id}`).remove();
                if (assignmentMenu.children().length === 0) {
                    $('.no-assignment.row').show();
                    $('.assignment.row').hide();
                } else {
                    assignmentMenu.children().get(0).click();
                }
            },
            onFailure: function (response) {
                hideDimmer('.modal.delete-assignment');
                $('.form.delete-assignment').form('add errors', [response]);
            }
        })
    }
});

$(document).ready(function () {
    loadAssignments();
});

$('.dropdown.grading-template-dropdown').dropdown({
    showOnFocus: false,
}).api({
    action: 'get grading templates',
    method: 'get',
    urlData: {teacherId: $('input[name="teacherId"]').val()},
    on: 'now',
    onSuccess(response, element, xhr) {
        const values = [];
        xhr.responseJSON.data.forEach(function (gradingTemplate) {
            values.push({
                value: gradingTemplate._id,
                name: gradingTemplate.gradingTemplateName,
                text: gradingTemplate.gradingTemplateName,
            })
        });
        $(element).dropdown('change values', values);
    }
});

function loadAssignments() {
    $.api({
        action: 'get assignments',
        on: 'now',
        method: 'get',
        urlData: {
            classId: classId,
        },
        onSuccess: function (res, element, xhr) {
            const assignmentMenu = $('.assignment-menu');
            if (xhr.responseJSON.data.length === 0) {
                $('.no-assignment.row').show();
                $('.assignment.row').hide();
            } else {
                $('.no-assignment.row').hide();
                $('.assignment.row').show();
                xhr.responseJSON.data.forEach(function (assignment) {
                    const assignmentItem = $(`<a class="item assignment-item" id="${assignment._id}">${assignment.name}</a>`)
                        .click(function () {
                            $('.assignment-menu').children().removeClass("active");
                            $(this).addClass('active');
                            loadAssignment(assignment._id);
                        });
                    assignmentMenu.append(assignmentItem);
                    const firstElement = assignmentMenu.children().get(0);
                    firstElement.click();
                    $(firstElement).addClass('active');
                });
            }
        },
        onFailure: function (res) {
            console.log(res);
        },
    });
}

function loadAssignment(id) {
    $.api({
        action: 'get assignment',
        on: 'now',
        method: 'get',
        urlData: {
            id: id,
        },
        onSuccess: function (res, element, xhr) {
            const deadline = new Date(res.deadline);
            $('.segment.assignment .assignment-name').text(res.name);
            $('.segment.assignment .deadline').text(deadline.toLocaleDateString('en-GB'));
            $('.segment.assignment .detail-text').text(res.description);
            // if (new Date() <= deadline) {
            //     $('.segment.assignment .show-results').addClass('disabled');
            // } else {
            //     $('.segment.assignment .show-results').removeClass('disabled');
            //     $('.segment.assignment .show-results').prop('href', `/teacher/assignment/${res._id}/results`);
            // }
            $('.segment.assignment .show-results').prop('href', `/teacher/assignment/${res._id}/results`);
            if (res.gradingTemplateId != null || res.gradingTemplateId !== '') {
                $.ajax({
                    url: '/api/grading-template/' + res.gradingTemplateId,
                    method: 'get',
                    success: function (jqXHR) {
                        $('.segment.assignment .template-name').text(jqXHR.gradingTemplateName);
                    },
                    error: function () {
                        alert('unable to get grading template info');
                    }
                });
            } else {
                $('.segment.assignment .template-name').text("not chosen");
            }
            $('.form.edit-assignment').form('set values', {
                id: res._id,
                name: res.name,
                deadline: res.deadline,
                description: res.description,
                gradingTemplateId: res.gradingTemplateId
            });
            $('.form.delete-assignment').form('set values', {
                id: res._id
            })
        },
        onFailure: function (res) {
            console.log(res);
        },
    });
}