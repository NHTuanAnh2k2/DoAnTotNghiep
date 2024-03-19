package com.example.demo.service.impl;

import com.example.demo.entity.DeGiay;
import com.example.demo.repository.DeGiayRepository;
import com.example.demo.service.DeGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return deGiayRepository.save(deGiay);
    }

    @Override
    public List<DeGiay> getDeGiayByTen(String ten) {
        return deGiayRepository.getDeGiayByTen(ten);
    }


}
