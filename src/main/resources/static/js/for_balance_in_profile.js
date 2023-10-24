function checkIfLessThan24Hours(historyId) {
    $.ajax({
        type: "GET",
        url: "/checkIfLessThan24Hours?historyId=" + historyId,
        success: function (result) {
            if (result) {
                // Запись создана менее чем 24 часа назад, выполните нужные действия здесь
                console.log("Запись создана менее 24 часов назад");
            } else {
                // Запись создана более 24 часов назад, выполните нужные действия здесь
                console.log("Запись создана более 24 часов назад");
            }
        },
        error: function () {
            console.error("Ошибка при выполнении AJAX-запроса");
        }
    });
}