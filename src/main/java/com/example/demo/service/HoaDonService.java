package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface HoaDonService {
    Page<HoaDon> findAll(Pageable p);
    Page<HoaDon> Loc(Integer trangThai, Boolean loaihd, Date tu, Date den, Pageable p);
    Page<HoaDon> timKiemTT(Integer trangThai, Pageable p);
    Long tinhTong(Integer tt);
    List<HoaDon> timall();
    Page<HoaDon> LockTT(Boolean loaihd, Date tu, Date den, Pageable p);
    Page<HoaDon> LocKLHD(Integer trangThai, Date tu, Date den, Pageable p);
    Page<HoaDon> LocKngayTao(Integer trangThai, Boolean loaihd, Pageable p);
}
