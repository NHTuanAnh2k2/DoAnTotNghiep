package com.example.demo.controller.sanpham;

import com.example.demo.entity.*;
import com.example.demo.info.SanPhamInfo;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.*;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Controller
public class SanPhamController {
    @Autowired
    MauSacRepository mauSacRepository;
    @Autowired
    ChatLieuRepository chatLieuRepository;
    @Autowired
    ThuongHieuRepository thuongHieuRepository;
    @Autowired
    DeGiayRepository deGiayRepository;
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

    private String taoChuoiNgauNhien(int doDaiChuoi, String kiTu) {
        Random random = new Random();
        StringBuilder chuoiNgauNhien = new StringBuilder(doDaiChuoi);
        for (int i = 0; i < doDaiChuoi; i++) {
            chuoiNgauNhien.append(kiTu.charAt(random.nextInt(kiTu.length())));
        }
        return chuoiNgauNhien.toString();
    }

    @PostMapping("/addTenSPModal")
    public String addTenSPModel(Model model, @ModelAttribute("sanpham") SanPham sanPham) {
        String chuoiNgauNhien = taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        LocalDateTime currentTime = LocalDateTime.now();
        String maSanPham = "SP" + chuoiNgauNhien;

        // Trim tên sản phẩm và thay thế nhiều khoảng trắng liên tiếp bằng một khoảng trắng
        String trimmedTenSanPham = (sanPham.getTensanpham() != null)
                ? sanPham.getTensanpham().trim().replaceAll("\\s+", " ")
                : null;
        sanPham.setTensanpham(trimmedTenSanPham);

        sanPham.setTrangthai(true);
        sanPham.setMasanpham(maSanPham);
        sanPham.setNgaytao(currentTime);
        sanPham.setLancapnhatcuoi(currentTime);
        sanPham.setNguoitao("DuyNV");
        sanPham.setNguoicapnhat("DuyNV");
        sanPham.setQrcode("ABC");
        sanPhamRepositoty.save(sanPham);
        return "redirect:/viewaddSPGET";
    }


