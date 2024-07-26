package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraCuuDetailInfo {
    private String tenAnh;
    private String tenSanPham;
    private String size;
    private Integer soLuong;
    private BigDecimal tongTien;
}
