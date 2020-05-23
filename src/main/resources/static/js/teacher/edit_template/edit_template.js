const templateId = $('input[name="templateId"]').val();

function editTemplateName() {
    $('#template-name').css('display', 'none');
    $('#template-name-edit-button').css('display', 'none');
    $('#template-name-input').css("display", "inline-block");
    $('#template-name-save-button').css('display', 'inline-block');
    $('#template-name-input input').val($('#template-name').html());
}

function saveTemplateName() {
    $('#template-name').css('display', 'inline-block');
    $('#template-name-edit-button').css('display', 'inline-block');
    $('#template-name-input').css('display', 'none');
    $('#template-name-save-button').css('display', 'none');
    $('#template-name').html($('#template-name-input input').val())
}

function addCriteria() {
    var criteriaSegment = new CriteriaSegment();
    $('.segments').append(criteriaSegment.html);
    criteriaSegment.showForm();
}

class CriteriaSegment {
    constructor(id, data) {
        this.id = (id == null) ? 0 : id;
        this.html = $('.template .criteria.segment').clone();
        this.formGroup = this.html.find('.criteria-form');
        this.header = this.html.find('.criteria.header');
        this.input_name = this.html.find('input[name="name"]');
        this.input_weight = this.html.find('input[name="weight"]');
        this.input_description = this.html.find('textarea[name="description"]');
        this.input_levels = this.html.find('table');
        this.btn_save = $(this.html.find('.button.save'));
        this.btn_close = $(this.html.find('.button.close'));
        this.btn_add_level = $(this.html.find('.add-level'));
        this.btn_delete = $(this.html.find('.button.delete'))
        if (data != null) {
            this.setValues(data);
            this.isNew = false;
        } else {
            this.isNew = true;
        }
        this.bindEvents();
    }

    bindEvents() {
        const ref = this;
        this.input_name.keyup(function () {
            ref.header.html($(this).val());
        });
        this.header.click(function () {
            ref.formGroup.transition({
                animation: 'slide down',
                onHide: function () {
                    ref.formGroup.css('display', 'none');
                }
            });
        });
        this.btn_save.click(function () {
            // TODO: validate data
            let url;
            let method;
            if (ref.isNew) {
                method = 'POST';
                url = `/api/grading-template/${templateId}/criteria`;
            } else {
                method = 'PUT';
                url = `/api/grading-template/criteria/${ref.id}`;
            }
            $.ajax({
                method: method,
                url: url,
                data: JSON.stringify(ref.getValues()),
                contentType: 'application/json; charset=utf-8',
                success: function (jqXHR) {
                    alert('criteria saved successfully');
                    ref.id = jqXHR._id;
                    console.log(jqXHR._id);
                },
                error: function () {
                    alert('error saving criteria');
                },
            });
            //console.log(JSON.stringify(ref.getValues()));
//            console.log(ref);
        });
        this.btn_close.click(function () {
            ref.showForm();
        });
        this.btn_add_level.click(function () {
            const newLevel = $('<tr><td><input type="text" placeholder="name"></td><td><input type="number" placeholder="weight"></td></tr>');
            ref.input_levels.find('tbody tr:last-child').before(newLevel);
        });
        this.btn_delete.click(function () {
            console.log(ref);
            if (ref.id !== 0) {
                $.ajax({
                    method: 'DELETE',
                    url: '/api/grading-template/criteria/' + ref.id,
                    success: function () {
//                        alert('criteria deleted successfully');
                        $('body')
                          .toast({
                            class: 'success',
                            message: `criteria deleted successfully!`
                          })
                        ;
                    },
                    error: function () {
//                        alert('error deleting criteria');
                        $('body')
                          .toast({
                            class: 'error',
                            message: `error deleting criteria!`
                          })
                        ;
                    }
                });
            } else {
                $('body')
                  .toast({
                    class: 'success',
                    message: `criteria deleted successfully!`
                  })
                ;
            }
            ref.html.remove();
        });
    }

    setValues(data) {
        this.header.html(data['name']);
        this.input_name.val(data['name']);
        this.input_weight.val(data['weight']);
        this.input_description.val(data['description']);
        const lastLevel = this.input_levels.find('tbody tr:last-child');
        $.each(data['levels'], function (index, level) {
            const newLevel = $(`<tr><td><input type="text" placeholder="name" value="${level.levelName}"></td><td><input type="number" placeholder="weight" value="${level.weight}"></td></tr>`);
            lastLevel.before(newLevel);
        });
    };

    getValues() {
        const levels = [];
        $.each(this.input_levels.find('tr:not(:last-child)'), function (index, el) {
            const inputs = $(el).find('input');
            levels.push({
                'levelName': $(inputs[0]).val(),
                'weight': $(inputs[1]).val()
            });
        });
        return {
            'name': this.input_name.val(),
            'weight': this.input_weight.val(),
            'description': this.input_description.val(),
            'levels': levels
        }
    }

    showForm() {
        this.header.click();
    }
}

$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `/api/grading-template/${templateId}/criteria`,
        success: function (jqXHR) {
            console.log(jqXHR);
            $.each(jqXHR, function (index, data) {
                var criteriaSegment = new CriteriaSegment(data._id, data);
                console.log(criteriaSegment);
                $('.segments').append(criteriaSegment.html);
            })
        },
        error: function (jqXHR) {
            alert(jqXHR);
        },
    })
});