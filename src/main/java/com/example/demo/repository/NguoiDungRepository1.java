package com.example.demo.repository;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository1 extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT c FROM NguoiDung c left join KhachHang n on n.nguoidung.id = c.id where c.sodienthoai = ?1 and  n.id = null")
    NguoiDung searchEmail(String sdt);
    @Query("SELECT c FROM NguoiDung c WHERE c.id = ?1")
    NguoiDung searchId(Integer id);
    List<NguoiDung> getAllByOrderByIdDesc();
    List<NguoiDung> getNhanVienByTrangthai(Boolean trangThai);
    @Query("SELECT u FROM NguoiDung u WHERE " +
            "(:name is null or u.hovaten LIKE %:name% or u.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.ngaysinh <= :endDate) and " +
            "(:status is null or u.trangthai = :status) ORDER BY u.lancapnhatcuoi DESC")
    List<NguoiDung> findByKey(@Param("name") String name,
                           @Param("startDate") Date startDate,
                           @Param("endDate") Date endDate,
                           @Param("status") boolean status);
    @Query("SELECT u FROM NguoiDung u WHERE " +
            "(:name is null or u.hovaten LIKE %:name% or u.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.ngaysinh >= :startDate) and " +
            "(:endDate is null or u.ngaysinh <= :endDate) ORDER BY u.lancapnhatcuoi DESC")
    List<NguoiDung> findByKeys(@Param("name") String name,
                              @Param("startDate") Date startDate,
                              @Param("endDate") Date endDate);
    @Query("SELECT u FROM NguoiDung u WHERE " +
            "(:name is null or u.hovaten LIKE %:name% or u.sodienthoai LIKE %:name%) and " +
            "(:status is null or u.trangthai = :status)")
    List<NguoiDung> findByKeys(@Param("name") String name,
                              @Param("status") boolean status);
    @Query("SELECT u FROM NguoiDung u WHERE " +
            "(:name is null or u.hovaten LIKE %:name% or u.sodienthoai LIKE %:name%) and " +
            "(:startDate is null or u.ngaysinh >= :startDate) and " +
            "(:status is null or u.trangthai = :status)")
    List<NguoiDung> findByStart(@Param("name") String name,
                              @Param("startDate") Date startDate,
                              @Param("status") boolean status);
    @Query("SELECT u FROM NguoiDung u WHERE " +
            "(:name is null or u.hovaten LIKE %:name% or u.sodienthoai LIKE %:name%) and " +
            "(:endDate is null or u.ngaysinh <= :endDate) and " +
            "(:status is null or u.trangthai = :status)")
    List<NguoiDung> findByEnd(@Param("name") String name,
                                @Param("endDate") Date endDate,
                                @Param("status") boolean status);
}
