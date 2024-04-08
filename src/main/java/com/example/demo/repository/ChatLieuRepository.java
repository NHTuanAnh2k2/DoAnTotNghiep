package com.example.demo.repository;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
    @Query("SELECT c FROM ChatLieu c WHERE c.ten LIKE %?1% OR c.trangthai = ?2")
    List<ChatLieu> findByTenVaTrangThai(String ten, Boolean trangthai);

    List<ChatLieu> getChatLieuByTenOrTrangthai(String ten, Boolean trangthai);

    List<ChatLieu> findAllByOrderByNgaytaoDesc();

}
