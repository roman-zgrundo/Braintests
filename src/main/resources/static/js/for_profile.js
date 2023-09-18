const Buttun1 = document.getElementById('button1')
const count = document.getElementById("count")

function ctrlButton() {
    Buttun1.disabled = this.value.trim().length === 0;
}

count.addEventListener('input', ctrlButton, false);
ctrlButton.call(count);

Buttun1.onclick = function (){
    let count = document.getElementById("count").value
    if (count < 1) {
        count = 3;
    }
    document.location.href = `/profile/${count}`;
}