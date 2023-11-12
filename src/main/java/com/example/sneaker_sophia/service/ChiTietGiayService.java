package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChiTietGiayService {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public static int checkCTG = 0;

    List<ChiTietGiay> findAllByIdGiay(List<String> listId) {
        return chiTietGiayRepository.findAllByIdGiay(convertStringListToUUIDList(listId));
    }


    List<ChiTietGiay> findAllById(List<UUID> listIDG) {
        return chiTietGiayRepository.findAllById(listIDG);
    }


    public List<ChiTietGiay> findAllByTrangThaiEquals(int trangThai) {
        return chiTietGiayRepository.findAllByTrangThaiEquals(trangThai);
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
//            listIDCTG.remove("AllG");
            checkCTG = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() >= listIDCTG.size()) {
//            listIDCTG.remove("AllG");
            checkCTG = 0;
            model.addAttribute("checkAllCTG", false);
            listIDCTG.remove("AllCTG");
            return listIDCTG;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (listIDCTG.size() == findAllByTrangThaiEquals(0).size() && checkCTG == 0) {
            checkCTG = 1;
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }

//        Khi đã chọn all nhưng không bỏ chọn giá trị nào khác(khi gọi lại server)
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() < listIDCTG.size()) {
//            listIDCTG.remove("AllG");
            listIDCTG.remove("AllCTG");
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }
        return temp;
    }

    public static List<UUID> convertStringListToUUIDList(List<String> stringList) {
        List<UUID> uuidList = new ArrayList<>();
        for (String str : stringList) {
            try {
                UUID uuid = UUID.fromString(str);
                uuidList.add(uuid);
            } catch (IllegalArgumentException e) {
                // Xử lý ngoại lệ nếu chuỗi không hợp lệ
                // Ví dụ: có thể bỏ qua hoặc log thông báo lỗi.
            }
        }
        return uuidList;
    }

    public static final int PRODUCT_DETAIL_PER_PAGE = 10;
    public List<ChiTietGiay> getAll(){
        return chiTietGiayRepository.findAll();
    }
    public void save(ChiTietGiay chiTietGiay){
        chiTietGiayRepository.save(chiTietGiay);
    }
    public ChiTietGiay getOne(UUID id){
        return chiTietGiayRepository.findById(id).get();
    }
    public void delete(UUID id){
            chiTietGiayRepository.deleteById(id);
    }

    public Page<ChiTietGiay> findAll(Pageable pageable){
        return chiTietGiayRepository.findAll(pageable);
    }

    public Page<ChiTietGiay> listByPageAndProductName(int pageNum, String sortField, String sortDir, String keyword, String productName){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);

        if (StringUtils.isEmpty(productName) && StringUtils.isEmpty(keyword)) {
            return chiTietGiayRepository.findAll(pageable);
        } else if (StringUtils.isEmpty(productName)) {
            return chiTietGiayRepository.findByKeyword(keyword, pageable);
        } else if (StringUtils.isEmpty(keyword)) {
            return chiTietGiayRepository.findByGiay_TenContainingIgnoreCase(productName, pageable);
        } else {
            return chiTietGiayRepository.findByMaAndKeyWord(keyword, productName, pageable);
        }
    }

    public Page<ChiTietGiay> filterCombobox(int pageNum, String sortField, String sortDir,Giay giay, DeGiay deGiay, Hang hang, LoaiGiay loaiGiay, MauSac mauSac, KichCo kichCo,Double giaMin,Double giaMax){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);
        return chiTietGiayRepository.findChiTietGiayByMultipleParams(giay,deGiay,hang,loaiGiay,mauSac,kichCo,giaMin,giaMax,pageable);
    }

    public List<ChiTietGiay> findChiTietGiaysById(UUID uuid){
        return chiTietGiayRepository.findChiTietGiaysById(uuid);
    }

    public ChiTietGiay findChiTietGiayByIdKichCo(UUID uuid){
        return chiTietGiayRepository.findChiTietGiayByIdKichCo(uuid);
    }

    public List<ChiTietGiay> getChiTietGiaysByIdChiTietGiay(Giay giay, DeGiay deGiay, Hang hang, LoaiGiay loaiGiay, MauSac mauSac){
        return chiTietGiayRepository.getChiTietGiaysByIdChiTietGiay(giay,deGiay,hang,loaiGiay,mauSac);
    }

    public List<ChiTietGiay> filterChiTietGiay(
            List<String> tenGiay,
            List<String> tenKichCo,
            List<String> tenDeGiay,
            List<String> tenHang,
            List<String> tenLoaiGiay,
            List<String> tenMauSac,
            List<String> minPriceRanges
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ChiTietGiay> query = cb.createQuery(ChiTietGiay.class);
        Root<ChiTietGiay> root = query.from(ChiTietGiay.class);

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
        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ChiTietGiay> typedQuery = entityManager.createQuery(query);
        System.out.println("typedQuery ở đây"+typedQuery.getResultList());
        return typedQuery.getResultList();
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
            sort = JpaSort.unsafe(Sort.Direction.ASC, "CAST(gia AS DECIMAL)");
        } else if ("hightPrice".equals(sortField)) {
            sort = JpaSort.unsafe(Sort.Direction.DESC, "CAST(gia AS DECIMAL)");
        }
//        else if ("ten".equals(sortField)) {
//            sort = Sort.by("ten").ascending();
//        }
        else if ("newest".equals(sortField)) {
            sort = Sort.by("ngayTao").descending();
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<ChiTietGiay> pageResult = chiTietGiayRepository.findAll(spec, pageable);

        return pageResult;
    }
}

