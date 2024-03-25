package com.example.demo.service;

import com.example.demo.entity.MauSac;

import java.util.List;

public interface MauSacService {
    List<MauSac> findAll();

    MauSac addMauSac(MauSac mauSac);
}
