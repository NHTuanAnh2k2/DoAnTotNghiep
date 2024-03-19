package com.example.demo.service;

import com.example.demo.entity.DeGiay;
import com.example.demo.repository.DeGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DeGiayService {

    public List<DeGiay> getAll();

    DeGiay add(DeGiay deGiay);

}
