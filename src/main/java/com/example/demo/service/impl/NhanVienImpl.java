package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class NhanVienImpl implements NhanVienService {
    @Autowired
    NhanVienRepository nhanVienRepository;
    @Override
    public List<NhanVien> getNhanVienByTrangThai(Boolean trangThai) {
        return nhanVienRepository.getNhanVienByTrangthai(trangThai);
    }

    @Override
    public List<NhanVien> getAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public NhanVien add(NhanVienInfo nhanVien) {
        NhanVien nv = new NhanVien();
        nv.setVaitro(true);
        nv.setNgaytao(new Timestamp(new Date().getTime()));
        nv.setNguoitao("");
        nv.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nv.setNguoicapnhat("");
        nv.setTrangthai(true);
        nv.setNguoidung((NguoiDung) nhanVien.getIdnguoidung());
        return nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien update(NhanVienInfo nhanVien, Integer id) {
        NhanVien nv = nhanVienRepository.TimIdNguoiDung(id);
        nv.setVaitro(true);
        nv.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nv.setNguoicapnhat("");
        nv.setTrangthai(true);
        return nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien search(Integer id) {
        return nhanVienRepository.TimIdNguoiDung(id);
    }

}
