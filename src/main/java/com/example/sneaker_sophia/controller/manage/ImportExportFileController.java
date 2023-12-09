package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
import com.example.sneaker_sophia.validate.AlertInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ImportExportFileController {
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @Autowired
    GiayService giayService;
    @Autowired
    HangService hangService;
    @Autowired
    DeGiayService deGiayService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @Autowired
    KichCoService2 kichCoService2;
    @Autowired
    KichCoService kichCoService;
    @Autowired
    AnhService anhService;
    @Autowired
    private AlertInfo alertInfo;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int QR_CODE_LENGTH = 16;

    public static String generateRandomQRCode() {
        Random random = new SecureRandom();
        StringBuilder qrCode = new StringBuilder(QR_CODE_LENGTH);

        for (int i = 0; i < QR_CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }

    // import excel
    @GetMapping("/exportToExcel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {

        List<ChiTietGiay> chiTietGiayList = chiTietGiayService.getAll();

        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        // Cài lưu ý triong sheet
        // Tạo một CellStyle cho văn bản màu đỏ và nghiêng
        CellStyle redItalicStyle = workbook.createCellStyle();
        Font redItalicFont = workbook.createFont();
        redItalicFont.setColor(IndexedColors.RED.getIndex());
        redItalicFont.setItalic(true);
        redItalicStyle.setFont(redItalicFont);

        // Dòng 0
        Row luuY1 = sheet.createRow(0);
        Cell cellluuY1 = luuY1.createCell(0);
        cellluuY1.setCellValue("Lưu ý: Các mã chỉ được tồn tại 1 lần và không trùng lặp," +
                " nếu trùng lặp mã sẽ thêm số lượng cho sản phẩm mà không thay đổi bất kì thuộc tính nào của sản phẩm");
        cellluuY1.setCellStyle(redItalicStyle);

        // Dòng 1
        Row luuY2 = sheet.createRow(1);
        Cell cellluuY2 = luuY2.createCell(0);
        cellluuY2.setCellValue("     Các thuộc tính của sản phẩm không được bỏ trống trừ mô tả và QR code nếu bỏ trống QR code sẽ được tạo ngẫu nhiên, thêm ít nhất 2 ảnh " +
                "nếu ảnh để trống sẽ tự động thêm ảnh mặc định.");
        cellluuY2.setCellStyle(redItalicStyle);

        // Dòng 2
        Row luuY3 = sheet.createRow(2);
        Cell cellluuY3 = luuY3.createCell(0);
        cellluuY3.setCellValue("     Giá và số lượng phải lơn hơn 0, Số lượng phải bé hơn 10.000, và giá thấp hơn 10 tỉ");
        cellluuY3.setCellStyle(redItalicStyle);
        // Tạo hàng đầu tiên để đặt tên cho các cột
        Row headerRow = sheet.createRow(3);
        headerRow.createCell(0).setCellValue("Mã");
        headerRow.createCell(1).setCellValue("Tên");
        headerRow.createCell(2).setCellValue("Giày");
        headerRow.createCell(3).setCellValue("Đế Giày");
        headerRow.createCell(4).setCellValue("Hãng");
        headerRow.createCell(5).setCellValue("Kích Cỡ");
        headerRow.createCell(6).setCellValue("Loại Giày");
        headerRow.createCell(7).setCellValue("Màu Sắc");
        headerRow.createCell(8).setCellValue("Mô Tả");
        headerRow.createCell(9).setCellValue("Giá");
        headerRow.createCell(10).setCellValue("Số Lượng");
        headerRow.createCell(11).setCellValue("Trạng Thái");
        headerRow.createCell(12).setCellValue("QR code");
        headerRow.createCell(13).setCellValue("Ảnh 1");
        headerRow.createCell(14).setCellValue("Ảnh 2");
        headerRow.createCell(15).setCellValue("Ảnh 3");

        // Lặp qua danh sách và ghi vào sheet
        int rowIndex = 4; // Bắt đầu từ hàng thứ 2 để tránh ghi đè hàng đầu tiên
        for (ChiTietGiay chiTietGiay : chiTietGiayList) {
            Row row = sheet.createRow(rowIndex++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(chiTietGiay.getMa());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(chiTietGiay.getTen());

            // ComboBox cho Giay
            Cell cell2 = row.createCell(2);
            createComboBoxForGiay(workbook, sheet, cell2, giayService.getAll(),chiTietGiay.getGiay().getTen());

            Cell cell3 = row.createCell(3);
            createComboBoxForDeGiay(workbook, sheet, cell3, deGiayService.getAll(),chiTietGiay.getDeGiay().getTen());

            Cell cell4 = row.createCell(4);
            createComboBoxForHang(workbook, sheet, cell4, hangService.getAll(),chiTietGiay.getHang().getTen());

            Cell cell5 = row.createCell(5);
            createComboBoxForKichCo(workbook, sheet, cell5, kichCoService.getAll(),chiTietGiay.getKichCo().getTen());

            Cell cell6 = row.createCell(6);
            createComboBoxForLoaiGiay(workbook, sheet, cell6, loaiGiayService.getAll(),chiTietGiay.getLoaiGiay().getTen());

            Cell cell7 = row.createCell(7);
            createComboBoxForMauSac(workbook, sheet, cell7, mauSacService.getAll(),chiTietGiay.getMauSac().getTen());

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(chiTietGiay.getMoTa());

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(chiTietGiay.getGia());

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(chiTietGiay.getSoLuong());

            Cell cell11= row.createCell(11);
            cell11.setCellValue(chiTietGiay.getTrangThai());

            Cell cell12= row.createCell(12);
            cell12.setCellValue(chiTietGiay.getQrCode());

            List<Anh> anhs = anhService.anhsFindIdChitietGiay(chiTietGiay);

            for (int i = 0; i < anhs.size(); i++) {
                Anh anh = anhs.get(i);
                Cell cell = row.createCell(13 + i);
                cell.setCellValue(anh.getDuongDan());
            }
        }

        // Ghi workbook vào ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Chuẩn bị dữ liệu để trả về
        byte[] excelBytes = outputStream.toByteArray();

        // Tạo định dạng ngày giờ cho tên file
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = dateFormat.format(new Date());

        // Tạo tên file với ngày giờ phút giây
        String fileName = "exported_chiTietGiay_" + formattedDate + ".xlsx";

        // Thiết lập HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", fileName);

        // Trả về ResponseEntity
        alertInfo.alert("successTaiQuay", "Xuất file excel thành công");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/importFromExcel", method = RequestMethod.POST)
    public ResponseEntity<String> importFromExcel(@RequestParam("file") MultipartFile file) {
        // Set để theo dõi các mã đã xuất hiện
        Set<String> existingMaSet = new HashSet<>();
        Set<String> existingQRCodeSet = new HashSet<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 4; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                String ma = getStringValue(row.getCell(0));

                // Kiểm tra xem mã đã xuất hiện chưa
                if (existingMaSet.contains(ma)) {
                    // Nếu đã tồn tại, xử lý lỗi hoặc thông báo mà bạn muốn
                    return ResponseEntity.badRequest().body("Lỗi: Mã đã xuất hiện trước đó - " + ma);
                }

                existingMaSet.add(ma);

                // Kiểm tra điều kiện số lượng và giá trước khi truy cập vào đối tượng
                int importedSoLuong = getIntegerValue(row.getCell(10));
                double importedGia = getDoubleValue(row.getCell(9));
                String qrCode = getStringValue(row.getCell(12));

                if (importedGia <= 0 || importedSoLuong <= 0 || importedGia >= 1000000000 || importedSoLuong >= 10000) {
                    return ResponseEntity.badRequest().body("Lỗi: Giá hoặc số lượng không hợp lệ cho sản phẩm có mã: " + ma);
                }

                ChiTietGiay existingChiTietGiay = chiTietGiayService.findByMa(ma);
                ChiTietGiay existingChiTietGiayByQr = chiTietGiayService.getCTGByQrCode(qrCode);

                if (existingChiTietGiay != null) {
                    // Nếu mã đã tồn tại, cập nhật số lượng
                    existingChiTietGiay.setSoLuong(importedSoLuong);
                    existingChiTietGiay.setGia(importedGia);
                    if (qrCode.isEmpty() || qrCode == null) {
                        qrCode = chiTietGiayService.generateRandomQRCode();
                        existingChiTietGiay.setQrCode(qrCode);
                    }else if (!qrCode.equals(existingChiTietGiay.getQrCode())) {
                        // Kiểm tra nếu mã QR code đã tồn tại ở một sản phẩm khác
                        if (existingChiTietGiayByQr != null) {
                            return ResponseEntity.badRequest().body("Lỗi: Mã QR bị trùng lặp ở mã giày: " + existingChiTietGiay.getMa());
                        }
                        existingChiTietGiay.setQrCode(qrCode); // Cập nhật QR
                    }

                    chiTietGiayService.save(existingChiTietGiay);
            } else {
                    // Nếu mã chưa tồn tại, thêm mới
                    ChiTietGiay chiTietGiay = new ChiTietGiay();
                    chiTietGiay.setMa(getStringValue(row.getCell(0)));
                    chiTietGiay.setTen(getStringValue(row.getCell(1)));
                    chiTietGiay.setGiay(giayService.findByTen(getStringValue(row.getCell(2))));
                    chiTietGiay.setDeGiay(deGiayService.findByTen(getStringValue(row.getCell(3))));
                    chiTietGiay.setHang(hangService.findByTen(getStringValue(row.getCell(4))));
                    chiTietGiay.setKichCo(kichCoService2.findByTen(getStringValue(row.getCell(5))));
                    chiTietGiay.setLoaiGiay(loaiGiayService.findByTen(getStringValue(row.getCell(6))));
                    chiTietGiay.setMauSac(mauSacService.findByTen(getStringValue(row.getCell(7))));
                    chiTietGiay.setMoTa(getStringValue(row.getCell(8)));
                    chiTietGiay.setGia(getDoubleValue(row.getCell(9)));
                    chiTietGiay.setSoLuong(getIntegerValue(row.getCell(10)));
                    chiTietGiay.setTrangThai(getIntegerValue(row.getCell(11)));
                    //check QR
                    String generatedQRCode= getStringValue(row.getCell(12));
                    if (generatedQRCode.isEmpty() || generatedQRCode == null) {
                        generatedQRCode = chiTietGiayService.generateRandomQRCode();
                        chiTietGiay.setQrCode(generatedQRCode);
                    } else if (existingQRCodeSet.contains(generatedQRCode)) {
                        // Nếu QR code đã tồn tại, xử lý lỗi hoặc thông báo mà bạn muốn
                        return ResponseEntity.badRequest().body("Lỗi: Mã QR code đã xuất hiện trước đó - " + generatedQRCode);
                    }
                    existingQRCodeSet.add(generatedQRCode);

                    // Kiểm tra giá và số lượng
                    double importedGia1 = getDoubleValue(row.getCell(9));
                    int importedSoLuong1 = getIntegerValue(row.getCell(10));
                    if (importedGia1 <= 0 || importedSoLuong1 <= 0 || importedGia1 >= 1000000000 || importedSoLuong1 >= 10000) {
                        // Nếu giá hoặc số lượng không đúng, có thể thực hiện xử lý tùy ý
                        return ResponseEntity.badRequest().body("Lỗi: Giá hoặc số lượng không hợp lệ cho sản phẩm có mã: " + ma);
                    }

                    // Kiểm tra các trường khác không được trống
                    if (chiTietGiay.getMa() == null || chiTietGiay.getMa().isEmpty() ||
                            chiTietGiay.getTen() == null || chiTietGiay.getTen().isEmpty() ||
                            chiTietGiay.getGiay() == null || chiTietGiay.getDeGiay() == null ||
                            chiTietGiay.getHang() == null || chiTietGiay.getKichCo() == null ||
                            chiTietGiay.getLoaiGiay() == null || chiTietGiay.getMauSac() == null ||
                            chiTietGiay.getGia() == null || chiTietGiay.getSoLuong() == null ||
                            chiTietGiay.getTrangThai() == null) {
                        return ResponseEntity.badRequest().body("Lỗi: Một hoặc nhiều trường không được trống cho sản phẩm có mã: " + ma);
                    }

                    chiTietGiayService.save(chiTietGiay);

                    String linkAnh1 = getStringValue(row.getCell(13));
                    String linkAnh2 = getStringValue(row.getCell(14));
                    String linkAnh3 = getStringValue(row.getCell(15));

                    // Kiểm tra và lưu link ảnh vào bảng ảnh
                    saveLinkAnh(chiTietGiay, linkAnh1, 1);
                    saveLinkAnh(chiTietGiay, linkAnh2, 0);
                    saveLinkAnh(chiTietGiay, linkAnh3, 0);

                    // Kiểm tra ít nhất 2 ảnh
                    List<String> links = Arrays.asList(linkAnh1, linkAnh2, linkAnh3);
                    long numberOfNonNullLinks = links.stream().filter(link -> link != null && !link.isEmpty()).count();

                    if (numberOfNonNullLinks < 2) {
                        // Nếu ít hơn 2 ảnh, thay thế link ảnh rỗng bằng link mặc định
                        for (int i = 0; i < links.size(); i++) {
                            if (links.get(i) == null || links.get(i).isEmpty()) {
                                links.set(i, "http://res.cloudinary.com/deihbhsfj/image/upload/v1700796874/1c68cc62-7857-46b4-8ce2-b62ad2dc82d1.jpg");
                            }
                        }

                        // Lưu lại link ảnh mới vào bảng ảnh
                        saveLinkAnh(chiTietGiay, links.get(0), 1);
                        saveLinkAnh(chiTietGiay, links.get(1), 0);
                    }
                }
            }
            workbook.close();
            return ResponseEntity.ok("Tải lên thành công");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên danh sách");
        }
    }

    // Phương thức để lưu link ảnh vào bảng ảnh
    private void saveLinkAnh(ChiTietGiay chiTietGiay, String linkAnh, int anhChinh) {
        if (linkAnh != null && !linkAnh.isEmpty()) {
            Anh anh = new Anh();
            anh.setDuongDan(linkAnh);
            anh.setChiTietGiay(chiTietGiay);
            anh.setAnhChinh(anhChinh);
            anhService.save(anh);
        }
    }

    private void createComboBoxForGiay(Workbook workbook, Sheet sheet, Cell cell, List<Giay> giayList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Loại bỏ các giá trị trùng lặp từ deGiayList
        List<Giay> uniqueDeGiayList = giayList.stream()
                .distinct()
                .collect(Collectors.toList());

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = uniqueDeGiayList.stream().map(Giay::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }

    private void createComboBoxForDeGiay(Workbook workbook, Sheet sheet, Cell cell, List<DeGiay> deGiayList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Loại bỏ các giá trị trùng lặp từ deGiayList
        List<DeGiay> uniqueDeGiayList = deGiayList.stream()
                .distinct()
                .collect(Collectors.toList());

        // Chuyển uniqueDeGiayList thành mảng String chứa tên
        String[] giayNames = uniqueDeGiayList.stream().map(DeGiay::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }


    private void createComboBoxForHang(Workbook workbook, Sheet sheet, Cell cell, List<Hang> hangList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Loại bỏ các giá trị trùng lặp từ deGiayList
        List<Hang> uniqueDeGiayList = hangList.stream()
                .distinct()
                .collect(Collectors.toList());

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = uniqueDeGiayList.stream().map(Hang::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }

    private void createComboBoxForKichCo(Workbook workbook, Sheet sheet, Cell cell, List<KichCo> kichCoList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
// Loại bỏ các giá trị trùng lặp từ deGiayList
        List<KichCo> uniqueDeGiayList = kichCoList.stream()
                .distinct()
                .collect(Collectors.toList());
        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = uniqueDeGiayList.stream().map(KichCo::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }

    private void createComboBoxForLoaiGiay(Workbook workbook, Sheet sheet, Cell cell, List<LoaiGiay> loaiGiayList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Loại bỏ các giá trị trùng lặp từ deGiayList
        List<LoaiGiay> uniqueDeGiayList = loaiGiayList.stream()
                .distinct()
                .collect(Collectors.toList());

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = uniqueDeGiayList.stream().map(LoaiGiay::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }


    private void createComboBoxForMauSac(Workbook workbook, Sheet sheet, Cell cell, List<MauSac> mauSacList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Loại bỏ các giá trị trùng lặp từ deGiayList
        List<MauSac> uniqueDeGiayList = mauSacList.stream()
                .distinct()
                .collect(Collectors.toList());

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = uniqueDeGiayList.stream().map(MauSac::getTen).toArray(String[]::new);

        // Thêm giá trị mặc định vào danh sách giá trị
        String[] allValues = Arrays.copyOf(giayNames, giayNames.length + 1);
        allValues[allValues.length - 1] = defaultValue;

        DataValidationConstraint constraint = helper.createExplicitListConstraint(allValues);

        CellRangeAddressList addressList = new CellRangeAddressList(
                cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex()
        );

        DataValidation validation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);

        // Đặt giá trị mặc định cho ô ComboBox
        cell.setCellValue(defaultValue);
    }

    //kiểm tra validate
    private String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            long longValue = (long) numericValue;
            if (numericValue == longValue) {
                return String.valueOf(longValue);
            } else {
                return String.valueOf(numericValue);
            }
        } else {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue().trim();
        }
    }


    private Double getDoubleValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            // Chuyển đổi chuỗi thành số
            return Double.valueOf(cell.getStringCellValue());
        }
        return null;
    }

    private Integer getIntegerValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            // Chuyển đổi giá trị số thành số nguyên
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            // Chuyển đổi chuỗi thành số nguyên
            return Integer.valueOf(cell.getStringCellValue());
        }
        return null;
    }

}
