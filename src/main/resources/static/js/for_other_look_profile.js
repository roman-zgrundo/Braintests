const Buttun2 = document.getElementById('button2')
const count = document.getElementById("count")
const url = window.location.href;

const segments = url.split('/');

if (segments.length === 7) {
    var id = segments[segments.length - 2];
}
if (segments.length === 6) {
    var id = segments[segments.length - 1];
}

function ctrlButton2() {
    Buttun2.disabled = this.value.trim().length === 0;
}

count.addEventListener('input', ctrlButton2, false);
ctrlButton2.call(count);

Buttun2.onclick = function (){
    let count = document.getElementById("count").value
    if (count < 1) {
        count = 3;
    }
    document.location.href = `/accounts/profile/${id}/${count}`;
}