function uploadExcel() {
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];

    if (file) {
        var formData = new FormData();
        formData.append('file', file);

        fetch('/admin/importFromExcel', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
                window.location.href = '/admin/chi-tiet-giay';
            })
            .catch(error => {
                alert('Tải danh sách thất bại, vui lòng kiểm tra lại');
                console.error('Error during file upload:', error);
            });
    } else {
        alert('Bạn chưa chọn tệp');
    }
}


var selectedImages = []; // Mảng lưu trữ các ảnh đã chọn
var maxImages = 3; // Số lượng tối đa ảnh được phép tải lên

function showSelectedImages(input) {
    var imageList = document.getElementById("imageList");
    imageList.innerHTML = ''; // Xóa bỏ danh sách ảnh trước đó

    for (var i = 0; i < input.files.length; i++) {
        if (input.files.length > maxImages) {
            alert("Chỉ được tải lên tối đa " + maxImages + " ảnh.");
            break;
        } else {

            var file = input.files[i];

            if (file) {
                var reader = new FileReader();

                reader.onload = function (e) {

                    var img = document.createElement("img");
                    img.src = e.target.result;

                    var listItem = document.createElement("li");
                    listItem.appendChild(img);

                    // Tạo nút xóa và thêm sự kiện xóa
                    var deleteButton = document.createElement("button");
                    deleteButton.innerHTML = '<i class="fa fa-times-circle"></i>'; // Biểu tượng X
                    deleteButton.onclick = function () {
                        deleteImage(this);
                    };
                    listItem.appendChild(deleteButton);

                    imageList.appendChild(listItem);
                    selectedImages.push(file); // Lưu trữ ảnh đã chọn
                };

                reader.readAsDataURL(file);
            }
        }
    }
}

function deleteImage(button) {
    var listItem = button.parentElement;
    var index = Array.from(listItem.parentElement.children).indexOf(listItem);
    selectedImages.splice(index, 1); // Xóa ảnh khỏi mảng
    listItem.remove(); // Xóa phần tử li chứa ảnh
}

function deleteSelectedImages() {
    // Xóa tất cả các ảnh đã chọn
    selectedImages = [];
    var imageList = document.getElementById("imageList");
    imageList.innerHTML = '';
}

function validateForm() {
    var selectedImages = document.querySelectorAll('#imageList li');
    var validationMessage = document.getElementById('imageValidationMessage');
    var soLuongInput = document.getElementById('soluong');
    var soLuongValidationMessage = document.getElementById('soLuongValidationMessage');
    var giaInput = document.getElementById('gia');
    var giaValidationMessage = document.getElementById('giaValidationMessage');
    var tenInput = document.getElementById('tenChiTietGiay');
    var tenValidationMessage = document.getElementById('tenValidationMessage');
    var moTaInput = document.getElementById('moTa');
    var moTaValidationMessage = document.getElementById('moTaValidationMessage');
    var qrCodeInput = document.getElementById('qrCode');
    var qrCodeValidationMessage = document.getElementById('qrCodeValidationMessage');
    var maInput = document.getElementById('maChiTietGiay');
    var maValidationMessage = document.getElementById('maValidationMessage');

    var hasErrors = false;

    if (!isAlphanumeric(maInput.value) || maInput.value.length > 20) {
        maValidationMessage.textContent = "Mã chỉ được nhập chữ và số, không quá 20 ký tự.";
        hasErrors = true;
    } else {
        maValidationMessage.textContent = ""; // Ẩn thông báo lỗi khi không có lỗi
    }

    if (!isAlphanumeric(tenInput.value) || tenInput.value.length > 50) {
        tenValidationMessage.textContent = "Tên chỉ được nhập chữ và số, không quá 50 ký tự.";
        hasErrors = true;
    } else {
        tenValidationMessage.textContent = "";
    }

    if (!isPositiveNumber(soLuongInput.value) || parseInt(soLuongInput.value) >= 10000) {
        soLuongValidationMessage.textContent = "Số lượng phải là số dương và nhỏ hơn 10.000.";
        hasErrors = true;
    } else {
        soLuongValidationMessage.textContent = "";
    }

    const inputValue = parseFloat(giaInput.value);

    if (isNaN(inputValue) || inputValue <= 0 || inputValue >= 1000000000) {
        giaValidationMessage.textContent = "Giá phải là số dương và nhỏ hơn 1.000.000.000.";
        hasErrors = true;
    } else {
        giaValidationMessage.textContent = "";
    }


    if (moTaInput.value.length > 2000) {
        moTaValidationMessage.textContent = "Mô tả không được vượt quá 2000 ký tự.";
        hasErrors = true;
    } else {
        moTaValidationMessage.textContent = "";
    }

    if (!isAlphanumeric(qrCodeInput.value) || maInput.value.length > 30) {
        qrCodeValidationMessage.textContent = "QR Code chỉ được nhập chữ và số, không quá 30 ký tự";
        hasErrors = true;
    } else {
        qrCodeValidationMessage.textContent = "";
    }

    if (selectedImages.length < 2) {
        validationMessage.textContent = "Bạn phải chọn ít nhất 2 ảnh.";
        hasErrors = true;
    } else {
        validationMessage.textContent = "";
    }

    return !hasErrors; // Trả về true nếu không có lỗi, ngược lại trả về false
}

function isNumber(value) {
    return /^[0-9]+$/.test(value);
}

function isPositiveNumber(value) {
    return /^[1-9]\d*$/.test(value);
}

function isAlphanumeric(value) {
    return /^[a-zA-Z0-9]+$/.test(value);
}

// hiện qr form sửa
document.addEventListener("DOMContentLoaded", function () {
    // Lấy tất cả các phần tử có class "qr-code"
    var qrcodeContainers = document.querySelectorAll(".qr-code");

    // Duyệt qua từng phần tử và tạo mã QR
    qrcodeContainers.forEach(function (qrcodeContainer) {
        // Lấy dữ liệu từ thuộc tính th:text
        var qrCodeData = qrcodeContainer.querySelector('div').textContent;

        // Tạo mã QR
        var qrcode = new QRCode(qrcodeContainer, {
            text: qrCodeData,
            width: 128,
            height: 128
        });
    });
});
