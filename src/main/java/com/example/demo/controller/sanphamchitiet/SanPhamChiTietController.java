package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamChiTietInfo;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.*;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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
    @Autowired
    AnhRepository anhRepository;

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
    public String viewupdateCTSP(@PathVariable Integer id, Model model,
                                 @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                                 @ModelAttribute("chatlieu") ChatLieu chatLieu,
                                 @ModelAttribute("kichco") KichCo kichCo,
                                 @ModelAttribute("degiay") DeGiay deGiay,
                                 @ModelAttribute("mausac") MauSac mauSac,
                                 @ModelAttribute("tim") ThuocTinhInfo info

    ) {
        List<DeGiay> listDG = null;
        if (info.getKey() != null) {
            listDG = deGiayImp.getDeGiayByTen(info.getKey());
        } else {
            listDG = deGiayRepository.getAll();
        }
        model.addAttribute("listDG", listDG);
        List<ThuongHieu> listTH = null;
        if (info.getKey() != null) {
            listTH = thuongHieuImp.getThuongHieuByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            listTH = thuongHieuRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("listTH", listTH);

        List<ChatLieu> listCL = null;
        if (info.getKey() != null) {
            listCL = chatLieuRepository.getChatLieuByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            listCL = chatLieuRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("listCL", listCL);
        List<MauSac> listMS = null;
        if (info.getKey() != null) {
            listMS = mauSacRepository.getDeGiayByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            listMS = mauSacRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("listMS", listMS);

        List<KichCo> listKC = null;
        if (info.getKey() != null) {
            listKC = kichCoRepository.getKichCoByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            listKC = kichCoRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("listKC", listKC);

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
    public String updateCTSP(@PathVariable Integer id, @ModelAttribute("hehe") SanPhamChiTiet sanPhamChiTiet,
                             @RequestParam(name = "anhs") List<MultipartFile> anhFiles,
                             @RequestParam(name = "spctIds") Integer spctId
    ) {
        int doDaiChuoi = 10;
        // Chuỗi chứa tất cả các ký tự có thể có trong chuỗi ngẫu nhiên
        String kiTu = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        // Tạo đối tượng Random
        Random random = new Random();
        // StringBuilder để xây dựng chuỗi ngẫu nhiên
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        // Lặp để thêm ký tự ngẫu nhiên vào chuỗi
        for (int i = 0; i < doDaiChuoi; i++) {
            // Lấy một ký tự ngẫu nhiên từ chuỗi kiTu và thêm vào chuỗi ngẫu nhiên
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        LocalDateTime currentTime = LocalDateTime.now();
        sanPhamChiTiet.setId(id);
        sanPhamChiTiet.setMasanphamchitiet(chuoiNgauNhien.toString());
        sanPhamChiTiet.setNgaytao(currentTime);
        sanPhamChiTiet.setLancapnhatcuoi(currentTime);
        SanPham sanPham = sanPhamChiTiet.getSanpham();
        sanPham.setId(id);
        sanPham.setMasanpham(chuoiNgauNhien.toString());
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
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId).orElse(null);
        if (spct != null) {
            for (MultipartFile anhFile : anhFiles) {
                String anhUrl = saveImage(anhFile);
                Anh anh = new Anh();
                anh.setTenanh(anhUrl);
                anh.setTrangthai(true);
                anh.setNgaytao(currentTime);
                anh.setLancapnhatcuoi(currentTime);
                anh.setSanphamchitiet(spct);
                anhRepository.save(anh);
            }
        }

        Integer firstProductId = sanPhamChiTiet.getSanpham().getId();
        return "redirect:/detailsanpham/" + firstProductId;
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