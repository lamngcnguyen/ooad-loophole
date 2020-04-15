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
                const assignmentItem = $(`<a class="item assignment-item" id="${assignment._id}">${assignment.name}</a>`)
                    .click(function () {
                        $('.assignment-menu').children().removeClass("blue active");
                        $(this).addClass('blue active');
                        loadAssignment(assignment._id);
                    });
                assignmentMenu.append(assignmentItem);
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
                $('.form.create-assignment').form('add errors', [response]);
            }
        })
    }
});

$(document).ready(function () {
    loadAssignments();
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
            xhr.responseJSON.data.forEach(function (assignment) {
                const assignmentItem = $(`<a class="item assignment-item" id="${assignment._id}">${assignment.name}</a>`)
                    .click(function () {
                        $('.assignment-menu').children().removeClass("blue active");
                        $(this).addClass('blue active');
                        loadAssignment(assignment._id);
                    });
                assignmentMenu.append(assignmentItem);
                const firstElement = assignmentMenu.children().get(0);
                firstElement.click();
                $(firstElement).addClass('blue active');
            });
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
            $('.segment.assignment .assignment-name').text(res.name);
            $('.segment.assignment .deadline').text(new Date(res.deadline).toLocaleDateString('en-GB'));
            $('.segment.assignment .detail-text').text(res.description);
            $('.form.edit-assignment').form('set values', {
                id: res._id,
                name: res.name,
                deadline: res.deadline,
                description: res.description
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