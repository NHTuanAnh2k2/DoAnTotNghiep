package com.example.demo.repository.PhieuGiamGiaChiTiet;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.PhieuGiamGiaChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuGiamChiTietRepository extends JpaRepository<PhieuGiamGiaChiTiet,Integer> {
 List<PhieuGiamGiaChiTiet> findAllByHoadon(HoaDon hd);
}
