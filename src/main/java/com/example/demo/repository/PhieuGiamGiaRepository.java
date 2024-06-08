package com.example.demo.repository;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia,Integer> {
    PhieuGiamGia findFirstByOrderByNgaytaoDesc();
    @Query("SELECT p FROM PhieuGiamGia p where (:keySearch is null or p.tenphieu like %:keySearch% or p.macode like %:keySearch%)" +
            " and (:tungaySearch is null or p.ngaybatdau >= :tungaySearch)" +
            " and (:denngaySearch is null or p.ngayketthuc <= :denngaySearch)" +
            " and (:kieuSearch is null or p.kieuphieu = :kieuSearch)" +
            " and (:loaiSearch is null or p.loaiphieu = :loaiSearch)" +
            " and (:ttSearch is null or p.trangthai = :ttSearch) ORDER BY p.ngaytao DESC ")
    Page<PhieuGiamGia> findAllOrderByNgayTaoDESC(@Param("keySearch") String keySearch,
                                                 @Param("tungaySearch")Timestamp tungaySearch,
                                                 @Param("denngaySearch")Timestamp denngaySearch,
                                                 @Param("kieuSearch") Boolean kieuSearch,
                                                 @Param("loaiSearch") Boolean loaiSearch,
                                                 @Param("ttSearch") Integer ttSearch,
                                                 Pageable pageable);
    @Query("SELECT p FROM PhieuGiamGia p WHERE p.id=?1")
    PhieuGiamGia findPhieuGiamGiaById(Integer Id);
    @Query("SELECT p FROM PhieuGiamGia p where p.kieuphieu=?1 and p.trangthai=?2")
    List<PhieuGiamGia> findAllByKieuphieuaAndTrangthais(Boolean kieuphieu,Integer trangthai);
}
