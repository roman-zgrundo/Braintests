let word_count = 4;
let max_word_count;
let minutes = 0;
let seconds = 0;
const RangeSize = document.getElementById('customRange3')
const label = document.querySelector('.form-label ')
const RangeResult = document.getElementById('range_result')
const BtnStart = document.querySelector('.btn_start')
const ChooseCountWords = document.getElementById('choose_count_words')

const popUp = document.querySelector('.popUp')
const timeMinutes = document.querySelector('.time-minutes')
const popUpMinutes = document.querySelector('.popUp-minutes')
const popUpMistake = document.querySelector('.popUp-mistake')
const popUpSeconds = document.querySelector('.popUp-seconds')
const timeSeconds = document.querySelector('.time-seconds')
const BtnBack = document.querySelector('.btn_back')

const GameContainer = document.getElementById('game_container')


const shuffledWords = document.getElementById('lists1-center'); // Используем lists1-center напрямую
const column1Element = document.getElementById('column1')
const column2Element = document.getElementById('column2')


GameContainer.style.display = 'none';

const wordsFromColumn1 = column1Element.textContent.trim().split(' ');
const wordsFromColumn2 = column2Element.textContent.trim().split(' ');


// Получаем количество слов
const count_column1 = wordsFromColumn1.length;
const count_column2 = wordsFromColumn2.length;

console.log(count_column1); // Выводим количество слов
console.log(count_column2);

if (count_column1 < count_column2) {
    max_word_count = count_column1;
} else {
    max_word_count = count_column2;
}
console.log(max_word_count);
RangeSize.max = max_word_count * 2;


RangeResult.innerText = 'Количество слов: ' + RangeSize.value ;
word_count = RangeSize.value;

RangeSize.addEventListener('input', function () {
    RangeResult.innerText = 'Количество слов: ' + RangeSize.value;
    word_count = RangeSize.value;
    console.log(RangeSize.value);
})





// document.addEventListener("DOMContentLoaded", function () {
BtnStart.addEventListener('click', function () {
    const lists1Left = document.getElementById('lists1-left');
    const lists1Center = document.getElementById('lists1-center');
    const lists1Right = document.getElementById('lists1-right');
    const btnCheck = document.querySelector('.btn_check');

    new Sortable(lists1Left, {
        group: 'shared',
        animation: 150
    });

    new Sortable(lists1Center, {
        group: 'shared',
        animation: 150
    });

    new Sortable(lists1Right, {
        group: 'shared',
        animation: 150
    });



    ChooseCountWords.style.display = 'none';
    // RangeResult.style.display = 'none';
    // BtnStart.style.display = 'none';

    GameContainer.style.display = 'block';

    const wordsFromColumn1Copy = [...wordsFromColumn1];
    const wordsFromColumn2Copy = [...wordsFromColumn2];

    shuffleArray(wordsFromColumn1Copy); // Перемешиваем массивы слов
    shuffleArray(wordsFromColumn2Copy);

    const wordsFromColumn1Count = Math.min(word_count / 2, wordsFromColumn1Copy.length);
    const wordsFromColumn2Count = Math.min(word_count / 2, wordsFromColumn2Copy.length);

    const selectedWordsFromColumn1 = wordsFromColumn1Copy.splice(0, wordsFromColumn1Count);
    const selectedWordsFromColumn2 = wordsFromColumn2Copy.splice(0, wordsFromColumn2Count);

    const selectedWords = [...selectedWordsFromColumn1, ...selectedWordsFromColumn2];
    shuffleArray(selectedWords);

    selectedWords.forEach(word => {
        const listItem = document.createElement('div');
        listItem.textContent = word;

        if (selectedWordsFromColumn1.includes(word)) {
            listItem.classList.add('list-group-item', 'for_left_list');
        } else if (selectedWordsFromColumn2.includes(word)) {
            listItem.classList.add('list-group-item', 'for_right_list');
        }

        shuffledWords.appendChild(listItem);
    });

    // setInterval(time, 1000);
console.log(selectedWords);



    btnCheck.addEventListener('click', function () {
        const items = document.querySelectorAll('.for_left_list, .for_right_list');

        items.forEach(item => {
            const isCorrectlyPlaced =
                (item.classList.contains('for_left_list') && item.parentElement === lists1Left) ||
                (item.classList.contains('for_right_list') && item.parentElement === lists1Right);

            item.classList.remove('correct', 'incorrect');
            item.classList.add(isCorrectlyPlaced ? 'correct' : 'incorrect');
        });

        if (document.querySelectorAll('.incorrect').length === 0) {
            popUp.classList.add('active');
            timeMinutes.innerHTML = minutes;
            popUpSeconds.innerHTML = seconds;
            popUpMinutes.innerHTML = minutes;
        }
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

    // Функция для перемешивания массива
    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }
});
