package com.example.demo.info;

import com.example.demo.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietConvertTA {
    Integer id;
    SanPham sanpham;
    KichCo kichco;
    MauSac mausac;
    List<Anh> anh;
}
