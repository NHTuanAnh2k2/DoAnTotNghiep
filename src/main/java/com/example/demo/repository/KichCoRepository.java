package com.example.demo.repository;

import com.example.demo.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KichCoRepository extends JpaRepository<KichCo,Integer> {
    List<KichCo> getKichCoByTenOrTrangthai(String ten, Boolean trangthai);

    List<KichCo> findAllByOrderByNgaytaoDesc();

    KichCo findByTen(String ten);
}
