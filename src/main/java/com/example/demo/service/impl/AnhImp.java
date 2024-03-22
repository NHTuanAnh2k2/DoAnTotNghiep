package com.example.demo.service.impl;

import com.example.demo.entity.Anh;
import com.example.demo.repository.AnhRepository;
import com.example.demo.service.AnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnhImp implements AnhService {
    @Autowired
    AnhRepository anhRepository;
    @Override
    public List<Anh> findAll() {
        return anhRepository.findAll();
    }
}
