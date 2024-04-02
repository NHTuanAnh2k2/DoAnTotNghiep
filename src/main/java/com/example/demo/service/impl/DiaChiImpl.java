package com.example.demo.service.impl;

import com.example.demo.entity.DiaChi;
import com.example.demo.info.DiaChiNVInfo;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DiaChiImpl implements DiaChiService {
    @Autowired
    DiaChiRepository diaChiRepository;
    @Override
    public List<DiaChi> getAll() {
        return diaChiRepository.findAll();
    }

    @Override
    public List<DiaChi> get(String ht, String sdt) {
        return diaChiRepository.TimhoTenHoacSdt(ht,sdt);
    }

    @Override
    public List<DiaChi> getTT(Boolean tt) {
        return diaChiRepository.TimTrangThai(tt);
    }

    @Override
    public DiaChi add(DiaChiNVInfo diaChi) {
        DiaChi dc = new DiaChi();
        dc.setTenduong(diaChi.getTenduong());
        dc.setXaphuong(diaChi.getXaphuong());
        dc.setQuanhuyen(diaChi.getQuanhuyen());
        dc.setTinhthanhpho(diaChi.getTinhthanhpho());
        dc.setNgaytao(new Timestamp(new Date().getTime()));
        dc.setNguoitao("");
        dc.setNguoicapnhat("");
        dc.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        dc.setTrangthai(true);
        dc.setNguoidung(diaChi.getIdnguoidung());
        return diaChiRepository.save(dc);
    }

    @Override
    public DiaChi update(DiaChiNVInfo diaChi, Integer id) {
        DiaChi dc = diaChiRepository.TimIdNguoiDung(id);
        dc.setTenduong(diaChi.getTenduong());
        dc.setXaphuong(diaChi.getXaphuong());
        dc.setQuanhuyen(diaChi.getQuanhuyen());
        dc.setTinhthanhpho(diaChi.getTinhthanhpho());
        dc.setNguoicapnhat("");
        dc.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        dc.setTrangthai(true);
        return diaChiRepository.save(dc);
    }

    @Override
    public DiaChi search(Integer id) {
        return diaChiRepository.TimIdNguoiDung(id);
    }

}
