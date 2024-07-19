package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    @Query("SELECT c FROM DiaChi c JOIN NhanVien n on n.nguoidung.id = c.nguoidung.id Order by c.nguoidung.lancapnhatcuoi desc")
    List<DiaChi> getAll();
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.id = ?1 and c.trangthai=true")
    DiaChi TimIdNguoiDungMacDinh(Integer id);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.id = ?1")
    DiaChi TimIdNguoiDung(Integer id);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.hovaten = ?1 OR c.nguoidung.sodienthoai = ?1")
    List<DiaChi> TimhoTenHoacSdt(String ht, String sdt);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status) ORDER BY u.lancapnhatcuoi DESC")
    List<DiaChi> findByKey(@Param("name") String name,
                              @Param("startDate") Date startDate,
                              @Param("endDate") Date endDate,
                           @Param("status") boolean status);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) ORDER BY u.lancapnhatcuoi DESC")
    List<DiaChi> findByKeys(@Param("name") String name,
                           @Param("startDate") Date startDate,
                           @Param("endDate") Date endDate);

    @Query("SELECT d FROM DiaChi d WHERE d.tinhthanhpho LIKE %?1%")
    List<DiaChi> findDiaChiByTinhthanhpho(@Param("tinhthanhpho") String tinhthanhpho);
    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1 ORDER BY d.trangthai DESC, d.ngaytao DESC")
    List<DiaChi> findDiaChiByIdNd(Integer idNd);
    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1")
    DiaChi findDiaChiByIdNguoidung(Integer idNd);
    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1 AND d.trangthai = ?2")
    DiaChi findDiaChiByTrangthai(Integer idNd, boolean trangthai);
    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1 AND d.trangthai = ?2")
    List<DiaChi> findLstDiaChiByTrangthai(Integer idNd, boolean trangthai);

}
