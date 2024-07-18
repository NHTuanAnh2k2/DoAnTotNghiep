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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.getAll();
        List<MauSac> listMauSac = mauSacRepository.getAll();
        List<KichCo> listKichCo = kichCoRepository.getAll();
        List<DeGiay> listDeGiay = deGiayRepository.getAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.getAll();
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
        List<DeGiay> listDG = deGiayRepository.findAll();
        model.addAttribute("listDG", listDG);
        List<ThuongHieu> listTH = thuongHieuRepository.findAll();
        model.addAttribute("listTH", listTH);
        List<ChatLieu> listCL = chatLieuRepository.findAll();
        model.addAttribute("listCL", listCL);
        List<MauSac> listMS = mauSacRepository.findAll();
        model.addAttribute("listMS", listMS);
        List<KichCo> listKC = kichCoRepository.findAll();
        model.addAttribute("listKC", listKC);

        List<SanPham> listSanPham = sanPhamImp.findAll();
        List<SanPhamChiTiet> listSPCT = sanPhamChiTietImp.findAll();
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.getAll();
        List<MauSac> listMauSac = mauSacRepository.getAll();
        List<KichCo> listKichCo = kichCoRepository.getAll();
        List<DeGiay> listDeGiay = deGiayRepository.getAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.getAll();
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
                             @RequestParam(name = "spctIds") Integer spctId,
                             RedirectAttributes redirectAttributes) {
        int doDaiChuoi = 10;
        String kiTu = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        for (int i = 0; i < doDaiChuoi; i++) {
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        LocalDateTime currentTime = LocalDateTime.now();

        // Lấy các thực thể từ cơ sở dữ liệu dựa trên ID từ view
        ThuongHieu thuongHieu = thuongHieuRepository.findById(sanPhamChiTiet.getThuonghieu().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));
        ChatLieu chatLieu = chatLieuRepository.findById(sanPhamChiTiet.getChatlieu().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));
        DeGiay deGiay = deGiayRepository.findById(sanPhamChiTiet.getDegiay().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đế giày"));
        MauSac mauSac = mauSacRepository.findById(sanPhamChiTiet.getMausac().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));
        KichCo kichCo = kichCoRepository.findById(sanPhamChiTiet.getKichco().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích cỡ"));

        // Gán các thực thể này vào sanPhamChiTiet
        sanPhamChiTiet.setThuonghieu(thuongHieu);
        sanPhamChiTiet.setChatlieu(chatLieu);
        sanPhamChiTiet.setDegiay(deGiay);
        sanPhamChiTiet.setMausac(mauSac);
        sanPhamChiTiet.setKichco(kichCo);

        // Lưu thực thể SanPham nếu nó chưa được lưu
        SanPham sanPham = sanPhamChiTiet.getSanpham();
        if (sanPham.getId() == null) {
            sanPham.setMasanpham(chuoiNgauNhien.toString());
            sanPham.setTrangthai(true);
            sanPham.setNgaytao(currentTime);
            sanPham.setLancapnhatcuoi(currentTime);
            sanPhamRepositoty.save(sanPham);
        }

        sanPhamChiTiet.setId(id);
        sanPhamChiTiet.setMasanphamchitiet(chuoiNgauNhien.toString());
        sanPhamChiTiet.setNgaytao(currentTime);
        sanPhamChiTiet.setLancapnhatcuoi(currentTime);

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
        redirectAttributes.addFlashAttribute("success", true);
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