package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.info.MauHoaDon;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.FileOutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HoaDonImp implements HoaDonService {
    @Autowired
    HoaDonRepository dao;

    @Override
    public Page<HoaDon> findAll(Pageable p) {
        return dao.findAll(p);
    }

    @Override
    public Page<HoaDon> Loc(Integer trangThai, Boolean loaihd, Date tu, Date den, Pageable p) {
        return dao.findAllByTrangthaiAndLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(trangThai, loaihd, tu, den, p);
    }

    @Override
    public Page<HoaDon> timKiemTT(Integer trangThai, Pageable p) {
        return dao.findAllByTrangthai(trangThai, p);
    }

    @Override
    public Long tinhTong(Integer tt) {
        return dao.countAllByTrangthai(tt);
    }

    @Override
    public List<HoaDon> timall() {
        return dao.findAll();
    }

    @Override
    public Page<HoaDon> LockTT(Boolean loaihd, Date tu, Date den, Pageable p) {
        return dao.findAllByLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(loaihd, tu, den, p);
    }

    @Override
    public Page<HoaDon> LocKLHD(Integer trangThai, Date tu, Date den, Pageable p) {
        return dao.findAllByTrangthaiAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(trangThai, tu, den, p);
    }

    @Override
    public Page<HoaDon> LocKngayTao(Integer trangThai, Boolean loaihd, Pageable p) {
        return dao.findAllByTrangthaiAndLoaihoadon(trangThai, loaihd, p);
    }

    @Override
    public Page<HoaDon> LocTheoKhoangNgay(Date tu, Date den, Pageable p) {
        return dao.findAllByNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(tu, den, p);
    }

    @Override
    public Page<HoaDon> LocTheoLoaiDon(Boolean loaihd, Pageable p) {
        return dao.findAllByLoaihoadon(loaihd, p);
    }

    @Override
    public List<HoaDon> timTheoID(Integer id) {
        return dao.findAllById(id);
    }

    @Override
    public void capNhatHD(HoaDon hd) {
        dao.save(hd);
    }

    @Override
    public String htmlToPdf(String fileHtmlName,String pdfname) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            DefaultFontProvider defaultFontProvider = new DefaultFontProvider(false, true, false);
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFontProvider);
            HtmlConverter.convertToPdf(fileHtmlName, pdfWriter, converterProperties);
            FileOutputStream fout = new FileOutputStream("C:/Users/ADMIN/Desktop/"+pdfname+".pdf");
            byteArrayOutputStream.writeTo(fout);
            byteArrayOutputStream.close();
            byteArrayOutputStream.flush();
            fout.close();
            return null;
        } catch (Exception e) {
            //exception out

        }
        return null;
    }

    @Override
    public Context setData(MauHoaDon userList) {
        Context context = new Context();
        Map<String, Object> data = new HashMap<>();
        data.put("user", userList);
        context.setVariables(data);
        return context;
    }

}
