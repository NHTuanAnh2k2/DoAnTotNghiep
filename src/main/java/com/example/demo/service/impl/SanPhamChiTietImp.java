package com.example.demo.service.impl;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SanPhamChiTietImp implements SanPhamChiTietService {
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;


    @Override
    public List<SanPhamChiTiet> findAll() {
        return sanPhamChiTietRepository.findAll();
    }

    @Override
    public Page<SanPhamChiTiet> finAllPage(Pageable pageable) {
        return sanPhamChiTietRepository.findAll(pageable);
    }

    @Override
    public SanPhamChiTiet addSPCT(SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    @Override
    public void deleteSPCT(Integer id) {
        sanPhamChiTietRepository.deleteById(id);
    }

    @Override
    public List<SanPhamChiTiet> findBySanPhamId(Integer idSanPham) {
        return sanPhamChiTietRepository.findBySanPhamId(idSanPham);
    }

//    @Override
//    public void updateSoLuongVaGiaTien(List<Integer> ids, Integer soluong, BigDecimal giatien) {
//        sanPhamChiTietRepository.updateSoLuongVaGiaTien(ids,soluong,giatien);
//    }

//    @Override
//    public void update(Integer id, Integer soLuong, BigDecimal giaTien) {
//        sanPhamChiTietRepository.update(id,soLuong,giaTien);
//    }



    @Override
    public SanPhamChiTiet findById(Integer id) {
       return sanPhamChiTietRepository.findById(id).orElse(null);
    }


}
