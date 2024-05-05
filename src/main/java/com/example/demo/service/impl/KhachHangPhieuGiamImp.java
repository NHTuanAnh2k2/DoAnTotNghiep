package com.example.demo.service.impl;

import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.repository.KhachHangPhieuGiamRepository;
import com.example.demo.service.KhachHangPhieuGiamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangPhieuGiamImp implements KhachHangPhieuGiamService {
    @Autowired
    private KhachHangPhieuGiamRepository khachHangPhieuGiamRepository;

    @Override
    public KhachHangPhieuGiam AddKhachHangPhieuGiam(KhachHangPhieuGiam khachHangPhieuGiam) {
        return khachHangPhieuGiamRepository.save(khachHangPhieuGiam);
    }

    @Override
    public List<KhachHangPhieuGiam> findKhachHangPhieuGiamByIdPhieugiamgia(Integer IdPhieu) {
        return khachHangPhieuGiamRepository.findKhachHangPhieuGiamByIdPhieugiamgia(IdPhieu);
    }
}
