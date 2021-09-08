$(document).ready(function () {
    let url = new URL(window.location.href);
    let row = url.searchParams.get("row");
    let cell = url.searchParams.get("cell");
    $('h3').html(`<h3>Вы выбрали Ряд: ${row}, Место: ${cell}, Сумма: 500 рублей</h3>`)
});

function pay() {
    if ($('#username').val() === "") {
        alert($('#username').attr('title'));
    } else if ($('#email').val() === "") {
        alert($('#email').attr('title'));
    } else if ($('#phone').val() === "") {
        alert($('#phone').attr('title'));
    } else {
        let url = new URL(window.location.href);
        let row = url.searchParams.get("row");
        let cell = url.searchParams.get("cell");
        $.ajax({
            type: 'POST',
            url: '/job4j_cinema/payment',
            data: JSON.stringify({
                row: row,
                cell: cell,
                name: $('#username').val(),
                email: $('#email').val(),
                phone: $('#phone').val()
            }),
            dataType: 'json',
            success: function (data) {
                if (data) {
                    alert("Оплата произведена");
                } else {
                    alert("Извините, данное место уже забронировано");
                }
                window.location.href = "/job4j_cinema/index.html";
            },
            error: function (err) {
                console.log(err);
            }
        });
    }
}