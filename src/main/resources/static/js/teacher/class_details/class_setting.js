function showTab(tabName) {
    $('a.item').removeClass('active');
    $('.tab').removeClass('active');
    $('a.item.' + tabName).addClass('active');
    $('.tab.' + tabName).addClass('active');
}

var classId = $("input[name='classId']").val();