$('.pdf-viewer .popup-close').click(function (e) {
    $('.pdf-viewer').fadeOut();
});

var toggler = document.getElementsByClassName("caret");
for (var i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener("click", function () {
        this.parentElement.querySelector(".nested").classList.toggle("active");
        this.classList.toggle("caret-down");
    });
}

$('.repo-tree li span').hover(function (e) {
    $(this).addClass('hovered-node');
}).mouseleave(function (e) {
    $(this).removeClass('hovered-node');
});

// $(".pdf-viewer .my-viewport").kendoPDFViewer({
//     pdfjsProcessing: {
//         file: {
//             url: "http://www.africau.edu/images/default/sample.pdf"
//         }
//     },
//     width: "100%",
//     height: "100%"
// });