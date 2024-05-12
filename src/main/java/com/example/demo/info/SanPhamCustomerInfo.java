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
}
