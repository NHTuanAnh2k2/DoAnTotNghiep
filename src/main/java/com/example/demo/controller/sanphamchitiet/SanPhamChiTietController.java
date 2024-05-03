package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamChiTietInfo;
import com.example.demo.repository.*;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

///
@Controller
public class SanPhamChiTietController {
    @Autowired
    ChatLieuRepository chatLieuRepository;
    @Autowired
    MauSacRepository mauSacRepository;
    @Autowired
    KichCoRepository kichCoRepository;
    @Autowired
    DeGiayRepository deGiayRepository;
    @Autowired
    ThuongHieuRepository thuongHieuRepository;
    @Autowired
    SanPhamRepositoty sanPhamRepositoty;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPhamImp sanPhamImp;
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

    @GetMapping("/allSPCT")
    public String allSPCT(Model model, @ModelAttribute("search") SanPhamChiTietInfo info) {
        List<SanPhamChiTiet> list = null;
        if (info.getKey() == null && info.getIdChatLieu() == null && info.getIdDeGiay() == null && info.getIdKichCo() == null
                && info.getIdMauSac() == null && info.getIdThuongHieu() == null && info.getGioitinh() == null) {
            list = sanPhamChiTietRepository.findAll();
        } else {
            list = sanPhamChiTietRepository.search(
                    "%" + info.getKey() + "%",
                    info.getIdThuongHieu(),
                    info.getIdDeGiay(),
                    info.getIdKichCo(),
                    info.getIdMauSac(),
                    info.getIdChatLieu(),
                    info.getGioitinh());
        }
        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuImp.findAll();
        List<MauSac> listMauSac = mauSacImp.findAll();
        List<KichCo> listKichCo = kichCoImp.findAll();
        List<DeGiay> listDeGiay = deGiayImp.findAll();
        List<ChatLieu> listChatLieu = chatLieuImp.findAll();
        List<SanPhamChiTiet> listSanPhamChiTiet = sanPhamChiTietRepository.findAll();
        model.addAttribute("sp", listSanPham);
        model.addAttribute("th", listThuongHieu);
        model.addAttribute("ms", listMauSac);
        model.addAttribute("kc", listKichCo);
        model.addAttribute("dg", listDeGiay);
        model.addAttribute("cl", listChatLieu);
        model.addAttribute("spct", listSanPhamChiTiet);
        model.addAttribute("listAllCTSP", list);
        return "admin/allchitietsanpham";
    }

//    @GetMapping("/deleteCTSP/{id}")
//    public String deleteCTSP(@PathVariable Integer id, Model model) {
//        sanPhamChiTietImp.deleteSPCT(id);
//        // Cập nhật lại danh sách sản phẩm chi tiết
////        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findAll();
////        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
////        SanPham sanPham=new SanPham();
////        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findBySanPhamId(sanPham.getId());
////        model.addAttribute("sanphamchitiet", sanPhamChiTiets);
//        SanPham sanPham = sanPhamRepositoty.findFirstByOrderByNgaytaoDesc();
//        List<SanPhamChiTiet> listSPCT = sanPhamChiTietRepository.findBySanPhamId(sanPham.getId());
//        model.addAttribute("sanphamchitiet", listSPCT);
//        return "forward:/viewaddSPPOST";
////        return "admin/addsanpham";
//    }


    @GetMapping("/updateCTSP/{id}")
    public String viewupdateCTSP(@PathVariable Integer id, Model model) {
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
        model.addAttribute("hehe", sanPhamChiTietImp.findById(id));
        return "admin/detailCTSP";
    }

    @PostMapping("/updateCTSP/{id}")
    public String updateCTSP(@PathVariable Integer id, @ModelAttribute("hehe") SanPhamChiTiet sanPhamChiTiet) {
        LocalDateTime currentTime = LocalDateTime.now();
        sanPhamChiTiet.setId(id);
        sanPhamChiTiet.setNgaytao(currentTime);
        sanPhamChiTiet.setLancapnhatcuoi(currentTime);
        SanPham sanPham = sanPhamChiTiet.getSanpham();
        sanPham.setId(id);
        sanPham.setTrangthai(true);
        sanPham.setNgaytao(currentTime);
        sanPham.setLancapnhatcuoi(currentTime);
        sanPham.setTensanpham(sanPhamChiTiet.getSanpham().getTensanpham());
        sanPhamRepositoty.save(sanPham);
        ThuongHieu thuongHieu = sanPhamChiTiet.getThuonghieu();
        thuongHieu.setId(id);
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieu.setTen(sanPhamChiTiet.getThuonghieu().getTen());
        thuongHieuRepository.save(thuongHieu);
        ChatLieu chatLieu = sanPhamChiTiet.getChatlieu();
        chatLieu.setId(id);
        chatLieu.setTrangthai(true);
        chatLieu.setNgaytao(currentTime);
        chatLieu.setLancapnhatcuoi(currentTime);
        chatLieu.setTen(sanPhamChiTiet.getChatlieu().getTen());
        chatLieuRepository.save(chatLieu);
        DeGiay deGiay = sanPhamChiTiet.getDegiay();
        deGiay.setId(id);
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiay.setTen(sanPhamChiTiet.getDegiay().getTen());
        deGiayRepository.save(deGiay);
        MauSac mauSac = sanPhamChiTiet.getMausac();
        mauSac.setId(id);
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSac.setTen(sanPhamChiTiet.getMausac().getTen());
        mauSacRepository.save(mauSac);
        KichCo kichCo = sanPhamChiTiet.getKichco();
        kichCo.setId(id);
        kichCo.setTrangthai(true);
        kichCo.setNgaytao(currentTime);
        kichCo.setLancapnhatcuoi(currentTime);
        kichCo.setTen(sanPhamChiTiet.getKichco().getTen());
        kichCoRepository.save(kichCo);
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        Integer firstProductId = sanPhamChiTiet.getSanpham().getId();
        return "redirect:/detailsanpham/" + firstProductId;
    }

    @PostMapping("/updateGiaAndSoLuongCTSP")
    public String updateGiaAndSoLuong(
            Model model,
            @RequestParam("soluong") Integer soluong,
            @RequestParam("giatien") BigDecimal giatien,
            @RequestParam("choncheckbox") String[] choncheckbox
    ) {
        List<String> listString = Arrays.asList(choncheckbox);
        List<Integer> listInt = new ArrayList<>();
        for (String s : listString) {
            Integer i = Integer.parseInt(s);
            listInt.add(i);
        }
        listInt.remove(Integer.valueOf(-1));
        Integer firstProductId = null;
        if (!listInt.isEmpty()) {
            Integer firstSPCTId = listInt.get(0);
            firstProductId = sanPhamChiTietRepository.findIdBySanpham(firstSPCTId);
        }
        for (Integer id : listInt) {
            sanPhamChiTietRepository.updateSoLuongVaGiaTienById(id, soluong, giatien);
        }
        return "redirect:/detailsanpham/" + firstProductId;
    }
}