// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var pieChart = document.getElementById("myPieChart");
fetch("/api/chart/product?nam=2023")
    .then(response => response.json())
    .then(data => {
        // Dãy màu cho backgroundColor
        const backgroundColorArray = ['#4e73df', '#1cc88a', '#36b9cc'];

// Dãy màu cho hoverBackgroundColor
        const hoverBackgroundColorArray = ['#2e59d9', '#17a673', '#2c9faf'];

// Tạo thêm màu sắc nếu cần
        for (let i = 0; i < 7; i++) {
            backgroundColorArray.push(getRandomColor());
            hoverBackgroundColorArray.push(getRandomColor());
        }

// Hàm để sinh màu sắc ngẫu nhiên
        function getRandomColor() {
            const letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        }

// Sử dụng mảng màu sắc trong mã của bạn
        const chartData = {
            datasets: [{
                backgroundColor: backgroundColorArray,
                hoverBackgroundColor: hoverBackgroundColorArray,
                // Các thuộc tính khác của dataset
            }],
            // Các thuộc tính khác của biểu đồ
        };

        var myPieChart = new Chart(pieChart, {
            type: 'doughnut',
            data: {
                labels: data.map(item => item[0]),
                datasets: [{
                    data: data.map(item => item[1]),
                    backgroundColor: backgroundColorArray,
                    hoverBackgroundColor: hoverBackgroundColorArray,
                    hoverBorderColor: "rgba(234, 236, 244, 1)",
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
                    display: false
                },
                cutoutPercentage: 80,
            },
        });
    });
