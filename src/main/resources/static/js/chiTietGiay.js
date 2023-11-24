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
