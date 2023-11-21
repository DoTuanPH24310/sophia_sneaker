package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    KichCoService kichCoService;
    @Autowired
    AnhService anhService;

    // import excel
    @GetMapping("/exportToExcel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {

        List<ChiTietGiay> chiTietGiayList = chiTietGiayService.getAll();

        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Tạo hàng đầu tiên để đặt tên cho các cột
        Row headerRow = sheet.createRow(0);
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
        headerRow.createCell(12).setCellValue("Ngày Tạo");
        headerRow.createCell(13).setCellValue("Ngày Sửa");
        headerRow.createCell(14).setCellValue("Người Tạo");
        headerRow.createCell(15).setCellValue("Người sửa");

        // Lặp qua danh sách và ghi vào sheet
        int rowIndex = 1; // Bắt đầu từ hàng thứ 2 để tránh ghi đè hàng đầu tiên
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

            Cell cell12 = row.createCell(12);
            cell12.setCellValue(chiTietGiay.getNgayTao());

            Cell cell13 = row.createCell(13);
            cell13.setCellValue(chiTietGiay.getNgaySua());

            Cell cell14 = row.createCell(14);
            cell14.setCellValue(chiTietGiay.getNguoiTao());

            Cell cell15 = row.createCell(15);
            cell15.setCellValue(chiTietGiay.getNguoiSua());

            Cell cell16 = row.createCell(16);
            cell16.setCellValue(chiTietGiay.getNguoiTao());
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
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/importFromExcel", method = RequestMethod.POST)
    public String importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Đọc file Excel từ MultipartFile
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Giả sử dữ liệu ở sheet đầu tiên

            // Lặp qua từng hàng trong sheet (bắt đầu từ hàng thứ 1, bỏ qua hàng tiêu đề)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // Tạo đối tượng ChiTietGiay mới
                ChiTietGiay chiTietGiay = new ChiTietGiay();
                chiTietGiay.setMa( row.getCell(0).getStringCellValue());
                chiTietGiay.setTen(row.getCell(1).getStringCellValue());
                chiTietGiay.setGiay(giayService.findByTen(row.getCell(2).getStringCellValue()));
                chiTietGiay.setDeGiay(deGiayService.findByTen(row.getCell(3).getStringCellValue()));
                chiTietGiay.setHang(hangService.findByTen(row.getCell(4).getStringCellValue()));
                chiTietGiay.setKichCo(kichCoService.findByTen(row.getCell(5).getStringCellValue()));
                chiTietGiay.setLoaiGiay(loaiGiayService.findByTen(row.getCell(6).getStringCellValue()));
                chiTietGiay.setMauSac(mauSacService.findByTen(row.getCell(7).getStringCellValue()));
                chiTietGiay.setMoTa( row.getCell(8).getStringCellValue());
                chiTietGiay.setGia(Double.valueOf(row.getCell(9).getStringCellValue()));
                chiTietGiay.setTrangThai(Integer.valueOf(row.getCell(10).getStringCellValue()));
                chiTietGiay.setSoLuong(Integer.valueOf(row.getCell(11).getStringCellValue()));
                chiTietGiay.setNgayTao(LocalDateTime.parse(row.getCell(12).getStringCellValue()));
                chiTietGiay.setNgaySua(LocalDateTime.parse(row.getCell(13).getStringCellValue()));
                chiTietGiay.setNguoiTao( row.getCell(14).getStringCellValue());
                chiTietGiay.setNguoiSua( row.getCell(15).getStringCellValue());

                // Gọi service để thêm sản phẩm mới vào cơ sở dữ liệu
                chiTietGiayService.save(chiTietGiay);
                System.out.println("ăn");
            }

            // Đóng workbook
            workbook.close();

            return "redirect:/admin/chi-tiet-giay";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/chi-tiet-giay";
        }
    }


    private void createComboBoxForGiay(Workbook workbook, Sheet sheet, Cell cell, List<Giay> giayList, String defaultValue) {
        DataValidationHelper helper = sheet.getDataValidationHelper();

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = giayList.stream().map(Giay::getTen).toArray(String[]::new);

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

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = deGiayList.stream().map(DeGiay::getTen).toArray(String[]::new);

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

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = hangList.stream().map(Hang::getTen).toArray(String[]::new);

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

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = kichCoList.stream().map(KichCo::getTen).toArray(String[]::new);

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

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = loaiGiayList.stream().map(LoaiGiay::getTen).toArray(String[]::new);

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

        // Chuyển danh sách giayList thành mảng String
        String[] giayNames = mauSacList.stream().map(MauSac::getTen).toArray(String[]::new);

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
}
