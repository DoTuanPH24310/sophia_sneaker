package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
@Service
public class ChiTietGiayIplm implements ChiTietGiayService {
    @Autowired
    ChiTietGiayRepository chiTietGiayRepository;
    @Override
    public List<ChiTietGiay> getAll() {
        return chiTietGiayRepository.findAll();
    }

//    @Override
//    public Page<ChiTietGiay> listByPageAndProductName(int pageNum, String sortField, String sortDir, String keyword, String productName) {
//        Sort sort = Sort.by(sortField);
//        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);
//
//        if (StringUtils.isEmpty(productName) && StringUtils.isEmpty(keyword)) {
//            return chiTietGiayRepository.findAll(pageable);
//        } else  {
//            return chiTietGiayRepository.findByKeyword(keyword, pageable);
//        }
//    }
}
