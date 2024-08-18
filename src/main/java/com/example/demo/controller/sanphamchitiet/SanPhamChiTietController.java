package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamChiTietInfo;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.*;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpSession;
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
import java.util.stream.Collectors;


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

    @Autowired
    NhanVienRepository nhanvienRPo;

    @Autowired
    NguoiDungRepository daoNguoiDung;

    //Cập nhật trạng thái sản phẩm chi tiết

    @PostMapping("/chi-tiet-san-pham/updateTrangThai/{id}")
    public String updateTrangThaiCTSP(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id).orElse(null);
        if (spct != null) {
            spct.setTrangthai(!spct.getTrangthai());
            sanPhamChiTietRepository.save(spct);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/detailsanpham/" + id;
    }


    @GetMapping("/allSPCT")
    public String allSPCT(Model model, @ModelAttribute("search") SanPhamChiTietInfo info) {
        List<SanPhamChiTiet> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isAllFiltersNull = (
                isKeyEmpty &&
                        info.getIdChatLieu() == null &&
                        info.getIdDeGiay() == null &&
                        info.getIdKichCo() == null &&
                        info.getIdMauSac() == null &&
                        info.getIdThuongHieu() == null &&
                        info.getGioitinh() == null &&
                        info.getTrangthai() == null
        );
        if (isAllFiltersNull) {
            list = sanPhamChiTietRepository.findAll();
        } else {
            list = sanPhamChiTietRepository.search(
                    "%" + trimmedKey + "%",
                    "%" + trimmedKey + "%",
                    info.getIdThuongHieu(),
                    info.getIdDeGiay(),
                    info.getIdKichCo(),
                    info.getIdMauSac(),
                    info.getIdChatLieu(),
                    info.getGioitinh(),
                    info.getTrangthai()
            );
        }
        for (SanPhamChiTiet chiTiet : list) {
            chiTiet.setSanphamdotgiam(chiTiet.getSanphamdotgiam().stream()
                    .filter(dotGiam -> dotGiam.getDotgiamgia().getTrangthai() == 1)
                    .collect(Collectors.toList()));
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
        model.addAttribute("fillSearch", trimmedKey);
        model.addAttribute("fillThuongHieu", info.getIdThuongHieu());
        model.addAttribute("fillDeGiay", info.getIdDeGiay());
        model.addAttribute("fillKichCo", info.getIdKichCo());
        model.addAttribute("fillMauSac", info.getIdMauSac());
        model.addAttribute("fillChatLieu", info.getIdChatLieu());
        model.addAttribute("fillGioiTinh", info.getGioitinh());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/allchitietsanpham";
    }


    @GetMapping("/updateCTSP/{id}")
    public String viewupdateCTSP(@PathVariable Integer id, Model model,
                                 @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                                 @ModelAttribute("chatlieu") ChatLieu chatLieu,
                                 @ModelAttribute("kichco") KichCo kichCo,
                                 @ModelAttribute("degiay") DeGiay deGiay,
                                 @ModelAttribute("mausac") MauSac mauSac,
                                 @ModelAttribute("tim") ThuocTinhInfo info

    ) {
        //dùng cho validate
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

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietImp.findById(id);
        BigDecimal giatrigiam = sanPhamChiTiet.getGiamGia();
        BigDecimal giagoc = sanPhamChiTiet.getGiatien();
        BigDecimal phantramgiam = giatrigiam.divide(new BigDecimal(100));
        BigDecimal sotiengiam = giagoc.multiply(phantramgiam);
        BigDecimal giamoi = giagoc.subtract(sotiengiam);
        model.addAttribute("hehe", sanPhamChiTiet);
        model.addAttribute("giatrigiam", giatrigiam);
        model.addAttribute("giamoi", giamoi);
        return "admin/detailCTSP";
    }


    @GetMapping("/updateAllCTSP/{id}")
    public String viewupdateAllCTSP(@PathVariable Integer id, Model model,
                                 @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                                 @ModelAttribute("chatlieu") ChatLieu chatLieu,
                                 @ModelAttribute("kichco") KichCo kichCo,
                                 @ModelAttribute("degiay") DeGiay deGiay,
                                 @ModelAttribute("mausac") MauSac mauSac,
                                 @ModelAttribute("tim") ThuocTinhInfo info

    ) {
        //dùng cho validate
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
        model.addAttribute("AllCTSP", sanPhamChiTietImp.findById(id));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietImp.findById(id);
        BigDecimal giatrigiam = sanPhamChiTiet.getGiamGia();
        BigDecimal giagoc = sanPhamChiTiet.getGiatien();
        BigDecimal phantramgiam = giatrigiam.divide(new BigDecimal(100));
        BigDecimal sotiengiam = giagoc.multiply(phantramgiam);
        BigDecimal giamoi = giagoc.subtract(sotiengiam);
        model.addAttribute("hehe", sanPhamChiTiet);
        model.addAttribute("giatrigiam", giatrigiam);
        model.addAttribute("giamoi", giamoi);
        return "admin/ViewAllCTSP";
    }


    @PostMapping("/updateCTSP/{id}")
    public String updateCTSP(@PathVariable Integer id, @ModelAttribute("hehe") SanPhamChiTiet sanPhamChiTiet,
                             @RequestParam(name = "anhs", required = false) List<MultipartFile> anhFiles,
                             @RequestParam(name = "spctIds") Integer spctId,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        LocalDateTime currentTime = LocalDateTime.now();
        SanPham sanPham = sanPhamRepositoty.findById(sanPhamChiTiet.getSanpham().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
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
        SanPhamChiTiet existingSanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

        existingSanPhamChiTiet.setSanpham(sanPham);
        existingSanPhamChiTiet.setThuonghieu(thuongHieu);
        existingSanPhamChiTiet.setChatlieu(chatLieu);
        existingSanPhamChiTiet.setDegiay(deGiay);
        existingSanPhamChiTiet.setMausac(mauSac);
        existingSanPhamChiTiet.setKichco(kichCo);
        existingSanPhamChiTiet.setLancapnhatcuoi(currentTime);
        existingSanPhamChiTiet.setSoluong(sanPhamChiTiet.getSoluong());
        existingSanPhamChiTiet.setGiatien(sanPhamChiTiet.getGiatien());
        existingSanPhamChiTiet.setMota(sanPhamChiTiet.getMota());
        existingSanPhamChiTiet.setTrangthai(sanPhamChiTiet.getTrangthai());
        existingSanPhamChiTiet.setGioitinh(sanPhamChiTiet.getGioitinh());
        existingSanPhamChiTiet.setNguoitao(nv.getNguoidung().getHovaten());
        existingSanPhamChiTiet.setNguoicapnhat(nv.getNguoidung().getHovaten());

        sanPhamChiTietRepository.save(existingSanPhamChiTiet);
        if (anhFiles != null && !anhFiles.isEmpty()) {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId).orElse(null);
            if (spct != null) {
                for (MultipartFile anhFile : anhFiles) {
                    if (!anhFile.isEmpty()) {
                        String anhUrl = saveImage(anhFile);
                        Anh anh = new Anh();
                        anh.setTenanh(anhUrl);
                        anh.setTrangthai(true);
                        anh.setNgaytao(currentTime);
                        anh.setLancapnhatcuoi(currentTime);
                        anh.setSanphamchitiet(spct);
                        anh.setNguoitao(nv.getNguoidung().getHovaten());
                        anh.setNguoicapnhat(nv.getNguoidung().getHovaten());
                        anhRepository.save(anh);
                    }
                }
            }
        }
        Integer firstProductId = sanPhamChiTiet.getSanpham().getId();
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/detailsanpham/" + firstProductId;
    }



    @PostMapping("/updateAllCTSP/{id}")
    public String updateAllCTSP(@PathVariable Integer id, @ModelAttribute("AllCTSP") SanPhamChiTiet sanPhamChiTiet,
                             @RequestParam(name = "anhs", required = false) List<MultipartFile> anhFiles,
                             @RequestParam(name = "spctIds") Integer spctId,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        String username = (String) session.getAttribute("adminDangnhap");
        NguoiDung ndung = daoNguoiDung.findNguoiDungByTaikhoan(username);
        List<NhanVien> lstnvtimve = nhanvienRPo.findByNguoidung(ndung);
        NhanVien nv = lstnvtimve.get(0);

        LocalDateTime currentTime = LocalDateTime.now();
        SanPham sanPham = sanPhamRepositoty.findById(sanPhamChiTiet.getSanpham().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
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
        SanPhamChiTiet existingSanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

        existingSanPhamChiTiet.setSanpham(sanPham);
        existingSanPhamChiTiet.setThuonghieu(thuongHieu);
        existingSanPhamChiTiet.setChatlieu(chatLieu);
        existingSanPhamChiTiet.setDegiay(deGiay);
        existingSanPhamChiTiet.setMausac(mauSac);
        existingSanPhamChiTiet.setKichco(kichCo);
        existingSanPhamChiTiet.setLancapnhatcuoi(currentTime);
        existingSanPhamChiTiet.setSoluong(sanPhamChiTiet.getSoluong());
        existingSanPhamChiTiet.setGiatien(sanPhamChiTiet.getGiatien());
        existingSanPhamChiTiet.setMota(sanPhamChiTiet.getMota());
        existingSanPhamChiTiet.setTrangthai(sanPhamChiTiet.getTrangthai());
        existingSanPhamChiTiet.setGioitinh(sanPhamChiTiet.getGioitinh());
        existingSanPhamChiTiet.setNguoitao(nv.getNguoidung().getHovaten());
        existingSanPhamChiTiet.setNguoicapnhat(nv.getNguoidung().getHovaten());

        sanPhamChiTietRepository.save(existingSanPhamChiTiet);
        if (anhFiles != null && !anhFiles.isEmpty()) {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId).orElse(null);
            if (spct != null) {
                for (MultipartFile anhFile : anhFiles) {
                    if (!anhFile.isEmpty()) {
                        String anhUrl = saveImage(anhFile);
                        Anh anh = new Anh();
                        anh.setTenanh(anhUrl);
                        anh.setTrangthai(true);
                        anh.setNgaytao(currentTime);
                        anh.setLancapnhatcuoi(currentTime);
                        anh.setSanphamchitiet(spct);
                        anh.setNguoitao(nv.getNguoidung().getHovaten());
                        anh.setNguoicapnhat(nv.getNguoidung().getHovaten());
                        anhRepository.save(anh);
                    }
                }
            }
        }
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/allSPCT";
    }

    //    private String saveImage(MultipartFile file) {
//        String uploadDir = "G:\\Ki7\\DATN\\DATN\\src\\main\\resources\\static\\upload";
//        try {
//            File directory = new File(uploadDir);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//            String originalFileName = file.getOriginalFilename();
//            String filePath = uploadDir + File.separator + originalFileName;
//            File dest = new File(filePath);
//            file.transferTo(dest);
//            return filePath;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    private String saveImage(MultipartFile file) {
        String uploadDir = "D:\\DATN\\src\\main\\resources\\static\\upload";
        String dbUploadDir = "/upload";
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFileName = file.getOriginalFilename();
            String filePath = uploadDir + File.separator + originalFileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return dbUploadDir + "/" + originalFileName;
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