const stringWrapper = document.querySelector('.string-wrapper');
const btn = document.querySelector('.btn_start');
const mainSet = document.getElementById('main_set');
const labelR3 = document.querySelector('.form-for-customRange3');
const time = document.getElementById('customRange3');
const btnExit = document.getElementById('btn_exit');
const chosenWordCount = document.getElementById('customRange4');
const labelR4 = document.querySelector('.form-for-customRange4');
const RangeResult = document.getElementById('range_result');
const RangeResult4 = document.getElementById('range_word_count');
const wordCount = document.getElementById('word_count');
const nameSet = document.getElementById('name_set');
const idUser = document.getElementById('id_user');
const popUp = document.querySelector('.popUp');
const popUpMistake = document.querySelector('.popUp-mistake');
const popUpWordCount = document.getElementById('popup_word_count');

let wordCountValue = 0;
let mistakeCount = 0;
let i = 0;
let b = 500;
let a = 400;
let now = '';
let str = mainSet.innerText;
let randomWords = [];
let buttonIndex = 0;
let gameStarted = false; // Добавляем флаг состояния игры

RangeResult.innerText = 'Выбрана скорость ' + time.value + ' сек.';

time.addEventListener('input', function () {
    RangeResult.innerText = 'Выбрана скорость ' + time.value + ' сек.';
});

RangeResult4.innerText = 'Выбрано слов: ' + chosenWordCount.value;

chosenWordCount.addEventListener('input', function () {
    RangeResult4.innerText = 'Выбрано слов: ' + chosenWordCount.value;
});

btn.addEventListener('click', function () {

    mistakeCount = 0;
    i = 0;
    buttonIndex = 0; // Сброс buttonIndex
    stringWrapper.innerHTML = '';
    let arr = str.split(' ');

    let wordCountSelected = chosenWordCount.value;

    let timeSelected = parseFloat(time.value) * 1000;

    randomWords = getRandomWords(arr, wordCountSelected);

    RangeResult.classList.add('active');
    btn.classList.add('active');
    time.classList.add('active');
    labelR4.classList.add('active');
    labelR3.classList.add('active');
    RangeResult4.classList.add('active');
    chosenWordCount.classList.add('active');
    btnExit.classList.add('active');

    showWord(timeSelected);
});

function showWord(delay) {
    if (i < randomWords.length) {
        let word = document.createElement('span');
        word.innerText = randomWords[i];
        word.classList.add('fs-1'); // Добавляем класс "fs-1"
        stringWrapper.innerHTML = ''; // Очищаем содержимое stringWrapper перед добавлением нового слова
        stringWrapper.appendChild(word);
        word.classList.add('active');

        setTimeout(hideWord, a, delay);
    } else {
        showButtons();
    }
}


function hideWord(delay) {
    let word = stringWrapper.querySelector('span.active');
    word.classList.remove('active');

    setTimeout(function () {
        if (i < randomWords.length - 1) {
            i++;
            showWord(delay);
        } else {
            showButtons();
        }
    }, delay);
}

function removeWord() {
    let word = stringWrapper.querySelector('span');
    word.remove();
    i++;

    showWord();
}

function showButtons() {
    stringWrapper.innerHTML = '';

    let shuffledWords = shuffleArray(randomWords);

    shuffledWords.forEach((word) => {
        let button = document.createElement('button');
        button.classList.add('word-button');
        button.classList.add('btn');
        button.classList.add('btn-lg');
        button.classList.add('btn-dark');
        button.classList.add('fs-1');
        button.classList.add('m-4');
        button.innerText = word;
        button.addEventListener('click', function () {
            if (button.innerText === randomWords[buttonIndex]) {
                button.classList.add('correct');
                buttonIndex++;

                if (buttonIndex === randomWords.length) {
                    setTimeout(removeButtons, b);
                }
            } else {
                button.classList.add('incorrect');
                mistakeCount++; // Увеличение mistakeCount на единицу
                setTimeout(function () {
                    button.classList.remove('incorrect');
                }, 1000);
            }
        });

        stringWrapper.appendChild(button);
    });
}

function removeButtons() {
    wordCountValue = randomWords.length;
    // stringWrapper.innerHTML = '';

    popUp.classList.add('active')
    popUpMistake.innerHTML = `Количество ошибок: ${mistakeCount}`;
    popUpWordCount.innerHTML = `Количество слов: ${wordCountValue}`;
    btn.classList.remove('active');
    // RangeResult.classList.remove('active');
    // time.classList.remove('active');
    // labelR3.classList.remove('active');
    // labelR4.classList.remove('active');
    // RangeResult4.classList.remove('active');
    // chosenWordCount.classList.remove('active');
    // btnExit.classList.remove('active');


    saveInDB();

    if (gameStarted) { // Добавляем проверку на состояние игры
        let delayTime = time.value * 1000;
        setTimeout(function () {
            btn.click();
        }, delayTime);
    }

    gameStarted = false; // Сбрасываем флаг состояния игры после удаления кнопок

}

function saveInDB() {
    let id_user = idUser.innerText;


    let xhr = new XMLHttpRequest();
    xhr.open("POST", "http://78.110.62.84:8088/flashingwords");
    // xhr.open("POST", "http://localhost:8088/flashingwords");
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

    console.log(id_user);
    console.log(now);
    console.log(nameSet.innerText);
    console.log(wordCountValue);
    console.log(mistakeCount);

    let data = `{
        "user_id": ${id_user},
        "actionDate": "${now}",
        "name_set": "${nameSet.innerText}",
        "word_count": "${wordCountValue}",
        "mistake": ${mistakeCount},
        "speed": ${time.value}
    }`;

    xhr.send(data);
}


function getRandomWords(arr, count) {
    let randomWords = [];

    while (randomWords.length < count) {
        let randomIndex = Math.floor(Math.random() * arr.length);
        let randomWord = arr[randomIndex];

        if (!randomWords.includes(randomWord)) {
            randomWords.push(randomWord);
        }
    }

    return randomWords;
}

function shuffleArray(arr) {
    let shuffledArray = [...arr];

    for (let i = shuffledArray.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffledArray[i], shuffledArray[j]] = [shuffledArray[j], shuffledArray[i]];
    }

    return shuffledArray;
}

btnExit.addEventListener('click', function () {
    stringWrapper.innerHTML = '';
    RangeResult.classList.remove('active');
    btn.classList.remove('active');
    time.classList.remove('active');
    labelR3.classList.remove('active');
    labelR4.classList.remove('active');
    RangeResult4.classList.remove('active');
    chosenWordCount.classList.remove('active');
    btnExit.classList.remove('active');
});

btnExit.addEventListener('click', function () {
    gameStarted = false; // Добавляем сброс флага состояния игры при нажатии на кнопку "Выход"
});
