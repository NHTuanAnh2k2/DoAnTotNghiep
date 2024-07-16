package com.example.demo.repository;

import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
//    Page<ThuongHieu> getAll(Pageable pageable);

    List<ThuongHieu> getThuongHieuByTenOrTrangthai(String ten, Boolean trangthai);

    List<ThuongHieu> findAllByOrderByNgaytaoDesc();

    boolean existsByTen(String ten);

    // seach theo ten
    @Query("SELECT th FROM ThuongHieu th WHERE th.ten = :ten AND th.trangthai = true ")
    List<ThuongHieu> findThuongHieuByTenAndTrangThaiFalse(@Param("ten") String ten);

    // lấy tất cả theo điều kiện trangthai=1
    @Query(nativeQuery = true, value = """
            SELECT * FROM ThuongHieu WHERE TrangThai=1
               ORDER BY NgayTao DESC
            """)
    List<ThuongHieu> getAll();

    // cập nhật trangthai=0
    @Modifying
    @Transactional
    @Query("UPDATE ThuongHieu th SET th.trangthai = false WHERE th.id = :id")
    void updateTrangThaiToFalseById(Integer id);

}
