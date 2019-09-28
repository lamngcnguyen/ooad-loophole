var password = document.getElementById('password');
var repassword = document.getElementById('retype-password');
function check_pass() {
    if (document.getElementById('password').value != document.getElementById('retype-password').value) {
        document.getElementById('retype-password').setCustomValidity("Passwords Don't Match");
        // document.getElementById('submit').disabled = true;

    } else {
        // document.getElementById('submit').disabled = false;
        document.getElementById('retype-password').setCustomValidity("");
    }
}

function check_change_pass() {
    var oldpassword = document.getElementById("oldpassword");
    var newpassword = document.getElementById("password");
    if (oldpassword.value == newpassword.value) {
        newpassword.setCustomValidity("New Password must be different from Old Password!");
        // document.getElementById('submit').disabled = true;

    } else {
        // document.getElementById('submit').disabled = false;
        newpassword.setCustomValidity('');
    }
}
function Validate() {
    var password1 = document.getElementById("password");
    var repassword1 = document.getElementById("retype-password");
    if (password1.value !== repassword1.value) {
        repassword1.setCustomValidity("Passwords Don't Match");
    }else{
        repassword1.setCustomValidity('');
        $.post("/signup", document.querySelector('#fullname').value.trim(), document.querySelector('#email').value.trim(), document.querySelector('#password').value.trim()
            , function (data) {
            });
    }
}
