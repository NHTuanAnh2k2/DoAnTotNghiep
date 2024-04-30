package com.example.demo.repository;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    @Query("SELECT p FROM DotGiamGia p where (:keySearch is null or p.tendot like %:keySearch%)" +
            " and (:tungaySearch is null or p.ngaybatdau >= :tungaySearch)" +
            " and (:denngaySearch is null or p.ngayketthuc <= :denngaySearch)" +
            " and (:ttSearch is null or p.trangthai = :ttSearch) ORDER BY p.ngaytao DESC ")
    Page<DotGiamGia> findAllOrderByNgayTaoDESC(@Param("keySearch") String keySearch,
                                                 @Param("tungaySearch") Timestamp tungaySearch,
                                                 @Param("denngaySearch")Timestamp denngaySearch,
                                                 @Param("ttSearch") Integer ttSearch,
                                                 Pageable pageable);
    DotGiamGia findDotGiamGiaById(Integer Id);
}
