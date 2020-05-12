const classId = $("input[name='classId']").val();

$('.form.group-setting').form({
    onSuccess: function (evt, data) {
        correctFormData('.form.group-setting', data);
        $.api({
            action: 'group setting',
            on: 'now',
            method: 'post',
            urlData: {
                classId: classId
            },
            data: {
                groupMin: data.groupMin,
                groupMax: data.groupMax,
                deadline: data.deadline
            },
            onSuccess: function () {
                $('body').toast({
                    position: 'bottom right',
                    message: 'Group setting saved',
                    class: 'green'
                });
            },
            onFailure: function (response) {
                if (response.error !== undefined) {
                    $('.form.iteration-setting').form('add errors', [response.error]);
                } else {
                    $('.form.group-setting').form('add errors', [response.message]);
                }
            }
        })
    },
    fields: {
        groupMin: validationRules.groupMin,
        groupMax: validationRules.groupMax
    }
})

$('.form.iteration-setting').form({
    onSuccess: function (evt, data) {
        correctFormData('.form.iteration-setting', data);
        data['classId'] = classId;
        $.api({
            action: 'iteration setting',
            on: 'now',
            method: 'post',
            urlData: {
                classId: classId
            },
            data: {
                defaultLength: data.defaultLength,
                maxLength: data.maxLength,
                deadline: data.deadline
            },
            onSuccess: function () {
                $('body').toast({
                    position: 'bottom right',
                    message: 'Iteration setting saved',
                    class: 'green'
                });
            },
            onFailure: function (response) {
                if (response.error !== undefined) {
                    $('.form.iteration-setting').form('add errors', [response.error]);
                } else {
                    $('.form.iteration-setting').form('add errors', [response.message]);
                }
            }
        })
    },
    fields: {
        defaultLength: validationRules.defaultLength,
        maxLength: validationRules.maxLength
    }
})

function initForms() {
    $.api({
        action: 'get class config',
        on: 'now',
        method: 'get',
        urlData: {
            classId: classId
        },
        onSuccess: function (classConfig) {
            $('.form.group-setting').form('set values', {
                groupMin: classConfig.groupLimitMin,
                groupMax: classConfig.groupLimitMax,
                deadline: classConfig.groupRegistrationDeadline
            });
            $('.form.iteration-setting').form('set values', {
                defaultLength: classConfig.defaultIterationLength,
                maxLength: classConfig.maxIterationLength,
                deadline: classConfig.iterationSetupDeadline
            })
        },
        onFailure: function (response) {
            $('.form.iteration-setting').form('add errors', [response.error]);
        }
    })
}

function getClassDisciplineConfig() {
    $.api({
        action: 'get class disciplines config',
        on: 'now',
        method: 'get',
        urlData: {
            classId: classId
        },
        onSuccess: function (response, element, xhr) {
            xhr.responseJSON.data.forEach(function (discipline) {
                const disciplineCell = $('#templates .discipline-cell').clone();

                $('.discipline-table').append(disciplineCell);
            })
        }
    })
}

$(document).ready(function () {
    // const today = new Date();
    $('.form.group-setting .deadline-picker').calendar({
        // minDate: new Date(today.getFullYear(), today.getMonth(), today.getDate()),
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        }
    });
    $('.form.iteration-setting .deadline-picker').calendar({
        // minDate: new Date(today.getFullYear(), today.getMonth(), today.getDate()),
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';
                const day = ('0' + date.getDate()).slice(-2);
                const month = ('0' + (date.getMonth() + 1)).slice(-2);
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }
        }
    });
    initForms();
});