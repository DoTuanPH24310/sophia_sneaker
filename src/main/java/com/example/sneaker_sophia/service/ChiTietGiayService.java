package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.ChiTietGiayDTO;
import com.example.sneaker_sophia.dto.DTO_API_CTG;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.*;

@Service
public class ChiTietGiayService {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public static int checkCTG = 0;

    List<ChiTietGiay> findAllByIdGiay(List<String> listId, UUID idVC) {
        return chiTietGiayRepository.findAllByIdGiay(convertStringListToUUIDList(listId),idVC);
    }

    public List<ChiTietGiay> getCTGByG(List<UUID> list, UUID idVC) {
        return chiTietGiayRepository.findAllByIdGiay(list,idVC);
    }


    List<ChiTietGiay> findAllById(List<UUID> listIDG) {
        return chiTietGiayRepository.findAllById(listIDG);
    }


    public List<ChiTietGiay> findAllByTrangThaiEquals(int trangThai) {
        return chiTietGiayRepository.findAllAndOrder(trangThai);
    }

    public List<String> findIdByIdGiay(List<String> listIDGiay) {
        return chiTietGiayRepository.findIdByIdGiay(convertStringListToUUIDList(listIDGiay));
    }

    public String findMaByIdCTG(UUID id) {
        return chiTietGiayRepository.findMaByIdCTG(id);
    }


