package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienNVInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT new com.example.demo.info.NhanVienNVInfo(nd.id, nv.manhanvien, nd.hovaten," +
            "nd.sodienthoai, nd.ngaysinh, dc.tinhthanhpho, dc.quanhuyen, dc.xaphuong, dc.tenduong, nv.vaitro, nv.trangthai)" +
            "FROM NhanVien nv " +
            "JOIN NguoiDung nd ON nv.nguoidung.id = nd.id " +
            "JOIN DiaChi dc ON dc.nguoidung.id = nd.id " +
            "WHERE nd.id =?1")
    NhanVienNVInfo findNhanVienDiaChi(Integer userId);

    @Query("SELECT k FROM NhanVien k WHERE k.nguoidung.email = ?1")
    NhanVien findNhanVienByEmail(@Param("emailResetPassword") String emailResetPassword);

    @Query("SELECT N FROM NhanVien N WHERE N.manhanvien like %?1%")
    List<NhanVien> timNVTheoMa(String ten);

    @Query("SELECT n FROM NhanVien n WHERE n.nguoidung.id = ?1")
    NhanVien findNhanVienByIdNd(Integer id);

    @Query("SELECT n FROM NhanVien n ORDER BY n.lancapnhatcuoi DESC ")
    List<NhanVien> getAll();

    @Query("SELECT n FROM NhanVien n WHERE n.nguoidung.id <> ?1 ORDER BY n.lancapnhatcuoi DESC ")
    List<NhanVien> getAll1(Integer id);

    @Query("SELECT c FROM NhanVien c  WHERE c.nguoidung.id = ?1")
    NhanVien TimIdNguoiDung(Integer id);

    @Query("SELECT u FROM NhanVien u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<NhanVien> findByKey(@Param("name") String name,
                             @Param("startDate") Date startDate,
                             @Param("endDate") Date endDate,
                             @Param("status") boolean status);

    @Query("SELECT u FROM NhanVien u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate)  ORDER BY u.lancapnhatcuoi DESC")
    List<NhanVien> findByKe(@Param("name") String name,
                            @Param("startDate") Date startDate,
                            @Param("endDate") Date endDate);

    List<NhanVien> findByNguoidung(NguoiDung nd);

}
