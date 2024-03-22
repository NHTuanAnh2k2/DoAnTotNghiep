package com.example.demo.service.impl;

import com.example.demo.entity.MauSac;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacImp implements MauSacService {
    @Autowired
    MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> findAll() {
        return mauSacRepository.findAll();
    }
}
