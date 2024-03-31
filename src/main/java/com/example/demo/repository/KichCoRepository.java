package com.example.demo.repository;

import com.example.demo.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KichCoRepository extends JpaRepository<KichCo,Integer> {
    KichCo findByTen(String ten);
}
