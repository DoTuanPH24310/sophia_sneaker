const tinh = [[${session.tinh}]];
const quan = [[${session.quan}]];
let phuong = [[${session.phuong}]];
const host = "https://provinces.open-api.vn/api/";
let modalDDC = document.getElementById('modalDoiDiaChi');
let modalContentDDC = modalDDC.querySelector('.modal-content');

// function resetModalTDC() {
//     modalContentDDC.style.opacity = '1';
// }

var renderData = (array, select) => {
    let row;
    array.forEach(element => {
        if (tinh === element.code) {
            renderDataDis(element.districts, "district");
            row += `<option selected value="${element.code}">${element.name}</option>`;
        } else {
            row += `<option value="${element.code}">${element.name}</option>`;
        }
    });
    document.querySelector("#" + select).innerHTML = row;
}

var renderDataDis = (array, select) => {
    let row = ' <option disable value="">--Chọn Quận/Huyện--</option>';
    array.forEach(element => {
        if (quan === element.code) {
            renderDataWar(element.wards, "ward");
            row += `<option selected value="${element.code}">${element.name}</option>`;
        } else {
            row += `<option value="${element.code}">${element.name}</option>`;
        }

    });

    document.querySelector("#" + select).innerHTML = row;
}

var renderDataWar = (array, select) => {
    let row = ' <option disable value="">--Chọn Phường/Xã--</option>';
    array.forEach(element => {
        if (phuong === element.code) {
            row += `<option selected value="${element.code}">${element.name}</option>`;
            console.log(element);
        } else {
            row += `<option value="${element.code}">${element.name}</option>`;
        }

    });
    document.querySelector("#" + select).innerHTML = row;
}

var callAPI = (api) => {
    return axios.get(api)
        .then((response) => {
            renderData(response.data, "province");
        });
};
callAPI('https://provinces.open-api.vn/api/?depth=3');


var callApiDistrict = (api) => {
    return axios.get(api)
        .then((response) => {
            renderDataDis(response.data.districts, "district");
        });
};
var callApiWard = (api) => {
    return axios.get(api)
        .then((response) => {
            renderDataWar(response.data.wards, "ward");
        });
}

$("#province").change(() => {
    phuong = 0;
    callApiDistrict(host + "p/" + $("#province").val() + "?depth=2")
    callApiWard(host + "d/" + $("#district").val() + "?depth=2");
    printResult();
});


$("#district").change(() => {
    callApiWard(host + "d/" + $("#district").val() + "?depth=2");
    printResult();
});
$("#ward").change(() => {
    printResult();
})

var printResult = () => {
    if ($("#district").val() != "" && $("#province").val() != "" &&
        $("#ward").val() != "") {
        let result = $("#province option:selected").text() +
            " | " + $("#district option:selected").text() + " | " +
            $("#ward option:selected").text();
        $("#result").text(result)
    }

}

function previewImage(event) {
    const fileInput = event.target;
    const file = fileInput.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            const imageElement = document.querySelector('img[alt="Avatar"]'); // Chọn thẻ img của bạn
            imageElement.src = e.target.result;
        };

        reader.readAsDataURL(file);
    }
}

//    Thêm nhiều địa chỉ


