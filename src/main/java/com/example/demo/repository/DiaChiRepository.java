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
    List<DiaChi> getDiaChiByTrangthai(Boolean trangThai);
    @Query("SELECT c FROM DiaChi c JOIN NhanVien n on n.nguoidung.id = c.nguoidung.id Order by c.nguoidung.id desc")
    List<DiaChi> getAll();
    List<DiaChi> getAllByOrderByIdDesc();
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.id = ?1 and c.trangthai=true")
    DiaChi TimIdNguoiDungMacDinh(Integer id);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.id = ?1")
    DiaChi TimIdNguoiDung(Integer id);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.hovaten = ?1 OR c.nguoidung.sodienthoai = ?1")
    List<DiaChi> TimhoTenHoacSdt(String ht, String sdt);
    @Query("SELECT c FROM DiaChi c  WHERE c.nguoidung.trangthai = ?1")
    List<DiaChi> TimTrangThai(Boolean tt);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) and " +
    "(:status is null or u.nguoidung.trangthai = :status)")
    List<DiaChi> findByKey(@Param("name") String name,
                              @Param("startDate") Date startDate,
                              @Param("endDate") Date endDate,
                           @Param("status") boolean status);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<DiaChi> findByKeys(@Param("name") String name,
                           @Param("status") boolean status);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.nguoidung.ngaysinh >= :startDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<DiaChi> findByStart(@Param("name") String name,
                           @Param("startDate") Date startDate,
                           @Param("status") boolean status);
    @Query("SELECT u FROM DiaChi u WHERE " +
            "(:name is null or u.nguoidung.hovaten LIKE %:name% or u.nguoidung.sodienthoai LIKE %:name%) and " +
            "(:endDate is null or u.nguoidung.ngaysinh <= :endDate) and " +
            "(:status is null or u.nguoidung.trangthai = :status)")
    List<DiaChi> findByEnd(@Param("name") String name,
                           @Param("endDate") Date endDate,
                           @Param("status") boolean status);

    @Query("SELECT d FROM DiaChi d WHERE d.tinhthanhpho LIKE %?1%")
    List<DiaChi> findDiaChiByTinhthanhpho(@Param("tinhthanhpho") String tinhthanhpho);
    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1")
    List<DiaChi> findDiaChiByIdNd(Integer idNd);

    @Query("SELECT d FROM DiaChi d WHERE d.nguoidung.id = ?1")
    DiaChi findDiaChiByIdNguoidung(Integer idNd);

}
