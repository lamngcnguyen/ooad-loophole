function showTab(tabName) {
    $('.menu .item').removeClass('blue active');
    $('.tab').removeClass('active');
    $(`.menu .item.${tabName}`).addClass('blue active');
    $(`.tab.${tabName}`).addClass('active');
}
