package com.example.demo.info;

import com.example.demo.entity.SanPhamChiTiet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamInfo {
    String key;
    Boolean trangthai;
    Integer soluong;
}
