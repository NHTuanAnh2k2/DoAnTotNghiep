package com.example.demo.service.impl;

import com.example.demo.entity.DeGiay;
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
            deGiay.setNgay_tao(ngayTao);
            deGiay.setLan_cap_nhat_cuoi(ngayTao);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu không thể chuyển đổi chuỗi thành ngày
            e.printStackTrace();
        }
        deGiay.setNguoi_tao("duy");
        deGiay.setNguoi_cap_nhat("tùng");
        deGiay.setTrang_thai(true);
        return deGiayRepository.save(deGiay);
    }

    @Override
    public List<DeGiay> getDeGiayByTen(String ten) {
        return deGiayRepository.getDeGiayByTen(ten);
    }


}
