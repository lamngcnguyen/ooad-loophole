$('.form.add-item').form({
    onSuccess: function (evt, data) {
        $.api({
            action: 'create item',
            on: 'now',
            method: 'post',
            data: {name: data.name},
            onFailure: function (response) {
                $('body').toast({
                    message: response,
                    class: 'red'
                });
            },
            onSuccess: function (item) {
                const newItemCards = $('.new-item.cards');
                const card = `<a class="workItem card" href="/student/work-item/${item._id}">` +
                    '   <div class="content">' +
                    '       <div class="header">' +
                    '           <i class="teal tasks icon"></i>' +
                    '           <span class="workItem-auto-increment">1</span>' +
                    `           <span class="workItem-title" style="font-weight: normal"> ${item.name}</span>` +
                    '       </div>' +
                    '       <div class="description" style="margin-top: 5px">' +
                    '           <img class="ui workItem avatar image" src="/images/default.png"/>' +
                    '           <span class="workItem user">Unassigned</span>' +
                    '       </div>' +
                    '   </div>' +
                    '   <div class="extra content">' +
                    '       Status:<span class="workItem-status" style="float: right">' +
                    '           <i class="grey circle icon"></i>New</span>' +
                    '   </div>' +
                    '</a>';
                newItemCards.append(card);
                $('input[name="name"]').val('')
                $('body').toast({
                    message: `Work item "${item.name}" created`,
                    class: 'green'
                });
            }
        })
    },
});