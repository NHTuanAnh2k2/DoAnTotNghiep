package com.example.demo.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhanVienSearch {
    String key;
    boolean trangThai;
    String batdau;
    String ketthuc;
    int tuoi;
}
