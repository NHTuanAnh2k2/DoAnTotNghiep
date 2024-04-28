package com.example.demo.repository;

import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT n FROM NguoiDung n WHERE n.taikhoan = ?1")
    NguoiDung findNguoiDungByTaikhoan(String taikhoan);
    @Query("SELECT n FROM NguoiDung n WHERE n.hovaten LIKE %?1% OR n.sodienthoai LIKE %?2%")
    List<NguoiDung> findByTenOrSdt(@Param("hovaten") String ten,
                                   @Param("sodienthoai") String sdt);

}
