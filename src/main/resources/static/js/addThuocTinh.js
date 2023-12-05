let buttons = document.querySelectorAll('.clickButton');
let temp = '';

console.log('test: ')
console.log(buttons);
buttons.forEach(function (button) {
    button.addEventListener('click', function () {
        clear();
        temp = button.id;
        console.log('Button clicked:', temp);
    });
});

document.getElementById('btnSubmit').addEventListener('click', function () {
    let ma = document.getElementById('inputmaThuocTinh').value;
    let ten = document.getElementById('inputtenThuocTinh').value;
    validate(ma, ten, temp);
})

function clear() {
    document.getElementById('inputmaThuocTinh').value = '';
    document.getElementById('inputtenThuocTinh').value = '';
    document.getElementById('errMaThuocTinh').innerText = '';
    document.getElementById('errTenThuocTinh').innerText = ''
}

function findAll(data, select, ma) {
    select.innerHTML = '';
    for (let index = 0; index < data.length; index++) {
        insertCbb(data[index], select);
        if (data[index].ma == ma) {
            select.value = data[index].id;
        }
    }
}

function insertCbb(x, select) {
    let row = document.createElement('option');
    row.text = x.ten;
    row.value = x.id;
    select.add(row);
}

function validate(ma, ten, thuocTinh) {
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/api/chi-tiet-giay/find' + thuocTinh,
        success: function (data) {
            let count = 0;
            if (ma.trim().length == 0 || ma.trim().length > 20) {
                document.getElementById('errMaThuocTinh').innerText = 'Vui lòng nhập mã, tối đa 20 kí tự';
                count++;
            } else {
                let check = 0;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].ma == ma) {
                        document.getElementById('errMaThuocTinh').innerText = 'Mã đã tồn tại';
                        check = 1;
                        count++;
                        break;
                    }
                    if (check == 0) {
                        document.getElementById('errMaThuocTinh').innerText = '';
                    }
                }
            }

            if (ten.trim().length == 0) {
                document.getElementById('errTenThuocTinh').innerText = 'Vui lòng nhập tên, tối đa 50 kí tự';
                count++;
            } else {
                let check = 0;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].ten == ten) {
                        document.getElementById('errTenThuocTinh').innerText = 'Tên đã tồn tại';
                        check = 1;
                        count++;
                        break;

                    }
                }
                if (check == 0) {
                    document.getElementById('errTenThuocTinh').innerText = '';
                }
            }

            if (count == 0) {
                let params = {
                    ma: ma,
                    ten: ten,
                    trangThai: 0
                }

                $.ajax({
                    type: 'POST',
                    url: 'http://localhost:8080/api/chi-tiet-giay/add' + thuocTinh,
                    data: JSON.stringify(params),
                    contentType: 'application/json',
                    success: function (data1) {

                        $.ajax({
                            type: "Get",
                            url: "http://localhost:8080/api/chi-tiet-giay/" + thuocTinh,
                            success: function (data) {
                                let seclect = document.getElementById('cbb' + thuocTinh);
                                findAll(data, seclect, ma);
                                $('#modalThuocTinh').modal('hide');
                                let type = 'success';
                                let icon = 'fa-solid fa-circle-check';
                                let title = 'Thành công!';
                                let text = "Thuộc tính đã được thêm";
                                createToast(type, icon, title, text);
                            }
                        })
                    }
                })
            }
        }
    })
}
