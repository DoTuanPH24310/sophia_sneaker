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
var myPieChart;  // Declare myPieChart outside the function

function callApiAndUpdatePieChart() {
    var ngayBatDauValue = ngayBatDauInput.value;
    var ngayKetThucValue = ngayKetThucInput.value;
    var url3 = "/api/chart/status?ngayBatDau=" + encodeURIComponent(ngayBatDauValue) + "&ngayKetThuc=" + encodeURIComponent(ngayKetThucValue);
    console.log('ngày' + url3);

    var pieChart = document.getElementById("myPieChart");

    // Fetch dữ liệu mới từ API
    fetch(url3)
        .then(response => response.json())
        .then(data => {
            const backgroundColorArray = ['#4e73df', '#1cc88a', '#f6ff00', '#ff6384', '#ff9f40', '#4bc0c0'];
            const hoverBackgroundColorArray = ['#2e59d9', '#17a673', '#987f07', '#e03350', '#e67f34', '#46a3a3'];

            // Destroy existing chart if it exists
            if (myPieChart) {
                myPieChart.destroy();
            }

            // Create a new Chart instance
            myPieChart = new Chart(pieChart, {
                type: 'doughnut',
                data: {
                    labels: data.map(item => mapStatusToLabel(item[0])),
                    datasets: [{
                        data: data.map(item => item[1]),
                        backgroundColor: backgroundColorArray,
                        hoverBackgroundColor: hoverBackgroundColorArray,
                        hoverBorderColor: hoverBackgroundColorArray,
                    }],
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
                        display: true
                    },
                    cutoutPercentage: 80,
                },
            });
        });
}

// Hàm để cập nhật thông tin số lượng hóa đơn trên trang HTML
callApiAndUpdatePieChart();
