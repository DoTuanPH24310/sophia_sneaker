// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function number_format(number, decimals, dec_point, thousands_sep) {
    // *     example: number_format(1234.56, 2, ',', ' ');
    // *     return: '1 234,56'
    number = (number + '').replace(',', '').replace(' ', '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.round(n * k) / k;
        };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}
//check năm
function mapMonthToLabel(item) {
    const value = item[0];

    // Kiểm tra nếu giá trị là một số và có đúng 4 chữ số
    if (typeof value === 'number' && value.toString().length === 4) {
        return 'Năm ' + value;
    } else if (typeof value === 'number' && value >= 1 && value <= 12) {
        return 'Tháng ' + value;
    } else {
        return 'Không xác định';
    }
}
// định dạngh tiền
function formatCurrency(number) {
    return new Intl.NumberFormat('vi-VN').format(number);
}
// Area Chart Example
var areaChart = document.getElementById("myAreaChart");
// Lấy các phần tử DOM của ô ngày bắt đầu và kết thúc
var ngayBatDauInput = document.getElementById("NBDHoaDon");
var ngayKetThucInput = document.getElementById("NKTHoaDon");
var unit = document.getElementById("unit");

// Sự kiện khi giá trị của ô ngày bắt đầu thay đổi
ngayBatDauInput.addEventListener("input", function () {
    callApiAndUpdateChart();
    callApiAndUpdatePieChart();
    callApiAndUpdateBarChart();
});

// Sự kiện khi giá trị của ô ngày kết thúc thay đổi
ngayKetThucInput.addEventListener("input", function () {
    callApiAndUpdateChart();
    callApiAndUpdatePieChart();
    callApiAndUpdateBarChart();
});

unit.addEventListener("select", function () {
    callApiAndUpdateChart();
    callApiAndUpdatePieChart();
    callApiAndUpdateBarChart();
});

// Hàm gọi API và cập nhật biểu đồ
function callApiAndUpdateChart() {
    // Lấy giá trị từ ô ngày bắt đầu và ngày kết thúc
    var ngayBatDau = ngayBatDauInput.value;
    var ngayKetThuc = ngayKetThucInput.value;
    var unit = ngayKetThucInput.value;

    // Tạo URL với thông tin ngày
    var url = "http://localhost:8080/api/chart/revenue?ngayBatDau=" + encodeURIComponent(ngayBatDau) + "&ngayKetThuc=" + encodeURIComponent(ngayKetThuc);
    console.log('sá'+url)
    // Gửi request đến URL tương ứng
    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Cập nhật biểu đồ với dữ liệu mới
            updateChart(data);
        })
        .catch(error => console.error('Error:', error));

    var url1 = "http://localhost:8080/api/chart/thong-ke?ngayBatDau=" + encodeURIComponent(ngayBatDau) + "&ngayKetThuc=" + encodeURIComponent(ngayKetThuc);
    // Gửi request đến URL tương ứng
    fetch(url1)
        .then(response => response.json())
        .then(data => {
            // Cập nhật biểu đồ với dữ liệu mới
            $("#doanhThu").text(formatCurrency(data.doanhThu));
            $("#sanPhamDaBan").text(data.soSanPham);
            $("#soHoaDon").text(data.soHoaDon);
        })
        .catch(error => console.error('Error:', error));
}

// Hàm cập nhật biểu đồ với dữ liệu mới
function updateChart(data) {
    // Tạo hoặc cập nhật biểu đồ
    if (typeof myLineChart === 'undefined') {
        // Nếu biểu đồ chưa được tạo, tạo mới
        myLineChart = new Chart(areaChart, {
            type: 'line',
            data: {
                labels: data.map(item => mapMonthToLabel(item)),
                datasets: [
                    {
                        label: "Doanh thu ",
                        lineTension: 0.3,
                        backgroundColor: "rgba(78, 115, 223, 0.05)",
                        borderColor: "rgba(78, 115, 223, 1)",
                        pointRadius: 3,
                        pointBackgroundColor: "rgba(78, 115, 223, 1)",
                        pointBorderColor: "rgba(78, 115, 223, 1)",
                        pointHoverRadius: 1,
                        pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                        pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                        pointHitRadius: 10,
                        pointBorderWidth: 2,
                        data: data.map(item => item[1]),
                    },
                    {
                        label: "So với cùng kỳ",
                        lineTension: 0.3,
                        backgroundColor: "rgba(223, 78, 78, 0.05)",
                        borderColor: "rgba(223, 78, 78, 1)",
                        pointRadius: 3,
                        pointBackgroundColor: "rgba(223, 78, 78, 1)",
                        pointBorderColor: "rgba(223, 78, 78, 1)",
                        pointHoverRadius: 1,
                        pointHoverBackgroundColor: "rgba(223, 78, 78, 1)",
                        pointHoverBorderColor: "rgba(223, 78, 78, 1)",
                        pointHitRadius: 10,
                        pointBorderWidth: 2,
                        data: data.map(item => item[2]), // Giả sử giá trị của line thứ 2 nằm ở vị trí thứ 2 trong mảng data
                    }
                ],
            },
            options: {
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                    }
                },
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'date'
                        },
                        gridLines: {
                            display: false,
                            drawBorder: false
                        },
                        ticks: {
                            maxTicksLimit: 12
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            maxTicksLimit: 5,
                            padding: 10,
                            // Include a dollar sign in the ticks
                            callback: function (value, index, values) {
                                return number_format(value) + ' VNĐ';
                            }
                        },
                        gridLines: {
                            color: "rgb(234, 236, 244)",
                            zeroLineColor: "rgb(234, 236, 244)",
                            drawBorder: false,
                            borderDash: [2],
                            zeroLineBorderDash: [2]
                        }
                    }],
                },
                legend: {
                    display: true
                },
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    titleMarginBottom: 10,
                    titleFontColor: '#6e707e',
                    titleFontSize: 14,
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    intersect: false,
                    mode: 'index',
                    caretPadding: 10,
                    callbacks: {
                        label: function (tooltipItem, chart) {
                            var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                            return datasetLabel + ' ' + number_format(tooltipItem.yLabel) + ' VNĐ';
                        }
                    }
                }
            }
        });
    } else {
        // Nếu biểu đồ đã tồn tại, cập nhật dữ liệu
        myLineChart.data.labels = data.map(item => mapMonthToLabel(item));
        myLineChart.data.datasets[0].data = data.map(item => item[1]);
        myLineChart.update();
    }
}

// Gọi hàm lần đầu để hiển thị biểu đồ ban đầu
callApiAndUpdateChart();


