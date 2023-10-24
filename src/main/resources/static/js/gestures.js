const wrapper = document.querySelector('.wrapper');
let minutes = 0;
let seconds = 0;
let mistake = 0;
let size1 = 5;
let size2 = 5;
let isChecked;

const popUp = document.querySelector('.popUp')
const timeMinutes = document.querySelector('.time-minutes')
const timeSeconds = document.querySelector('.time-seconds')
const idUser = document.getElementById('id_user')
const flexSwitch = document.getElementById('flexSwitch')
const labelForFlexSwitch = document.getElementById('labelForFlexSwitch')
const startParam = document.getElementById('start_param')

const RangeSpeed = document.getElementById('customRange1')
const RangeCount = document.getElementById('customRange2')
const label = document.querySelector('.form-label ')
const RangeResult1 = document.getElementById('range_result1')
const RangeResult2 = document.getElementById('range_result2')
const MyTimer = document.getElementById('my_timer')
MyTimer.style.display = 'none';

const btn = document.getElementById('btn_start')

// Массив URL-адресов всех изображений
const startImageUrls = [
    "1.1.png", "1.2.png",
    "2.1.png", "2.2.png",
    "3.1.png", "3.2.png",
    "4.1.png", "4.2.png",
    "5.1.png", "5.2.png",
    "6.1.png", "6.2.png",
    "7.1.png", "7.2.png",
    "8.1.png", "8.2.png",
    "9.1.png", "9.2.png",
    "10.1.png", "10.2.png",
    "11.1.png", "11.2.png",
    "12.1.png", "12.2.png",
    "13.1.png", "13.2.png",
    "14.1.png", "14.2.png"
];
let imageUrls;


RangeResult1.innerText = 'Выбрано жестов: ' + RangeSpeed.value;
size1 = RangeSpeed.value;

RangeSpeed.addEventListener('input', function () {
    RangeResult1.innerText = 'Выбрано жестов: ' + RangeSpeed.value;
    size1 = RangeSpeed.value;
})

RangeResult2.innerText = 'Выбрана скорость: ' + RangeCount.value;
size2 = RangeCount.value;

RangeCount.addEventListener('input', function () {
    RangeResult2.innerText = 'Выбрана скорость: ' + RangeCount.value;
    size2 = RangeCount.value;
})

