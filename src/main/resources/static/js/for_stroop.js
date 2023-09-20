// const wrapper = document.querySelector('.wrapper');
// let activeItem = wrapper.querySelectorAll('.active')
// let size =3;
// wrapper.style.gridTemplateColumns = `${'1fr '.repeat(size)}`;
// let arr = [];
let index = 0;
let mistake = 0;
let now = '';
const popUp = document.querySelector('.popUp')
// const timeMinutes = document.querySelector('.time-minutes')
// const popUpMinutes = document.querySelector('.popUp-minutes')
const popUpMistake = document.querySelector('.popUp-mistake')
const popUpTrue = document.querySelector('.popUp-true')
// const popUpSeconds = document.querySelector('.popUp-seconds')
const timeSeconds = document.querySelector('.time-seconds')
const idUser = document.getElementById('id_user')

const StartBtn = document.getElementById('start-btn')
const StartMenu = document.getElementById('start-menu')
const RangeTime = document.getElementById('customRange3')
const label = document.querySelector('.form-label ')
const RangeResult = document.getElementById('range_result')



RangeResult.innerText = 'Выбрано время: ' + RangeTime.value + ' сек.';

let seconds = RangeTime.value;
let seconds2 = seconds;

RangeTime.addEventListener('input', function () {
    RangeResult.innerText = 'Выбрано время ' + RangeTime.value + ' сек.';
    seconds = RangeTime.value;
    seconds2 = seconds;
})


let arrColor = [
    {
        color: 'red',
        colorRU: "красный"
    },
    {
        color: 'green',
        colorRU: "зеленый"
    },
    {
        color: 'blue',
        colorRU: "синий"
    },
    {
        color: 'violet',
        colorRU: "фиолетовый"
    },
    {
        color: 'black',
        colorRU: "черный"
    },
    {
        color: 'orange',
        colorRU: "оранжевый"
    },
];

const colorWord = document.querySelector('.color-word');
const ButtonsColor = document.querySelector('.buttons-color');
let randomeNum;

StartBtn.addEventListener('click', function (e) {
    StartMenu.classList.add('start');

    function randomeNumFunc(arrColorLength) {
        timeSeconds.innerHTML = seconds + " сек.";
        arrColor = arrColor.sort(() => Math.random() - 0.5);
        ButtonsColor.innerHTML = '';
        randomeNum = Math.floor(Math.random() * arrColorLength);

        colorWord.innerHTML = `<div data-color =${arrColor[randomeNum].color}>${arrColor[randomeNum].colorRU}</div>`;
        for (let i = 0; i < arrColor.length; i++) {
            colorWord.classList.remove(`${arrColor[i].color}-word`);
            colorWord.classList.add(`${arrColor[Math.floor(Math.random() * arrColorLength)].color}-word`);
        }
        arrColor.forEach((el) => {
            ButtonsColor.innerHTML += `<div class="col-4 p-0 btn"><div class = "${el.color}"></div></div>`;
        });
    }

    randomeNumFunc(arrColor.length)
    ButtonsColor.addEventListener('click', function (e) {
        target = e.target;
        // console.log(colorWord.firstChild);
        if (colorWord.firstChild.dataset.color == target.className) {
            index++;
            // console.log('win');
        } else {
            mistake++;
            // console.log('lose');
        }
        randomeNumFunc(arrColor.length);
    })


    function time() {
        if (seconds === 0) {
            timeSeconds.innerHTML = 'Время вышло';
            popUpTrue.innerHTML = `Правильных ответов: ${index}`;
            popUpMistake.innerHTML = `Ошибок: ${mistake}`;
            popUp.classList.add('active')

            let id_user = idUser.innerText;

            // console.log(id_user);
            // console.log(index);
            // console.log(mistake);
            // console.log(seconds2);

            let xhr = new XMLHttpRequest();
            xhr.open("POST", "http://45.90.46.161:8088/stroop");
            // xhr.open("POST", "http://localhost:8088/stroop");
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    console.log(xhr.status);
                    console.log(xhr.responseText);
                }
            };

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
          "idUsr": ${id_user},
          "actionDate": "${now}",
          "correct": ${index},
          "mistake": ${mistake},
          "time": ${seconds2}
        }`;

            xhr.send(data);

            seconds = -1;

        } else {
            if (seconds >= 0) {
                seconds = seconds - 1;
                timeSeconds.innerHTML = seconds + " сек.";
            }
        }


        // console.log(seconds);


    }

    setInterval(time, 1000)
})




