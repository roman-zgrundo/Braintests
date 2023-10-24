document.addEventListener('DOMContentLoaded', function() {
    const rows = document.querySelectorAll('#my_table tbody tr');
    let num = 1;

    rows.forEach(function(row) {
        const firstCell = row.querySelector('th');

        if (firstCell) {
            firstCell.textContent = num.toString();
            num++;
        }
    });
});