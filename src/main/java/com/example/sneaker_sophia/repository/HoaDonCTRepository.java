package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.dto.IdHoaDonCT;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("hoaDonCTRepository")
public interface HoaDonCTRepository extends JpaRepository<HoaDonChiTiet, Object> {
    @Query(value = "select * from HoaDonChiTiet where IdHoaDon = ?1", nativeQuery = true)
    List<HoaDonChiTiet> getHDCTByIdHD(String idhd);

    @Query(value = "select hdct from HoaDonChiTiet hdct where hdct.idHoaDonCT.chiTietGiay.id = ?1 and hdct.idHoaDonCT.hoaDon.id = ?2")
    HoaDonChiTiet getHDCTByIdCTSP(UUID idctsp, String idhd);
    //29-10
    @Query(value = "select sum(soLuong * donGia) from HoaDonChiTiet where IdHoaDon = ?1", nativeQuery = true)
    Double tongTienHD(String idhc);

}