    public List<String> checkedCTG(List<String> listIDCTG, Model model, List<String> listIDG) {
        List<String> temp = listIDCTG;
        // Khi chọn All lần đầu tiên
        if (listIDCTG.contains("AllCTG") && checkCTG == 0) {
            checkCTG = 1;
            model.addAttribute("checkAllCTG", true);
            return this.findIdByIdGiay(listIDG);
        }

        // Khi đã chọn All nhưng lại không chọn nữa
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") == false && listIDCTG.size() <= findAllByTrangThaiEquals(0).size()) {
            System.out.println("2");
            checkCTG = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() >= listIDCTG.size()) {
            checkCTG = 0;
            model.addAttribute("checkAllCTG", false);
            listIDCTG.remove("AllCTG");
            return listIDCTG;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (!listIDCTG.contains("false") && listIDCTG.size() == chiTietGiayRepository.findIdByIdGiay(convertStringListToUUIDList(listIDG)).size() && checkCTG == 0) {
            System.out.println("nam oi la nam");
            checkCTG = 1;
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }

//        Khi đã chọn all nhưng không bỏ chọn giá trị nào khác(khi gọi lại server)
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() < listIDCTG.size()) {
            listIDCTG.remove("AllCTG");
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }
        return temp;
    }

    public    List<UUID> convertStringListToUUIDList(List<String> stringList) {
        List<UUID> uuidList = new ArrayList<>();
        for (String str : stringList) {
            try {
                UUID uuid = UUID.fromString(str);
                uuidList.add(uuid);
            } catch (IllegalArgumentException e) {
            }
        }
        return uuidList;
    }

    public static final int PRODUCT_DETAIL_PER_PAGE = 10;

    public List<ChiTietGiay> getAll() {
        return chiTietGiayRepository.findAllAndOrder(0);
    }
    public List<ChiTietGiay> getAllExcel() {
        return chiTietGiayRepository.findAllAndOrderExcel(-1);
    }
    public ChiTietGiay save(ChiTietGiay chiTietGiay) {
        chiTietGiayRepository.save(chiTietGiay);
        return chiTietGiay;
    }

    public ChiTietGiay getOne(UUID id) {
        return chiTietGiayRepository.findById(id).get();
    }

    public void delete(UUID id) {
        chiTietGiayRepository.updateTrangThaiTo1ById(id);
    }

    public Page<ChiTietGiay> findAll(Pageable pageable) {
        return chiTietGiayRepository.findAll(pageable);
    }

    public Page<ChiTietGiay> filterCombobox(int pageNum, String sortField, String sortDir, Giay giay, DeGiay deGiay, Hang hang, LoaiGiay loaiGiay, MauSac mauSac, KichCo kichCo, String trangThai, Double giaMin, Double giaMax,String keyWord) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);
        return chiTietGiayRepository.findChiTietGiayByMultipleParams(giay, deGiay, hang, loaiGiay, mauSac, kichCo, trangThai, giaMin, giaMax,keyWord,pageable);
    }

    public List<ChiTietGiay> findChiTietGiaysById(UUID uuid) {
        return chiTietGiayRepository.findChiTietGiaysById(uuid);
    }


    public Page<ChiTietGiay> filterChiTietGiay(
            List<String> tenGiay,
            List<String> tenKichCo,
            List<String> tenDeGiay,
            List<String> tenHang,
            List<String> tenLoaiGiay,
            List<String> tenMauSac,
            List<String> minPriceRanges,
            int page,
            int pageSize,
            String sortField
    ) {
        Specification<ChiTietGiay> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tenGiay != null && !tenGiay.isEmpty()) {
                predicates.add(root.get("giay").get("ten").in(tenGiay));
            }
            if (tenKichCo != null && !tenKichCo.isEmpty()) {
                predicates.add(root.get("kichCo").get("ten").in(tenKichCo));
            }
            if (tenDeGiay != null && !tenDeGiay.isEmpty()) {
                predicates.add(root.get("deGiay").get("ten").in(tenDeGiay));
            }
            if (tenHang != null && !tenHang.isEmpty()) {
                predicates.add(root.get("hang").get("ten").in(tenHang));
            }
            if (tenLoaiGiay != null && !tenLoaiGiay.isEmpty()) {
                predicates.add(root.get("loaiGiay").get("ten").in(tenLoaiGiay));
            }
            if (tenMauSac != null && !tenMauSac.isEmpty()) {
                predicates.add(root.get("mauSac").get("ten").in(tenMauSac));
            }
            if (minPriceRanges != null && !minPriceRanges.isEmpty()) {
                List<Predicate> priceRangePredicates = new ArrayList<>();

                for (String priceRange : minPriceRanges) {
                    double minPrice = 0.0;
                    double maxPrice = Double.MAX_VALUE;

                    switch (priceRange) {
                        case "0-1":
                            maxPrice = 1000000.0;
                            break;
                        case "1-1.5":
                            minPrice = 1000000.0;
                            maxPrice = 1500000.0;
                            break;
                        case "1.5-2":
                            minPrice = 1500000.0;
                            maxPrice = 2000000.0;
                            break;
                        case "2-2.5":
                            minPrice = 2000000.0;
                            maxPrice = 2500000.0;
                            break;
                        case "2.5-3":
                            minPrice = 2500000.0;
                            maxPrice = 3000000.0;
                            break;
                        case "3+":
                            minPrice = 3000000.0;
                            break;
                        default:
                            // Xử lý các khoảng giá khác nếu cần
                            break;
                    }
                    // Tạo một Predicate cho mỗi khoảng giá
                    Predicate pricePredicate = cb.between(root.get("gia"), minPrice, maxPrice);
                    priceRangePredicates.add(pricePredicate);
                }

                // Tạo một Predicate gồm logic OR cho tất cả các khoảng giá
                Predicate priceRangeOrPredicate = cb.or(priceRangePredicates.toArray(new Predicate[0]));

                // Thêm Predicate logic OR vào danh sách các Predicate
                predicates.add(priceRangeOrPredicate);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Sử dụng ChiTietGiayRepository và PageRequest để lấy kết quả phân trang
        Sort sort = Sort.unsorted();

        if ("lowPrice".equals(sortField)) {
            sort = Sort.by("gia").ascending();
        } else if ("hightPrice".equals(sortField)) {
            sort = Sort.by("gia").descending();
        } else if ("ten".equals(sortField)) {
            sort = Sort.by("ten").ascending();
        } else if ("newest".equals(sortField)) {
            sort = Sort.by("ngaySua").descending();
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<ChiTietGiay> pageResult = chiTietGiayRepository.findAll(spec, pageable);

        return pageResult;
    }

    public UUID findChiTietGiayIdByAnhId(String id) {
        return chiTietGiayRepository.findChiTietGiayIdByAnhId(id);
    }

    // cuongdv

    //28-10 hoanghh
    public List<ChiTietGiay> findChiTietGiayByMultipleParamsAPI(DTO_API_CTG ctg) {
        UUID idGiay = !ctg.getIdGiay().equals("null") ? UUID.fromString(ctg.getIdGiay()) : null;
        UUID idDeGiay = !ctg.getIdDe().equals("null") ? UUID.fromString(ctg.getIdDe()) : null;
        UUID idHang = !ctg.getIdHang().equals("null") ? UUID.fromString(ctg.getIdHang()) : null;
        UUID idLoaiGiay = !ctg.getIdLoaigiay().equals("null") ? UUID.fromString(ctg.getIdLoaigiay()) : null;
        UUID idMau = !ctg.getIdMau().equals("null") ? UUID.fromString(ctg.getIdMau()) : null;
        UUID idKichCo = !ctg.getIdSize().equals("null") ? UUID.fromString(ctg.getIdSize()) : null;
        String textSearch = ctg.getTextSearch() == null || ctg.getTextSearch().trim().length() == 0 ? null : "%" + ctg.getTextSearch() + "%";
        return chiTietGiayRepository.findChiTietGiayByMultipleParamsAPI(idGiay, idDeGiay, idHang, idLoaiGiay, idMau, idKichCo, textSearch);
    }

    // 28-10 cuongdv
    public ChiTietGiay getChiTietGiayByIdctg(UUID idct) {
        return chiTietGiayRepository.findById(idct).orElse(null);
    }

    // 29-10 cuongdv
    public UUID getIdCTGByMa(String maCTG) {
        return chiTietGiayRepository.getIdCTGByMa(maCTG);
    }

    //    29/10 hoangnh
    public Integer findSoLuongTon(String ma) {
        return chiTietGiayRepository.findSoLuongTon(ma);
    }

    // 11- 11
    public ChiTietGiay getCTGByQrCode(String qrcode) {
        return chiTietGiayRepository.getChiTietGiayByQrCode(qrcode);
    }

    // 13-11
    public List<ChiTietGiay> getAllCTG() {
        return chiTietGiayRepository.findAll();
    }

    //    14/11
    public Integer findSoLuongTonByQrCode(String qr) {
        return chiTietGiayRepository.findSoLuongTonByQrCode(qr);
    }

    // 17/11

    public Integer tongKMByIdctg(UUID idctg) {
        return chiTietGiayRepository.tongKMByIdctg(idctg);
    }

    public ChiTietGiay findByMa(String ma) {
        return chiTietGiayRepository.findByMa(ma);
    }

    public boolean validate(ChiTietGiayDTO chiTietGiay, MultipartFile[] imageFiles, Model model) {
        int check = 0;
        String errMa = null, errQr = null, errAnhs = "";

        // Kiểm tra mã đã được sử dụng hay chưa
        if (chiTietGiayRepository.getIdCTGByMa(chiTietGiay.getMa()) != null) {
            errMa = "Mã đã được sử dụng";
            check++;
        }

        // Kiểm tra QR code đã được sử dụng hay chưa
        if (chiTietGiayRepository.getChiTietGiayByQrCode(chiTietGiay.getQrCode()) != null) {
            errQr = "QR code đã được sử dụng";
            check++;
        }

        // Kiểm tra số lượng ảnh
        int numberOfImages = imageFiles.length;
        if (numberOfImages == 0) {
            errAnhs = "Không có ảnh nào được tải lên.";
            check++;
        } else if (numberOfImages > 3) {
            errAnhs = "Số lượng ảnh không được vượt quá 3.";
            check++;
        } else if (numberOfImages < 2) {
            errAnhs = "Số lượng ảnh không đủ. Yêu cầu tối thiểu 2 ảnh.";
            check++;
        }

        // Kiểm tra định dạng ảnh
        for (MultipartFile imageFile : imageFiles) {
            if (!isValidImageFormat(imageFile)) {
                errAnhs = "Định dạng ảnh không hợp lệ. Chấp nhận chỉ JPG,JPEG, PNG, và GIF.";
                check++;
                break; // Ngưng kiểm tra nếu có ít nhất một ảnh không hợp lệ
            }
        }

        // Thêm thông báo lỗi vào model
        model.addAttribute("errMa", errMa);
        model.addAttribute("errQr", errQr);
        model.addAttribute("errAnhs", errAnhs);

        return check == 0;
    }

    // Hàm kiểm tra định dạng ảnh
    private boolean isValidImageFormat(MultipartFile file) {
        String[] allowedFormats = {"jpg", "jpeg", "png", "gif"};

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : "";

        for (String format : allowedFormats) {
            if (format.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }

        return false;
    }


    public boolean validateUpdate(ChiTietGiay chiTietGiay, Model model) {
        int check = 0;
        String errQr = null;

        // Kiểm tra xem mã QR code đã được sử dụng hay chưa
        ChiTietGiay existingChiTietGiay = chiTietGiayRepository.getChiTietGiayByQrCode(chiTietGiay.getQrCode());
        if (existingChiTietGiay != null && !existingChiTietGiay.getId().equals(chiTietGiay.getId())) {
            // Nếu mã QR code đã được sử dụng và không phải là chính nó, thì thông báo lỗi
            errQr = "QR code đã được sử dụng";
            check++;
        }


        // Thêm thông báo lỗi vào model
        model.addAttribute("errQr", errQr);

        return check == 0;
    }

    public boolean validateUpdateAnh(MultipartFile[] imageFiles, Model model) {
        int check = 0;
        String errAnhs="";

        // Kiểm tra số lượng ảnh
        int numberOfImages = imageFiles.length;
        if (numberOfImages == 0) {
            errAnhs = "Không có ảnh nào được tải lên.";
            check++;
        } else if (numberOfImages > 3) {
            errAnhs = "Số lượng ảnh không được vượt quá 3.";
            check++;
        } else if (numberOfImages < 2) {
            errAnhs = "Số lượng ảnh không đủ. Yêu cầu tối thiểu 2 ảnh.";
            check++;
        }

        // Kiểm tra định dạng ảnh
        for (MultipartFile imageFile : imageFiles) {
            if (!isValidImageFormat(imageFile)) {
                errAnhs = "Định dạng ảnh không hợp lệ. Chấp nhận chỉ JPG,JPEG, PNG, và GIF.";
                check++;
                break; // Ngưng kiểm tra nếu có ít nhất một ảnh không hợp lệ
            }
        }
        model.addAttribute("errAnhs", errAnhs);

        return check == 0;
    }
    // thong ke
    public List<Object[]> getConcatenatedInfoAndSoLuongBySoLuong(int soLuong){
        return chiTietGiayRepository.getConcatenatedInfoAndSoLuongBySoLuong(soLuong);
    }

    // lấy chitietgiay có sz khác nhau
    public List<ChiTietGiay> findSimilarChiTietGiay(Giay giay,DeGiay deGiay,Hang hang,LoaiGiay loaiGiay,MauSac mauSac,UUID kichCo){
        return chiTietGiayRepository.findSimilarChiTietGiay(giay,deGiay,hang,loaiGiay,mauSac,kichCo);
    }
    public List<ChiTietGiay> findSimilarChiTietGiayByMauSac(Giay giay,DeGiay deGiay,Hang hang,LoaiGiay loaiGiay,KichCo kichCo,UUID mauSac){
        return chiTietGiayRepository.findSimilarChiTietGiayByMauSac(giay,deGiay,hang,loaiGiay,kichCo,mauSac);
    }
    public List<KichCo> findSimilarSizeChiTietGiay(Giay giay,DeGiay deGiay,Hang hang,LoaiGiay loaiGiay,MauSac mauSac){
        return chiTietGiayRepository.findSimilarSizeChiTietGiay(giay,deGiay,hang,loaiGiay,mauSac);
    }
    public List<MauSac> findSimilarMauSacChiTietGiay(Giay giay,DeGiay deGiay,Hang hang,LoaiGiay loaiGiay,KichCo kichCo){
        return chiTietGiayRepository.findSimilarMauSacChiTietGiay(giay,deGiay,hang,loaiGiay,kichCo);
    }
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int QR_CODE_LENGTH = 16;
    private static final Random RANDOM = new SecureRandom();
    private static final Set<String> generatedCodes = new HashSet<>();

    public static String generateRandomQRCode() {
        String qrCode;
        do {
            qrCode = generateRandomCode();
        } while (!generatedCodes.add(qrCode));
        return qrCode;
    }

    private static String generateRandomCode() {
        StringBuilder qrCode = new StringBuilder(QR_CODE_LENGTH);

        for (int i = 0; i < QR_CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }
}

