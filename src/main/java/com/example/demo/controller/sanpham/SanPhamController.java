package com.example.demo.controller.sanpham;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamInfo;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    HttpServletRequest request;

    //Hiển thị list sản phẩm
    @GetMapping("/listsanpham")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("tim") SanPhamInfo info, Model model) {
        Pageable pageable = PageRequest.of(p, 20);
        Page<SanPham> page = null;
        Map<Integer, String> randomProductIds = new HashMap<>(); // Map để lưu trữ mã sản phẩm ngẫu nhiên

        if (info.getKey() != null) {
            page = sanPhamImp.findAllByTensanphamOrTrangthai(info.getKey(), info.getTrangthai(), pageable);
        } else {
            page = sanPhamRepositoty.findAllByOrderByNgaytaoDesc(pageable);
        }

        // Tạo mã sản phẩm ngẫu nhiên cho mỗi sản phẩm và lưu vào Map
        for (SanPham sp : page.getContent()) {
            Random random = new Random();
            int randomId = random.nextInt(1000000000);
            randomProductIds.put(sp.getId(), "SP" + String.format("%010d", randomId));
        }

        model.addAttribute("page", page);
        model.addAttribute("randomProductIds", randomProductIds); // Thêm Map vào Model
        return "admin/qlsanpham";
    }

    //hiển thị các thuộc tính của sản phẩm thông qua modelAttribute
    @RequestMapping(value = { "/viewaddSP", "/viewaddSP" }, method = { RequestMethod.GET, RequestMethod.POST })
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
        Pageable pageable = PageRequest.of(p, 20);
        Page<SanPhamChiTiet> page = sanPhamChiTietImp.finAllPage(pageable);
        model.addAttribute("page", page);
        return "admin/addsanpham";
    }
    @PostMapping("/addProduct")
//    @CacheEvict(value = "sanphamCache", allEntries = true)
    public String addProduct(@RequestParam(defaultValue = "0") int p, Model model, @RequestParam String tensp,
                             @RequestParam String mota,
                             @RequestParam Boolean trangthai,
                             @RequestParam ThuongHieu idThuongHieu,
                             @RequestParam ChatLieu idChatLieu,
                             @RequestParam Boolean gioitinh,
                             @RequestParam(name = "kichCoId") List<String> kichCoNames,
                             @RequestParam DeGiay idDeGiay,
                             @RequestParam List<MauSac> idMauSac
    ) {
        SanPham sanPham = new SanPham();
        sanPham.setTensanpham(tensp);
        sanPham.setTrangthai(trangthai);
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
                    spct.setTrangthai(trangthai);
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

    @GetMapping("/detailsanpham/{id}")
    public String detailsanpham(@PathVariable Integer id,Model model) {
     SanPham sanPham=sanPhamRepositoty.findById(id).orElse(null);
        model.addAttribute("sanpham",sanPham);
        model.addAttribute("sanphamchitiet", sanPham.getSpct());
        return "admin/qlchitietsanpham";
    }
}
