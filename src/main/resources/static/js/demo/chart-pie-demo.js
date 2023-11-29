// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';


// TRẠNG THAI
function mapStatusToLabel(status) {
    switch (status) {
        case 1:
            return 'Thành công';
        case 2:
            return 'Đang xử lý';
        case 3:
            return 'Chờ xử lý';
        case 4:
            return 'Thất bại';
        case 5:
            return 'Đã gửi';
        case 6:
            return 'Hủy';
        default:
            return 'Unknown';
    }
}
// Hàm để gọi API và cập nhật biểu đồ
function callApiAndUpdatePieChart() {
    // Lấy giá trị mới của ngayBatDau và ngayKetThuc
    var ngayBatDauValue = ngayBatDauInput.value;
    var ngayKetThucValue = ngayKetThucInput.value;
    var url2 = "/api/chart/status?ngayBatDau=" + encodeURIComponent(ngayBatDauValue) + "&ngayKetThuc=" + encodeURIComponent(ngayKetThucValue);
    console.log('ngày' +ngayBatDauValue)
    // Tạo URL mới với giá trị ngayBatDau và ngayKetThuc mới
    var pieChart = document.getElementById("myPieChart");
    // Fetch dữ liệu mới từ API
    fetch(url2)
        .then(response => response.json())
        .then(data => {
            const backgroundColorArray = ['#4e73df', '#1cc88a', '#36b9cc', '#ff6384', '#ff9f40', '#4bc0c0'];
            const hoverBackgroundColorArray = ['#2e59d9', '#17a673', '#2c9faf', '#e03350', '#e67f34', '#46a3a3'];

// Sử dụng mảng màu sắc trong mã của bạn
            const chartData = {
                datasets: [{
                    backgroundColor: backgroundColorArray,
                    hoverBackgroundColor: hoverBackgroundColorArray,
                    // Các thuộc tính khác của dataset
                    hoverBorderColor: hoverBackgroundColorArray,
                }],
                // Các thuộc tính khác của biểu đồ
            };

            var myPieChart = new Chart(pieChart, {
                type: 'doughnut',
                data: {
                    labels: data.map(item => mapStatusToLabel(item[0])),
                    datasets: [{
                        data: data.map(item => item[1]),
                        backgroundColor: backgroundColorArray,
                        hoverBackgroundColor: hoverBackgroundColorArray,
                        hoverBorderColor: hoverBackgroundColorArray,                    }],
                },

                options: {
                    maintainAspectRatio: false,
                    tooltips: {
                        backgroundColor: "rgb(255,255,255)",
                        bodyFontColor: "#858796",
                        borderColor: '#dddfeb',
                        borderWidth: 1,
                        xPadding: 15,
                        yPadding: 15,
                        displayColors: false,
                        caretPadding: 10,
                    },
                    legend: {
                        display: false
                    },
                    cutoutPercentage: 80,

                },
            });
            // Đặt các phần tử HTML bạn muốn cập nhật vào các biến
            var hoaDonCho = document.getElementById('hoaDonCho');
            var hoaDonchoXacNhan = document.getElementById('hoaDonchoXacNhan');
            var hoaDonChoGiao = document.getElementById('hoaDonChoGiao');
            var hoaDonDangGiao = document.getElementById('hoaDonDangGiao');
            var hoaDonThanhCong = document.getElementById('hoaDonThanhCong');
            var hoaDonHuy = document.getElementById('hoaDonHuy');

            // Duyệt qua mảng dữ liệu và cập nhật thông tin tương ứng
            for (var i = 0; i < data.length; i++) {
                var status = data[i][0];
                var count = data[i][1];

                switch (status) {
                    case 1:
                        hoaDonThanhCong.innerText = count;
                        break;
                    case 2:
                        hoaDonThanhCong.innerText = count;
                        break;
                    case 3:
                        hoaDonThanhCong.innerText = count;
                        break;
                    case 4:
                        hoaDonThanhCong.innerText = count;
                        break;
                    case 5:
                        hoaDonThanhCong.innerText = count;
                        break;
                    case 6:
                        hoaDonHuy.innerText = count;
                        break;
                    default:
                        // Xử lý trạng thái mặc định (nếu có)
                        break;
                }
            }
        });
}
// Hàm để cập nhật thông tin số lượng hóa đơn trên trang HTML

callApiAndUpdatePieChart()