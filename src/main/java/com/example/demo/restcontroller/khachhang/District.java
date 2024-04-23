package com.example.demo.restcontroller.khachhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
    private String Name;
    private List<Ward> Wards;
}
