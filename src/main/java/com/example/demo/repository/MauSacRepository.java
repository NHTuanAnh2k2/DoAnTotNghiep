package com.example.demo.repository;

import com.example.demo.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    List<MauSac> getDeGiayByTenOrTrangthai(String ten, Boolean trangthai);

    List<MauSac> findAllByOrderByNgaytaoDesc();

}
