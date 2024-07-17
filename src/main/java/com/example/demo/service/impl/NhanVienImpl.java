package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.repository.NguoiDungRepository1;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class NhanVienImpl implements NhanVienService {
    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    NguoiDungRepository1 ndRepo;
    @Override
    public List<NhanVien> getNhanVienByTrangThai(Boolean trangThai) {
        return nhanVienRepository.getNhanVienByTrangthai(trangThai);
    }

    @Override
    public List<NhanVien> getAll() {
        return nhanVienRepository.getAll();
    }
    private String taoChuoiNgauNhien(int doDaiChuoi, String kiTu) {
        Random random = new Random();
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        for (int i = 0; i < doDaiChuoi; i++) {
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        return chuoiNgauNhien.toString();
    }

    @Override
    public NhanVien add(NhanVienInfo nhanVien) {
        NhanVien nv = new NhanVien();
        String chuoiNgauNhien = taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        String maNV = "NV" + chuoiNgauNhien;
        nv.setVaitro(nhanVien.getVaitro());
        nv.setManhanvien(maNV);
        nv.setNgaytao(new Timestamp(new Date().getTime()));
        nv.setNguoitao("a");
        nv.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nv.setNguoicapnhat("a");
        nv.setTrangthai(true);
        nv.setNguoidung((NguoiDung) nhanVien.getIdnguoidung());
        return nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien update(NhanVienInfo nhanVien, Integer id) {
        NhanVien nv = nhanVienRepository.TimIdNguoiDung(id);
        nv.setVaitro(nhanVien.getVaitro());
        nv.setManhanvien(nhanVien.getManhanvien());
        nv.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nv.setNguoicapnhat("");
        nv.setTrangthai(nhanVien.getTrangthai());
        return nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien updateS(NhanVien nv) {
        return nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien search(Integer id) {
        return nhanVienRepository.TimIdNguoiDung(id);
    }

    @Override
    public List<NhanVien> searchND(String ten, Boolean trangThai, java.sql.Date batDau, java.sql.Date ketThuc) {
        return nhanVienRepository.findByKey(ten,batDau,ketThuc,trangThai);
    }

    @Override
    public List<NhanVien> searchNDs(String ten, java.sql.Date batDau, java.sql.Date ketThuc) {
        return nhanVienRepository.findByKe(ten,batDau,ketThuc);
    }

    @Override
    public List<NhanVien> searchKey(NhanVienSearch nhanVienSearch) {
        return nhanVienRepository.findByKeys(nhanVienSearch.getKey(),nhanVienSearch.isTrangThai());
    }

    @Override
    public List<NhanVien> searchStart(String ten, Boolean trangThai, java.sql.Date batDau) {
        return nhanVienRepository.findByStart(ten,batDau,trangThai);
    }

    @Override
    public List<NhanVien> searchEnd(String ten, Boolean trangThai, java.sql.Date ketThuc) {
        return nhanVienRepository.findByEnd(ten,ketThuc,trangThai);
    }

    @Override
    public List<NhanVien> timSDT(String sdt) {
        return nhanVienRepository.timSDT(sdt);
    }

    @Override
    public List<NhanVien> timEmail(String email) {
        return nhanVienRepository.timEmail(email);
    }

}