    @GetMapping("/listsanpham")
    public String hienthi(Model model, @ModelAttribute("tim") SanPhamInfo info) {
        List<Object[]> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            list = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc();
        } else {
            list = sanPhamRepositoty.findByMasanphamAndTenSanPhamAndTrangThai("%" + trimmedKey + "%", "%" + trimmedKey + "%", info.getTrangthai());
        }
        model.addAttribute("list", list);
        model.addAttribute("fillSearch", trimmedKey);
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/qlsanpham";
    }


    @RequestMapping(value = {"/viewaddSPGET", "/viewaddSPPOST"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String viewaddSP(Model model, @RequestParam(defaultValue = "0") int p,
                            @ModelAttribute("thuonghieu") ThuongHieu thuongHieu,
                            @ModelAttribute("chatlieu") ChatLieu chatLieu,
                            @ModelAttribute("kichco") KichCo kichCo,
                            @ModelAttribute("degiay") DeGiay deGiay,
                            @ModelAttribute("mausac") MauSac mauSac,
                            @ModelAttribute("sanpham") SanPham sanpham,
                            @ModelAttribute("tim") ThuocTinhInfo info
    ) {
        List<SanPham> listSanPham = sanPhamRepositoty.getAllByNgayTao();
        List<SanPhamChiTiet> listSPCT = sanPhamChiTietRepository.findAll();
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
        return "admin/addsanpham";
    }

    List<SanPhamChiTiet> sanPhamChiTietList = new ArrayList<>();


    @PostMapping("/addProduct")
    public String addProduct(@RequestParam(defaultValue = "0") int p, Model model,
                             @RequestParam Integer tensp,
                             @RequestParam String mota,
                             @RequestParam ThuongHieu idThuongHieu,
                             @RequestParam ChatLieu idChatLieu,
                             @RequestParam Boolean gioitinh,
                             @RequestParam(name = "kichCoId") List<String> kichCoNames,
                             @RequestParam DeGiay idDeGiay,
                             @RequestParam List<MauSac> idMauSac
    ) {
        String trimmedMota = (mota != null) ? mota.trim().replaceAll("\\s+", " ") : null;
        model.addAttribute("selectedTensp", tensp);
        model.addAttribute("motas", mota);
        model.addAttribute("gioitinh", gioitinh);
        model.addAttribute("selectedThuongHieu", idThuongHieu.getId());
        model.addAttribute("selectedChatLieu", idChatLieu.getId());
        model.addAttribute("gioitinh", gioitinh);
        model.addAttribute("kichCoNames", kichCoNames);
        SanPham sanPham = sanPhamRepositoty.findById(tensp).orElse(null);
        if (sanPham == null) {
            return "redirect:/error";
        }
        Integer nextId2 = sanPhamChiTietRepository.findMaxIdSPCT();
        if (nextId2 == null) {
            return "redirect:/error";
        }
        if (sanPhamChiTietList.size() <= 0) {
            for (MauSac colorId : idMauSac) {
                for (String sizeName : kichCoNames) {
                    KichCo kichCo = kichCoRepository.findByTen(sizeName);
                    if (kichCo != null) {
                        String chuoiNgauNhien = taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                        String maSanPhamCT = "SPCT" + chuoiNgauNhien;
                        nextId2++;
                        SanPhamChiTiet spct = new SanPhamChiTiet();
                        spct.setId(nextId2);
                        spct.setMasanphamchitiet(maSanPhamCT);
                        spct.setSanpham(sanPham);
                        spct.setSoluong(1);
                        spct.setGiatien(BigDecimal.valueOf(100000.000));
                        spct.setMota(trimmedMota);
                        spct.setThuonghieu(idThuongHieu);
                        spct.setChatlieu(idChatLieu);
                        spct.setGioitinh(gioitinh);
                        spct.setTrangthai(true);
                        spct.setKichco(kichCo);
                        spct.setDegiay(idDeGiay);
                        spct.setMausac(colorId);
                        sanPhamChiTietList.add(spct);

                        for (SanPhamChiTiet spcts : sanPhamChiTietList) {
                            System.out.println("idspct:" + spcts.getId());
                            System.out.println("mausac:" + spcts.getKichco().getTen());
                            System.out.println("kichco:" + spcts.getMausac().getTen());
                        }
                    }
                }
            }

        } else {
            for (MauSac colorId : idMauSac) {
                for (String sizeName : kichCoNames) {
                    KichCo kichCo = kichCoRepository.findByTen(sizeName);
                    if (kichCo != null) {
                        boolean found = false;
                        if (sanPhamChiTietList != null) {
                            for (SanPhamChiTiet spct2 : sanPhamChiTietList) {
                                if (spct2.getMausac().getId() == colorId.getId() && spct2.getKichco().getTen().equals(sizeName)) {
                                    spct2.setSoluong(spct2.getSoluong() + 1);
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            String chuoiNgauNhien = taoChuoiNgauNhien(7, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                            String maSanPhamCT = "SPCT" + chuoiNgauNhien;
                            int lastIndex = sanPhamChiTietList.size() - 1;
                            SanPhamChiTiet lastItem = sanPhamChiTietList.get(lastIndex);
                            int count = lastItem.getId();
                            count++;
                            SanPhamChiTiet spct = new SanPhamChiTiet();
                            spct.setId(count);
                            spct.setSanpham(sanPham);
                            spct.setMasanphamchitiet(maSanPhamCT);
                            spct.setSoluong(1);
                            spct.setGiatien(BigDecimal.valueOf(100000.000));
                            spct.setMota(trimmedMota);
                            spct.setThuonghieu(idThuongHieu);
                            spct.setChatlieu(idChatLieu);
                            spct.setGioitinh(gioitinh);
                            spct.setTrangthai(true);
                            spct.setKichco(kichCo);
                            spct.setDegiay(idDeGiay);
                            spct.setMausac(colorId);
                            sanPhamChiTietList.add(spct);
                        }
                    }
                }
            }

        }
        model.addAttribute("sanphamchitiet", sanPhamChiTietList);
        return "forward:/viewaddSPPOST";
    }


    @GetMapping("/detailsanpham/{id}")
    public String detailsanpham(@PathVariable Integer id, Model model) {
        SanPham sanPham = sanPhamRepositoty.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("sanphamchitiet", sanPham.getSpct());
        return "admin/qlchitietsanpham";
    }


    @GetMapping("/deleteCTSP/{id}")
    public String deleteCTSP(@PathVariable Integer id, Model model) {
        for (Iterator<SanPhamChiTiet> iterator = sanPhamChiTietList.iterator(); iterator.hasNext(); ) {
            SanPhamChiTiet spct = iterator.next();
            if (spct.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        model.addAttribute("sanphamchitiet", sanPhamChiTietList);
        return "forward:/viewaddSPPOST";
    }


    @PostMapping("/addImage")
    public String addImage(
            @RequestParam(name = "anh1") List<MultipartFile> anhFiles1,
            @RequestParam(name = "anh2") List<MultipartFile> anhFiles2,
            @RequestParam(name = "anh3") List<MultipartFile> anhFiles3,
            @RequestParam(name = "spctId") List<Integer> spctIds,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        SanPham sanPham = sanPhamChiTietList.get(0).getSanpham();
        List<SanPhamChiTiet> listsanPhamChiTietDB = sanPhamChiTietRepository.findBySanpham(sanPham);

        if (listsanPhamChiTietDB.isEmpty()) {
            for (SanPhamChiTiet spct : sanPhamChiTietList) {
                sanPhamChiTietRepository.save(spct);
            }
        } else {
            for (SanPhamChiTiet spctList : sanPhamChiTietList) {
                SanPhamChiTiet spctTim = sanPhamChiTietRepository.findSPCT(
                        spctList.getMausac(), spctList.getKichco(), spctList.getThuonghieu(),
                        spctList.getChatlieu(), spctList.getDegiay(), spctList.getSanpham());
                if (spctTim != null) {
                    spctTim.setSoluong(spctTim.getSoluong() + spctList.getSoluong());
                    for (Anh anh : spctList.getAnh()) {
                        anh.setSanphamchitiet(spctTim);
                        anhRepository.save(anh);
                    }
                    sanPhamChiTietRepository.save(spctTim);
                } else {
                    sanPhamChiTietRepository.save(spctList);
                }
            }
        }
        sanPhamChiTietList.clear();
        for (int i = 0; i < spctIds.size(); i++) {
            Integer spctId = spctIds.get(i);
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId).orElse(null);
            if (spct != null) {
                MultipartFile anhFile1 = anhFiles1.get(i);
                addAnh(spct, anhFile1);
                MultipartFile anhFile2 = anhFiles2.get(i);
                addAnh(spct, anhFile2);
                MultipartFile anhFile3 = anhFiles3.get(i);
                addAnh(spct, anhFile3);
            }
        }
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/listsanpham";
    }


    private void addAnh(SanPhamChiTiet spct, MultipartFile anhFile) {
        if (!anhFile.isEmpty()) {
            String anhUrl = saveImage(anhFile);
            if (anhUrl != null) {
                Anh anh = new Anh();
                LocalDateTime currentTime = LocalDateTime.now();
                anh.setTenanh(anhUrl);
                anh.setNgaytao(currentTime);
                anh.setSanphamchitiet(spct);
                anhRepository.save(anh);
            }
        }
    }

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



    @PostMapping("/updateGiaAndSoLuong")
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
        for (Integer id : listInt) {
            for (SanPhamChiTiet sanPhamChiTiet : sanPhamChiTietList) {
                if (sanPhamChiTiet.getId().equals(id)) {
                    sanPhamChiTiet.setGiatien(giatien);
                    sanPhamChiTiet.setSoluong(soluong);
                }
            }
        }
        model.addAttribute("sanphamchitiet", sanPhamChiTietList);
        return "forward:/viewaddSPPOST";
    }


    @GetMapping("timaddImage")
    @ResponseBody
    public ResponseEntity<?> timaddImage(@RequestParam("image") List<String> listData
    ) {
        Anh anh = new Anh();
        String url = "D:\\DATN\\src\\main\\resources\\static\\upload\\" + listData.get(1);
        anh.setTenanh(url);
        List<Anh> list =sanPhamChiTietList.get(Integer.valueOf(listData.get(0)) - 1).getAnh()==null?new ArrayList<>():sanPhamChiTietList.get(Integer.valueOf(listData.get(0)) - 1).getAnh();
        list.add(anh);
        System.out.println("KKKKKKKKKKKKKK" + list.size());
        sanPhamChiTietList.get(Integer.valueOf(listData.get(0)) - 1).setAnh(list);
        return ResponseEntity.ok(true);
    }
}