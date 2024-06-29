package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    private Integer idspct;
    private Integer soluong;
    private BigDecimal giatien;
}
