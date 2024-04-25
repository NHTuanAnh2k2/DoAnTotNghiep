package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.NhanVienInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.Normalizer;
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
        return nhanVienRepository.getAllByOrderByIdDesc();
    }

    @Override
    public NhanVien add(NhanVienInfo nhanVien) {
        List<NhanVien> l = nhanVienRepository.getAllByOrderByIdDesc();
        NhanVien nv = new NhanVien();
        String ten = nhanVien.getIdnguoidung().getHovaten();
        String[] cacTu = ten.split("\\s+");
        for (int i = 0; i < cacTu.length; i++) {
            cacTu[i] = Normalizer.normalize(cacTu[i], Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            cacTu[i] = cacTu[i].toLowerCase();
        }
        String tenCuoi = cacTu[cacTu.length - 1];
        String[] parts = ten.split("\\s+");
        StringBuilder chuoiMoi = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                chuoiMoi.append(parts[i].charAt(0));
            }
        }
        int s = l.size() + 1;
        tenCuoi = tenCuoi + chuoiMoi.toString().toLowerCase() + s;
        nv.setVaitro(true);
        nv.setManhanvien(tenCuoi);
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
//        List<NhanVien> l = nhanVienRepository.getAllByOrderByIdDesc();
//        String ten = nhanVien.getIdnguoidung().getHovaten();
//        String[] cacTu = ten.split("\\s+");
//        for (int i = 0; i < cacTu.length; i++) {
//            cacTu[i] = Normalizer.normalize(cacTu[i], Normalizer.Form.NFD)
//                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
//            cacTu[i] = cacTu[i].toLowerCase();
//        }
//        String tenCuoi = cacTu[cacTu.length - 1];
//        String[] parts = ten.split("\\s+");
//        StringBuilder chuoiMoi = new StringBuilder();
//        for (int i = 0; i < parts.length - 1; i++) {
//            if (!parts[i].isEmpty()) {
//                chuoiMoi.append(parts[i].charAt(0));
//            }
//        }
//        int s = l.size() + 1;
//        tenCuoi = tenCuoi + chuoiMoi.toString().toLowerCase() + s;
        nv.setVaitro(true);
//        nv.setManhanvien(tenCuoi);
        nv.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nv.setNguoicapnhat("");
        nv.setTrangthai(true);
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
