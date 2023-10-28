const customSwitches = document.getElementById("customSwitches");
const form = document.getElementById("form");

function loadding() {
    if (customSwitches.checked) {
        form.style.display = "block";
    } else {
        form.style.display = "none";
    }
}

customSwitches.addEventListener("change", loadding);
window.addEventListener("load", loadding);

// load modal

var buttons = null;
const tableBody = document.getElementById("sinhvien");
const table = document.getElementById("table");
var maCTG = '';
var selectHang = document.getElementById('cbbHang');
var selectGiay = document.getElementById('cbbGiay');
var selectDe = document.getElementById('cbbDe');
var selectLoaiGiay = document.getElementById('cbbLoaiGiay');
var selectSize = document.getElementById('cbbSize');
var selectMauSac = document.getElementById('cbbMau');
// Fill and click CTG
function getAllList() {
    $.ajax({
        type: "get",
        url: "http://localhost:9090/api/chi-tiet-giay/allCTG",
        success: function (data) {
            document.getElementById('err').innerHTML = "";
            document.getElementById('textSearch').value = "";
            loadAll(data);
            buttons = document.querySelectorAll("#sinhvien  button");
            addEventButton();
            loadAllSelect();
        }
    })
}

function loadAll(data) {
    tableBody.innerHTML = '';
    for (let index = 0; index < data.length; index++) {
        insertTr(data[index]);
    }
}

function insertTr(x) {
    var btn = '<button class="btn btn-primary" data-toggle="modal" data-target="#modalSoLuong">Thêm</button>';
    const row = tableBody.insertRow();
    const anh = row.insertCell().innerHTML = '';
    const ma = row.insertCell().innerHTML = x.ma;
    const tenGiay = row.insertCell().innerHTML = x.giay.ten;
    const loaiDe = row.insertCell().innerHTML = x.deGiay.ten;
    const hang = row.insertCell().innerHTML = x.hang.ten;
    const loaiGiay = row.insertCell().innerHTML = x.loaiGiay.ten;
    const soLuong = row.insertCell().innerHTML = x.soLuong;
    const gia = row.insertCell().innerHTML = x.gia;
    const button = row.insertCell().innerHTML = btn;
}
// Thêm sự kiện click cho mỗi button

function addEventButton() {
    for (let index = 0; index < buttons.length; index++) {
        buttons[index].addEventListener("click", function () {
            var row = index + 1;
            maCTG = table.rows[row].cells[1].textContent;
            document.getElementById("inputMaCTG").value = maCTG;
        });
    }
}


function loadAllSelect() {
    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/hang",
        success: function (data) {
            findAll(data, selectHang);
        }

    })

    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/giay",
        success: function (data) {
            findAll(data, selectGiay);
        }

    })

    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/de",
        success: function (data) {
            findAll(data, selectDe);
        }

    })

    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/loaiGiay",
        success: function (data) {
            findAll(data, selectLoaiGiay);
        }

    })

    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/size",
        success: function (data) {
            findAll(data, selectSize);
        }

    })

    $.ajax({
        type: "Get",
        url: "http://localhost:9090/api/chi-tiet-giay/mau",
        success: function (data) {
            findAll(data, selectMauSac);
        }

    })
}

function findAll(data, select) {
    select.innerHTML = '';
    var row = document.createElement('option');
    row.text = '---Tất cả---';
    row.value = null;
    row.style.textAlign = 'center';
    select.add(row);
    for (let index = 0; index < data.length; index++) {
        insertCbb(data[index], select);
    }
}

// Fill Hang
function insertCbb(x, select) {
    var row = document.createElement('option');
    row.text = x.ten;
    row.value = x.id;
    row.style.textAlign = 'center';
    select.add(row);
}

function test() {
    var params = {
        idGiay: selectGiay.value,
        idHang: selectHang.value,
        idDe: selectDe.value,
        idLoaigiay: selectLoaiGiay.value,
        idSize: selectSize.value,
        idMau: selectMauSac.value,
        textSearch: document.getElementById('textSearch').value
    }
    $.ajax({
        type: "POST",
        url: "http://localhost:9090/api/chi-tiet-giay/multipleFind",
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function (data) {
            console.log(document.getElementById('textSearch').value);
            if (data.length == 0) {
                document.getElementById('err').innerHTML = "Không có dữ liệu phù hợp";
                loadAll(data);
            }
            else {
                document.getElementById('err').innerHTML = "";
                loadAll(data);
            }
        }
    })
}
