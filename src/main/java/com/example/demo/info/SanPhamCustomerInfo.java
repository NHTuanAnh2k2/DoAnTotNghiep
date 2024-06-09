package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamCustomerInfo {
    String key;
    List<Integer> idThuongHieu;
    Integer idDeGiay;
    List<Integer> idKichCo2;
    Integer idMauSac;
    Integer idChatLieu;
    Boolean gioitinh;
    Boolean range1;
    Boolean range2;
    Boolean range3;
    Boolean range4;
    Boolean range5;
}
