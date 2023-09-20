const wrapper = document.querySelector('.wrapper');
let activeItem = wrapper.querySelectorAll('.active')
let arr = [];
let minutes = 0;
let seconds = 0;
let index = 0;
let mistake = 0;
let size = 5;
let now = '';

const popUp = document.querySelector('.popUp')
const timeMinutes = document.querySelector('.time-minutes')
const popUpMinutes = document.querySelector('.popUp-minutes')
const popUpMistake = document.querySelector('.popUp-mistake')
const popUpSeconds = document.querySelector('.popUp-seconds')
const timeSeconds = document.querySelector('.time-seconds')
const BtnBack = document.querySelector('.btn_back')
const spanTimer = document.querySelector('.span-timer')
spanTimer.classList.add('start')
const idUser = document.getElementById('id_user')



const RangeSize = document.getElementById('customRange3')
const label = document.querySelector('.form-label ')
const RangeResult = document.getElementById('range_result')

const btn = document.getElementById('btn_start')



RangeResult.innerText = 'Выбран масштаб ' + RangeSize.value + ' на ' + RangeSize.value;
size = RangeSize.value;
wrapper.style.gridTemplateColumns = `${'1fr '.repeat(size)}`;

    RangeSize.addEventListener('input', function () {
    RangeResult.innerText = 'Выбран масштаб ' + RangeSize.value + ' на ' + RangeSize.value;
    size = RangeSize.value;
    wrapper.style.gridTemplateColumns = `${'1fr '.repeat(size)}`;
    // console.log(RangeSize.value);
})

btn.addEventListener('click', function () {


    spanTimer.classList.remove('start')
    RangeResult.classList.add('start')
    btn.style.display = 'none';
    RangeSize.classList.add('start')
    label.classList.add('start')


    for (let i = 0; i < size**2; i++) {
        arr.push(i+1)
    }

    let newArr = arr.sort(() => Math.random()-0.5);
    newArr.forEach((el) => {
        wrapper.innerHTML += `<div class="item">${el}</div>`
    });

    function time() {
        seconds = seconds + 1;
        if (seconds == 60) {
            seconds = 0;
            minutes = minutes + 1
            timeSeconds.innerHTML = '0' + seconds;
        } else if (seconds < 10) {
            timeSeconds.innerHTML = '0' + seconds;
        } else if (seconds >= 10) {
            timeSeconds.innerHTML = seconds;
        }
        timeMinutes.innerHTML = minutes;
    }
    setInterval(time, 1000)
});

wrapper.addEventListener('click', function(e) {
    target = event.target
    if (target.textContent == index+1) {
        target.classList.add('active');
        index++;
    } else {
        mistake++
    }
    // console.log(index);
    if (index == size**2) {
        popUp.classList.add('active')
        popUpSeconds.innerHTML = seconds;
        popUpMinutes.innerHTML = minutes;
        popUpMistake.innerHTML = `Ошибок: ${mistake}`;
        // console.log('win');
        // console.log('mistake:',mistake);


        let id_user = idUser.innerText;

        // console.log(id_user);
        // console.log(size);
        // console.log(mistake);
        // console.log(minutes*60+seconds);

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "http://45.90.46.161:8088/schultetable");
        // xhr.open("POST", "http://localhost:8088/schultetable");
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
          "size": "${size}",
          "mistake": ${mistake},
          "time": ${minutes*60+seconds}
        }`;

        xhr.send(data);

    }
})


