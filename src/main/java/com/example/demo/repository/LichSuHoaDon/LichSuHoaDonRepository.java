package com.example.demo.repository.LichSuHoaDon;


import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    List<LichSuHoaDon> findAllByHoadonOrderByNgaytao(HoaDon hd);

    @Query("SELECT h FROM LichSuHoaDon h WHERE h.hoadon.id =?1")
    LichSuHoaDon findLichSuHoaDonByIdHoaDon(Integer idHoaDon);
}
