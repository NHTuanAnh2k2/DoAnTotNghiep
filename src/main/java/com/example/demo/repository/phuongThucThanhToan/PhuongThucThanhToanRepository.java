package com.example.demo.repository.phuongThucThanhToan;


import com.example.demo.entity.HoaDon;
import com.example.demo.entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    List<PhuongThucThanhToan> findAllByHoadon(HoaDon hd);
}
