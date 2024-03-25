package com.example.demo.service.impl;

import com.example.demo.entity.KichCo;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichCoImp implements KichCoService {
    @Autowired
    KichCoRepository kichCoRepository;
    @Override
    public List<KichCo> findAll() {
        return kichCoRepository.findAll();
    }

    @Override
    public KichCo addKichCo(KichCo kichCo) {
        return kichCoRepository.save(kichCo);
    }
}
