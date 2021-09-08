$(document).ready(function getHall() {
    $.ajax({
        type: 'GET',
        url: '/job4j_cinema/hall',
        dataType: 'json'
    }).done(function (data) {
        let id
        let row
        let cell
        for (let place of data) {
            id = place.id
            row = place.row
            cell = place.cell
            if (place.isBooked) {
                $('table tbody tr:nth-child(' + row + ') td:nth-child(' + (cell + 1) + ')')
                    .replaceWith(`<td class="text-danger">&emsp;Ряд ${row}, Место ${cell}</td>`);
            } else {
                $('table tbody tr:nth-child(' + row + ') td:nth-child(' + (cell + 1) + ')')
                    .replaceWith(
                        `<td class="text-success"><input type="radio" name="place" id="${id}" value="row=${row}&cell=${cell}"> Ряд ${row}, Место ${cell}</td>`
                    );
            }
        }
    }).fail(function (err) {
        console.log(err);
    });
    setTimeout(getHall, 10 * 1000);
});

function validateAndBook() {
    let place = $('input[name="place"]:checked').val();
    let placeId = $('input[name="place"]:checked').attr('id');
    if (place === undefined) {
        alert('Выберите место');
    } else {
        $.ajax({
            type: 'POST',
            url: '/job4j_cinema/hall',
            data: JSON.stringify({
                id: placeId
            }),
            dataType: 'json',
            success: window.location.href = "http://localhost:8080/job4j_cinema/payment.html?" + place
        }).fail(function (err) {
            console.log(err);
        });
    }
}