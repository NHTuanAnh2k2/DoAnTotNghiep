package com.example.demo.service.impl;

import com.example.demo.entity.DeGiay;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.DeGiayRepository;
import com.example.demo.service.DeGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DeGiayImp implements DeGiayService {
    @Autowired
    DeGiayRepository deGiayRepository;

    @Override
    public List<DeGiay> getAll() {
        return deGiayRepository.findAll();
    }

    @Override
    public DeGiay add(DeGiay deGiay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date ngayTao = dateFormat.parse("21-12-2022");
            deGiay.setNgaytao(ngayTao);
            deGiay.setLancapnhatcuoi(ngayTao);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu không thể chuyển đổi chuỗi thành ngày
            e.printStackTrace();
        }
        deGiay.setNguoitao("duy");
        deGiay.setNguoicapnhat("tùng");
        deGiay.setTrangthai(true);
        return deGiayRepository.save(deGiay);
    }

    @Override
    public DeGiay findById(Integer id) {
        return deGiayRepository.findById(id).orElseThrow(() -> new NotFoundException("id không tồn tại"));
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new NotFoundException("ID không tồn tại");
        }
        deGiayRepository.deleteById(id);
    }

    @Override
    public List<DeGiay> getDeGiayByTenOrTrangthai(String ten, Boolean trangthai) {
        return deGiayRepository.getDeGiayByTenOrTrangthai(ten, trangthai);
    }


}
