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
                window.location.href = '/staff/chi-tiet-giay/page/1?sortField=ngaySua';
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
        } else if(input.files.length < 2){
            alert("Chọn tối thiểu 2 ảnh");
            break;
        } else {

            var file = input.files[i];

            if (file) {
                var reader = new FileReader();

                reader.onload = function (e) {

                    var img = document.createElement("img");
                    img.src = e.target.result;
                    img.style.width ="100px";
                    img.style.height ="100px";

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
            height: 128,
        });
    });
});
