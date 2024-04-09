package com.example.demo.controller.sanpham;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamInfo;
import com.example.demo.repository.AnhRepository;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class SanPhamController {
    @Autowired
    KichCoRepository kichCoRepository;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    SanPhamRepositoty sanPhamRepositoty;

    @Autowired
    SanPhamImp sanPhamImp;

    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

    @Autowired
    ThuongHieuImp thuongHieuImp;

    @Autowired
    MauSacImp mauSacImp;

    @Autowired
    KichCoImp kichCoImp;

    @Autowired
    DeGiayImp deGiayImp;

    @Autowired
    ChatLieuImp chatLieuImp;

    @Autowired
    AnhImp anhImp;
    @Autowired
    AnhRepository anhRepository;

    @Autowired
    HttpServletRequest request;

    //Hiển thị list sản phẩm
    @GetMapping("/listsanpham")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("tim") SanPhamInfo info, Model model) {
        Pageable pageable = PageRequest.of(p, 20);
        Page<Object[]> page = null;
        if (info.getKey() != null) {
            page = sanPhamRepositoty.findByTenSanPhamAndTrangThai("%" + info.getKey() + "%",info.getTrangthai(),pageable);
        } else {
            page = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc(pageable);
        }
        model.addAttribute("page", page);
        return "admin/qlsanpham";
    }

    //hiển thị các thuộc tính của sản phẩm thông qua modelAttribute
    @RequestMapping(value = {"/viewaddSP", "/viewaddSP"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String viewaddSP(Model model, @RequestParam(defaultValue = "0") int p,
                            @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                            @ModelAttribute("chatlieu") ChatLieu chatLieu,
                            @ModelAttribute("kichco") KichCo kichCo,
                            @ModelAttribute("degiay") DeGiay deGiay,
                            @ModelAttribute("mausac") MauSac mauSac
    ) {
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<SanPhamChiTiet> listSPCT = sanPhamChiTietImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        List<Anh> listAnh = anhImp.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("spct", listSPCT);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);
        model.addAttribute("a", listAnh);
        return "admin/addsanpham";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam(defaultValue = "0") int p, Model model,
                             @RequestParam String tensp,
                             @RequestParam String mota,
//                             @RequestParam Boolean trangthai,
                             @RequestParam ThuongHieu idThuongHieu,
                             @RequestParam ChatLieu idChatLieu,
                             @RequestParam Boolean gioitinh,
                             @RequestParam(name = "kichCoId") List<String> kichCoNames,
                             @RequestParam DeGiay idDeGiay,
                             @RequestParam List<MauSac> idMauSac
    ) {
        SanPham sanPham = new SanPham();
        sanPham.setTensanpham(tensp);
        sanPham.setTrangthai(true);
        LocalDateTime currentTime = LocalDateTime.now();
        sanPham.setNgaytao(currentTime);
        sanPhamImp.add(sanPham);
        for (MauSac colorId : idMauSac) {
            for (String sizeName : kichCoNames) {
                KichCo kichCo = kichCoRepository.findByTen(sizeName);
                if (kichCo != null) {
                    SanPhamChiTiet spct = new SanPhamChiTiet();
                    spct.setSanpham(sanPham);
                    spct.setSoluong(1);
                    spct.setGiatien(BigDecimal.valueOf(1000));
                    spct.setMota(mota);
                    spct.setThuonghieu(idThuongHieu);
                    spct.setChatlieu(idChatLieu);
                    spct.setGioitinh(gioitinh);
                    spct.setTrangthai(true);
                    spct.setKichco(kichCo);
                    spct.setDegiay(idDeGiay);
                    spct.setMausac(colorId);
                    sanPhamChiTietImp.addSPCT(spct);
                } else {
                }
            }
        }

        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findBySanPhamId(sanPham.getId());
        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
        return "forward:/viewaddSP";
    }


    @PostMapping("/addImage")
    public String addImage(
            Model model,
            @RequestParam(name = "anh") List<MultipartFile> anhFiles,
            @RequestParam Integer spctId
//            @RequestParam("id") Integer id,
//            @RequestParam("soluong") Integer soluong,
//            @RequestParam("giatien") BigDecimal giatien
    ) {
//        sanPhamChiTietRepository.update(id, soluong, giatien);
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId).orElse(null);
        if (spct != null) {
            for (MultipartFile anhFile : anhFiles) {
                String anhUrl = saveImage(anhFile);
                Anh anh = new Anh();
                LocalDateTime currentTime = LocalDateTime.now();
                anh.setTenanh(anhUrl);
                anh.setNgaytao(currentTime);
                anh.setSanphamchitiet(spct);
                anhRepository.save(anh);
            }
        }
        return "redirect:/listsanpham";
    }

    private String saveImage(MultipartFile file) {
        String uploadDir = "G:\\Ki7\\DATN\\DATN\\src\\main\\resources\\static\\upload";
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFileName = file.getOriginalFilename();
            String filePath = uploadDir + File.separator + originalFileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/detailsanpham/{id}")
    public String detailsanpham(@PathVariable Integer id, Model model) {
        SanPham sanPham = sanPhamRepositoty.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("sanphamchitiet", sanPham.getSpct());
        return "admin/qlchitietsanpham";
    }
}
