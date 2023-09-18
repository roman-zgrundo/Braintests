

let today = new Date();

// Получаем ссылку на поле input
let dateInputs = document.querySelectorAll('.my-today-date');

// Преобразуем дату в формат yyyy-mm-dd
let yyyy = today.getFullYear();
let mm = String(today.getMonth() + 1).padStart(2, '0');
let dd = String(today.getDate()).padStart(2, '0');
let formattedDate = yyyy + '-' + mm + '-' + dd;

// Устанавливаем значение для каждого поля input
dateInputs.forEach(function(dateInput) {
    dateInput.value = formattedDate;
});

