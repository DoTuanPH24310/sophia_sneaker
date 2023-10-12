package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChiTietGiayService {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    List<ChiTietGiay> findAllByIdGiay(List<String> listId){
        return  chiTietGiayRepository.findAllByIdGiay(convertStringListToUUIDList(listId));
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

}
