package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "anh")
public class Anh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tenanh;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime ngaytao;
    String nguoitao;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lancapnhatcuoi;
    String nguoicapnhat;
    Boolean trangthai;
    @ManyToOne
    @JoinColumn(name = "idsanphamchitiet")
    SanPhamChiTiet sanphamchitiet;

}
