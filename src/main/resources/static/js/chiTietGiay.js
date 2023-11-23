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
            .then(response => {
                if (response.ok) {
                    alert('Tải lên thành công');
                    window.location.href = '/admin/chi-tiet-giay';
                } else {
                    alert('Tải danh sách thất bại, vui lòng kiểm tra lại');
                    console.error(response.statusText);
                }
            })
            .catch(error => {
                console.error('Error during file upload:', error);
            });
    } else {
        alert('Bạn chưa chọn tệp');
    }
}
    $(document).ready(function () {
    $("#trangThaiSelect").change(function () {
        var selectedValue = $(this).val();
        $("#trangThaiHidden").val(selectedValue);
    });
});
