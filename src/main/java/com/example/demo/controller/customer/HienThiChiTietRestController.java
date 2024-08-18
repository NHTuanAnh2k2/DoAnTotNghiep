package com.example.demo.controller.customer;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.info.SanPhamChiTietConvertTA;
import com.example.demo.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class HienThiChiTietRestController {
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    // ẩn size
    @GetMapping("/hien-thi-size-color/{encodedChuoighep}")
    public List<Integer> getSizeColor(@PathVariable("encodedChuoighep") String encodedChuoighep) {
        String decodedIdTen = encodedChuoighep.replace("%23", "#");
        String[] parts = decodedIdTen.split("-");
        Integer id = Integer.parseInt(parts[0]);
        String ten = parts[1];
        List<Integer> allSize = new ArrayList<>();
        List<SanPhamChiTiet> lst = sanPhamChiTietRepository.listSizeColor(id, ten);
        for (SanPhamChiTiet s : lst) {
            Integer size = Integer.parseInt(s.getKichco().getTen());
            allSize.add(size);
        }
        System.out.println("Received Id: " + id);
        System.out.println("Received tenMauSac: " + ten);
        return allSize;
    }
    // ẩn size
    @GetMapping("/hien-thi-all-size/{Id}")
    public List<Integer> getAllSize(@PathVariable("Id") Integer Id) {
        List<Integer> allSize = new ArrayList<>();
        List<SanPhamChiTiet> lst = sanPhamChiTietRepository.listAllSize(Id);
        for (SanPhamChiTiet s : lst) {
            Integer size = Integer.parseInt(s.getKichco().getTen());
            if (!allSize.contains(size)) {
                allSize.add(size);
            }
        }
        return allSize;
    }
    // ẩn màu
//    @GetMapping("/hien-thi-mausac/{encodedChuoighep}")
//    public List<String> getMauSac(@PathVariable("encodedChuoighep") String encodedChuoighep) {
//
//        String[] parts = decodedIdTen.split("-");
//        Integer id=Integer.parseInt(parts[0]);
//        String ten=parts[1];
//        List<Integer> allSize = new ArrayList<>();
//        List<SanPhamChiTiet> lst = sanPhamChiTietRepository.listSizeColor(id, ten);
//        for (SanPhamChiTiet s : lst) {
//            Integer size = Integer.parseInt(s.getKichco().getTen());
//            allSize.add(size);
//        }
//        System.out.println("Received Id: " + id);
//        System.out.println("Received tenMauSac: " + ten);
//        return allSize;
//    }


}
