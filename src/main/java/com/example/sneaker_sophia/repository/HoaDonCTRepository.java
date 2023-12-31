package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("hoaDonCTRepository")
public interface HoaDonCTRepository extends JpaRepository<HoaDonChiTiet, String> {
    @Query(value = "select * from HoaDonChiTiet where IdHoaDon = ?1", nativeQuery = true)
    List<HoaDonChiTiet> getHDCTByIdHD(String idhd);

    @Query(value = "select hdct from HoaDonChiTiet hdct where hdct.chiTietGiay.id = ?1 and hdct.hoaDon.id = ?2")
    HoaDonChiTiet getHDCTByIdCTSP(UUID idctsp, String idhd);

    //29-10
    @Query(value = "select sum(((hdct.donGia * (1- ((hdct.phanTramGiam) / 100.0)) * hdct.soLuongGiam)\n" +
            "                                              + (hdct.donGia * (hdct.soLuong - hdct.soLuongGiam)))) " +
            "from HoaDonChiTiet hdct where hdct.hoaDon.id = ?1")
    Double tongTienSauGiam(String idhc);

    @Query(value = "select sum(hdct.donGia * hdct.soLuong) from HoaDonChiTiet hdct where hdct.hoaDon.id = ?1")
    Double tongTienTruocGiam(String idhc);

    @Query(value = "select sum(hdct.donGia * ((hdct.phanTramGiam ) / 100.0)) from HoaDonChiTiet hdct where hdct.hoaDon.id = ?1")
    Double tienGiam(String idhd);

    @Query("SELECT c.chiTietGiay.id, COUNT(c.chiTietGiay.id) AS soLanXuatHien " +
            "FROM HoaDonChiTiet c " +
            "GROUP BY c.chiTietGiay.id " +
            "ORDER BY soLanXuatHien DESC")
    List<Object[]> findTop10IdChiTietGiay();


    //thống kê
    @Query("SELECT SUM(ctg.soLuong) FROM HoaDonChiTiet ctg WHERE ctg.hoaDon.trangThai = 1")
    Integer sumSoLuongByHoaDonTrangThaiEquals1();

}