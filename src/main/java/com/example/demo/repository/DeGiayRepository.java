package com.example.demo.repository;

import com.example.demo.entity.DeGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeGiayRepository extends JpaRepository<DeGiay, Integer> {
    List<DeGiay> getDeGiayByTen(String ten);
}
