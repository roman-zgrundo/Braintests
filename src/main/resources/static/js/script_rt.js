const stringWrapper = document.querySelector('.string-wrapper')
const btn = document.querySelector('.btn_start')
const words = stringWrapper.getElementsByTagName('span')
const time = document.getElementById('customRange3')
const label = document.querySelector('.form-label ')
const mainText = document.getElementById('main_text')
const nameText = document.getElementById('name_text')
const idText = document.getElementById('id_text')
const worldCount = document.getElementById('world_count')
const idUser = document.getElementById('id_user')
const RangeResult = document.getElementById('range_result')
const btnExit = document.getElementById('btn_exit')
const q1 = document.getElementById('q1')
const q2 = document.getElementById('q2')
const q3 = document.getElementById('q3')
const q4 = document.getElementById('q4')
const q5 = document.getElementById('q5')

let i = 0;
let b = 3000;
let a = 1000;
let now = '';
let str = mainText.innerText;

q1.classList.add('active')
q2.classList.add('active')
q3.classList.add('active')
q4.classList.add('active')
q5.classList.add('active')

RangeResult.innerText = 'Выбрана скорость ' + time.value + ' сек.';
// console.log(time.value);

time.addEventListener('input', function () {
    RangeResult.innerText = 'Выбрана скорость ' + time.value + ' сек.';
    // console.log(time.value);

})

btn.addEventListener('click', function () {
    // console.log(mainText.innerText);
    i=0;
    stringWrapper.innerHTML ="";
    let arr = str.split(' ')
    RangeResult.classList.add('active')
    btn.classList.add('active')
    time.classList.add('active')
    label.classList.add('active')
    btnExit.classList.add('active')
    q1.classList.add('active')
    q2.classList.add('active')
    q3.classList.add('active')
    q4.classList.add('active')
    q5.classList.add('active')
    arr.forEach(el => {
        stringWrapper.innerHTML += `<span>${el} </span>`
    });
    addOpacity()

});

function addOpacity() {
    setTimeout(function () {
        words[i].classList.add('active')
        i++;
        if (i < words.length) {
            addOpacity();
        } else {
            setTimeout(removeClass, b)
        }
    }, a)
}

function removeClass() {
    stringWrapper.innerHTML = ''
    // chooseText.classList.remove('active')
    RangeResult.classList.remove('active')
    btn.classList.remove('active')
    time.classList.remove('active')
    label.classList.remove('active')
    btnExit.classList.remove('active')
    q1.classList.remove('active')
    q2.classList.remove('active')
    q3.classList.remove('active')
    q4.classList.remove('active')
    q5.classList.remove('active')

    let  qwe = 100;
    let id_user = idUser.innerText;
    let world_count = worldCount.innerText;
    let res_time = time.value;
    let name_text = nameText.innerText;


    // console.log(name_text);
    // console.log(id_user);
    // console.log(world_count);
    // console.log(res_time);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "http://78.110.62.84:8088/runningtext");
    // xhr.open("POST", "http://localhost:8088/runningtext");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            console.log(xhr.status);
            console.log(xhr.responseText);
        }};

    let options = {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        timezone: 'UTC',
        hour: 'numeric',
        minute: 'numeric'
    };
    now = new Date().toLocaleString("ru", options);

    let data = `{
      "user_id": ${id_user},
      "actionDate": "${now}",
      "name_text": "${name_text}",
      "world_count": ${world_count},
      "time": ${world_count*res_time}
}`;
    xhr.send(data);
}

time.addEventListener('input', function () {
    a = time.value * 1000
})
