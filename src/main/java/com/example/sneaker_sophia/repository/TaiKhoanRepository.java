package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taiKhoanRepository")
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

//    @Query(value = "SELECT tk.ten, tk.email , tk.ngaySinh, tk.sdt, tk.gioiTinh," +
//            " tk.trangThai from TaiKhoan tk join VaiTro vt on tk.vaiTro.id = vt.id where vt.id = '179A8E82-EDD8-4B84-8993-274A8BAC82A8'")
//    List<NhanVienDTO> getAllNhanVien();

//    @Query(value = "SELECT Id,taiKhoan,anhDaiDien,canCuoc, TaiKhoan.ten, email, sdt, ngaySinh, sdt, gioiTinh, TaiKhoan.trangThai from TaiKhoan \n" +
//            "     ", nativeQuery = true)
//    Page<TaiKhoan> getAllNhanVien(Pageable pageable);

    //
    @Query(value = "select tk from TaiKhoan tk " +
            "join VaiTro vt on tk.vaiTro.id = vt.id " +
            "where (vt.ma = 'VT002' and (:trangThai is null or :trangThai = -1 or tk.trangThai = :trangThai)) " +
            "and (:search is null or tk.ten like %:search% or tk.sdt like %:search%)")
    Page<TaiKhoan> getALlNhanVienTT(@Param("search") String search, @Param("trangThai") Integer trangThai, Pageable pageable);

    @Query(value = "select tk from TaiKhoan tk " +
            "join VaiTro vt on tk.vaiTro.id = vt.id " +
            "where (vt.ma = 'VT001' and (:trangThai is null or :trangThai = -1 or tk.trangThai = :trangThai)) " +
            "and (:search is null or tk.ten like %:search% or tk.sdt like %:search%)")
    Page<TaiKhoan> getALlKhachHang(@Param("search") String search, @Param("trangThai") Integer trangThai, Pageable pageable);


    @Query(value = "select anhDaiDien from TaiKhoan where Id = ?1", nativeQuery = true)
    String getAnhById(String id);


//    cuongdv
    @Query(value = "select tk from TaiKhoan tk where tk.vaiTro.ten like 'Khach Hang'")
    List<TaiKhoan> findAllKhachHang();


    @Query(value = "select tk.* from TaiKhoan tk join VaiTro on tk.IdVaiTro = VaiTro.Id where " +
            "(?1 is null or  tk.ten like ?1) or" +
            "(?1 is null or tk.sdt like ?1)"
            , nativeQuery = true)
    List<TaiKhoan> findByText(String text);


}
