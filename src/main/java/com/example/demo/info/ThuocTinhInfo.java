package com.example.demo.info;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThuocTinhInfo {
    String key;
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyy-MM-dd")
//    Date ngaybd;
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyy-MM-dd")
//    Date ngaykt;
    Boolean trangthai;
}