btn.addEventListener('click', function () {

    // Индекс текущей пары
    let currentIndex = 0;

    seconds = 0;
    minutes = 0;
    timeSeconds.innerHTML = '00';
    timeMinutes.innerHTML = '0';

    MyTimer.style.display = '';
    btn.style.display = 'none';
    startParam.classList.add('start')
    flexSwitch.classList.add('start')
    labelForFlexSwitch.classList.add('start')

    isChecked = flexSwitch.checked;


// Можно выполнить необходимые действия на основе состояния переключателя
    if (isChecked) {
        console.log('1------------------');
        imageUrls = startImageUrls;
        // Переключатель включен
        console.log('Переключатель включен');
        console.log(size1);

    } else {
        console.log('2------------------');
        console.log(startImageUrls);
        // Разделите массив на два: один для элементов ".1" и другой для элементов ".2"
        let imageUrls1 = startImageUrls.filter(url => url.endsWith(".1.png"));
        let imageUrls2 = startImageUrls.filter(url => url.endsWith(".2.png"));
        console.log(imageUrls1);
        console.log(imageUrls2);

// Перемешайте каждый из двух массивов
        shuffleArrayForEveryone(imageUrls1);
        shuffleArrayForEveryone(imageUrls2);

        console.log('перемешивание------------------');
        console.log(imageUrls1);
        console.log(imageUrls2);

// Объедините массивы обратно
        let shuffledImageUrls = [];
        for (let i = 0; i < imageUrls1.length; i++) {
            shuffledImageUrls.push(imageUrls1[i]);
            shuffledImageUrls.push(imageUrls2[i]);
        }

        console.log(shuffledImageUrls);
        imageUrls = shuffledImageUrls;

        // Переключатель выключен
        console.log('Переключатель выключен');
    }


    // Сгенерируйте список пар изображений, учитывая ваше ограничение
    const allPairs = [];
    // Создаем все уникальные пары изображений
    for (let i = 0; i < imageUrls.length; i += 2) {
        allPairs.push([imageUrls[i], imageUrls[i + 1]]);
    }

    console.log("Все уникальные пары изображений:");
    console.log(allPairs);

    let imagePairs = [];
    let remainingPairs = [...allPairs]; // Копия всех пар для выбора

// Создаем случайную выборку уникальных пар изображений
    if (size1 % 2 == 0) {
        while (imagePairs.length < size1 * 2) {
            // Выбираем случайный индекс из оставшихся пар
            const randomIndex = Math.floor(Math.random() * remainingPairs.length);

            // Добавляем выбранную пару к итоговому массиву
            imagePairs.push(remainingPairs[randomIndex]);

            // Удаляем выбранную пару из списка оставшихся
            remainingPairs.splice(randomIndex, 1);

            // Если осталось мало пар для выбора, перемешиваем список и сбрасываем счетчик
            if (remainingPairs.length < 5) {
                remainingPairs = [...allPairs];
            }
        }

    } else if (size1 % 2 == 1) {
        while (imagePairs.length < size1) {
            // Выбираем случайный индекс из оставшихся пар
            const randomIndex = Math.floor(Math.random() * remainingPairs.length);

            // Добавляем выбранную пару к итоговому массиву
            imagePairs.push(remainingPairs[randomIndex]);

            // Удаляем выбранную пару из списка оставшихся
            remainingPairs.splice(randomIndex, 1);

            // Если осталось мало пар для выбора, перемешиваем список и сбрасываем счетчик
            if (remainingPairs.length < 5) {
                remainingPairs = [...allPairs];
            }
        }

    }

// Перемешиваем массив imagePairs
    shuffleArray(imagePairs);

    console.log("Перемешанный массив изображений:");
    console.log(imagePairs);


    // Подгрузите пару изображений на экран с учетом скорости size2
    function displayImagePair(pairIndex) {
        console.log('-----------------')
        console.log(pairIndex)
        const pair = imagePairs[pairIndex];
        const imageHTML = `<img src="/image/palms/${pair[0]}" alt="Image" width="350" height="350">
                       <img src="/image/palms/${pair[1]}" alt="Image" width="350" height="350">`;

        wrapper.innerHTML = imageHTML;

        currentIndex = (currentIndex + 1) % imagePairs.length;
    }

    // Отобразите первую пару изображений немедленно
    // displayImagePair(0);

// Запустите интервал для смены пар изображений с заданной скоростью
    const interval = setInterval(() => {
        if (currentIndex < imagePairs.length) {
            displayImagePair(currentIndex);
            currentIndex++;
        } else {
            clearInterval(interval); // Останавливаем интервал после окончания показа картинок
            clearDisplay(); // Очистить содержимое и восстановить элементы управления
        }
    }, size2 * 1000);


});


function time() {
    seconds = seconds + 1;
    if (seconds === 60) {
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


function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i][0], array[j][0]] = [array[j][0], array[i][0]]; // Перемешиваем первые элементы пар
        [array[i][1], array[j][1]] = [array[j][1], array[i][1]]; // Перемешиваем вторые элементы пар
    }
}

function shuffleArrayForEveryone(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]]; // Перемешиваем элементы массива
    }
}

function clearDisplay() {
    wrapper.innerHTML = ''; // Очистить содержимое
    // Восстановить контрольные элементы
    MyTimer.style.display = 'none';
    btn.style.display = '';
    startParam.classList.remove('start')
    flexSwitch.classList.remove('start')
    labelForFlexSwitch.classList.remove('start')
}

