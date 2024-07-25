package com.example.demo.info;

import com.example.demo.entity.SanPhamChiTiet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private SanPhamChiTiet sanphamchitiet;
    private Integer soluong;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
}
