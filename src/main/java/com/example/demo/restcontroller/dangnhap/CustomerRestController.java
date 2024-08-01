package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.LichSuHoaDon.LichSuHoaDonRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;


    @GetMapping("/huydonhang/{id}")
    public ResponseEntity<?> huydonhang(@PathVariable("id") Integer id,
                                        @RequestParam("lydohuy") String lydohuy,
                                        RedirectAttributes redirectAttributes) {
        HoaDon hoaDon = hoaDonRepository.findHoaDonById(id);
        hoaDon.setTrangthai(6);
        hoaDon.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        hoaDon.setNgaygiaodukien(null);
        hoaDon.setNgayxacnhan(null);
        hoaDon.setNgayvanchuyen(null);
        hoaDon.setNgayhoanthanh(null);
        hoaDon.setNguoicapnhat("CUSTOMER");
        hoaDonRepository.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        lichSuHoaDon.setNgaytao(Timestamp.valueOf(LocalDateTime.now()));
        lichSuHoaDon.setNhanvien(hoaDon.getNhanvien());
        lichSuHoaDon.setHoadon(hoaDon);
        lichSuHoaDon.setNguoicapnhat("CUSTOMER");
        lichSuHoaDon.setNguoitao("CUSTOMER");
        lichSuHoaDon.setGhichu(lydohuy);
        lichSuHoaDon.setTrangthai(6);
        lichSuHoaDonRepository.save(lichSuHoaDon);
        redirectAttributes.addFlashAttribute("huyhangSuccess", true);
        return ResponseEntity.ok("Thành công");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("anh") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Bạn chưa chọn file nào.", HttpStatus.BAD_REQUEST);
        }

        String UPLOAD_DIR = "D:\\DATN\\src\\main\\resources\\static\\upload";

        try {
            // Đảm bảo thư mục upload tồn tại
            File uploadDirectory = new File(UPLOAD_DIR);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // Tạo đường dẫn file đầy đủ
            String originalFilename = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + File.separator + originalFilename;
            File dest = new File(filePath);
            file.transferTo(dest);

            // Tạo đường dẫn tương đối để lưu vào cơ sở dữ liệu
            String relativeFilePath = "/upload/" + originalFilename;

            // Cập nhật thông tin ảnh trong cơ sở dữ liệu
            String username = (String) session.getAttribute("userDangnhap");
            if (username != null) {
                NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(username);
                nd.setAnh(relativeFilePath);
                nd.setNguoicapnhat("CUSTOMER");
                nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
                nguoiDungRepository.save(nd);
            }

            // Trả về phản hồi thành công
            return new ResponseEntity<>("Upload thành công " + relativeFilePath, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Upload thất bại: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clearLogoutNotification")
    public ResponseEntity<Void> clearLogoutNotification(HttpSession session) {
        session.removeAttribute("notificationLogout");
        return ResponseEntity.ok().build();
    }

}
