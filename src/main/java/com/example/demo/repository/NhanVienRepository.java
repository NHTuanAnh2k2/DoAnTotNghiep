package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT N FROM NhanVien N WHERE N.manhanvien like %?1%")
    List<NhanVien> timNVTheoMa(String ten);

    @Query("SELECT n FROM NhanVien n WHERE n.nguoidung.id = ?1")
    NhanVien findNhanVienByIdNd(Integer id);

    List<NhanVien> getNhanVienByTrangthai(Boolean trangThai);

    @Query("SELECT n FROM NhanVien n ORDER BY n.lancapnhatcuoi DESC ")
    List<NhanVien> getAll();
    @Query("SELECT n FROM NhanVien n WHERE n.nguoidung.id <> ?1 ORDER BY n.lancapnhatcuoi DESC ")
    List<NhanVien> getAll1(Integer id);

    @Query("SELECT c FROM NhanVien c  WHERE c.nguoidung.id = ?1")
    NhanVien TimIdNguoiDung(Integer id);

    @Query("SELECT N FROM NhanVien N WHERE N.nguoidung.sodienthoai = ?1")
    List<NhanVien> timSDT(String sdt);

    @Query("SELECT N FROM NhanVien N WHERE N.nguoidung.email = ?1")
    List<NhanVien> timEmail(String email);

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
    @Query("SELECT u FROM NhanVien u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<NhanVien> findByKeys(@Param("name") String name,
                              @Param("status") boolean status);

    @Query("SELECT u FROM NhanVien u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<NhanVien> findByStart(@Param("name") String name,
                               @Param("startDate") Date startDate,
                               @Param("status") boolean status);

    @Query("SELECT u FROM NhanVien u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<NhanVien> findByEnd(@Param("name") String name,
                             @Param("endDate") Date endDate,
                             @Param("status") boolean status);
}
