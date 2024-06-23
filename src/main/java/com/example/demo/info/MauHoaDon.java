package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MauHoaDon {
    String name;
    String ma;
    Timestamp date;
    String tu;
    String den;
    String sdtshop;
    String sdtnhan;
    String tennguoinhan;
    List<sanPhamIn> lst;
    BigDecimal tongtien;
    String qr;
}
