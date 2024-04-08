package com.example.demo.service.impl;

import com.example.demo.entity.DiaChi;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.restcontroller.khachhang.GeoName;
import com.example.demo.restcontroller.khachhang.GeoNamesResponse;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class KhachHangImp implements KhachHangService, NguoiDungService {
    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    DiaChiRepository diaChiRepository;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public Page<KhachHang> findAllKhachHang(Pageable pageable) {
        return khachHangRepostory.findAll(pageable);
    }

    @Override
    public List<KhachHang> findAll() {
        return khachHangRepostory.findAll();
    }

    @Override
    public KhachHang add(KhachHang khachHang, NguoiDung nguoiDung, DiaChi diaChi, String tinhthanhpho) {
        int usernameLength = 8;
        int passwordLength = 10;
        String username = generateRandomPassword(usernameLength);
        String password = generateRandomPassword(passwordLength);
        nguoiDung.setTaikhoan(username);
        nguoiDung.setMatkhau(password);
        nguoiDungRepository.save(nguoiDung);

        diaChi.setSoduong(1);
        diaChi.setTenduong("test");
        diaChi.setXaphuong("test");
        diaChi.setQuanhuyen("test");
        diaChi.setSdtnguoinhan(nguoiDung.getSodienthoai());
        diaChi.setNguoidung(nguoiDung);
        diaChi.setTrangthai(nguoiDung.getTrangthai());
        diaChi.setTinhthanhpho(tinhthanhpho);
        diaChiRepository.save(diaChi);

        khachHang.setNguoidung(nguoiDung);
        khachHang.setTrangthai(nguoiDung.getTrangthai());
        khachHangRepostory.save(khachHang);
        String nguoiNhan = diaChi.getNguoidung().getEmail();
        String tenNguoiNhan = nguoiDung.getHovaten();
        this.sendEmail(nguoiNhan, username, password, tenNguoiNhan);
        return khachHang;
    }

    public static String generateRandomPassword(int length) {
        if (length < 4) throw new IllegalArgumentException("Length too short, minimum 4 characters required");
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
        }
        return password.toString();
    }

    public void sendEmail(String recipient, String username, String password, String name) {
        // Cấu hình thông tin email
        String host = "smtp.gmail.com";
        String port = "587";
        String senderEmail = "thinhdqph28839@fpt.edu.vn";
        String senderPassword = "xxefjtjmafszpdch";

        // Cấu hình cài đặt
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo phiên gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Thiết lập người nhận
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Thiết lập tiêu đề
            message.setSubject("Thông tin tài khoản mới");

            // Thiết lập nội dung email
            String emailContent = "Xin chào, " + name + "\n\n";
            emailContent += "Đây là thông tin tài khoản của bạn:\n";
            emailContent += "Tài khoản: " + username + "\n";
            emailContent += "Mật khẩu: " + password + "\n\n";
            emailContent += "Hãy sử dụng thông tin này để đăng nhập vào hệ thống của chúng tôi.\n\n";
            emailContent += "Trân trọng,\n";
            emailContent += "Đỗ Quốc Thịnh";

            // Thiết lập nội dung
            message.setText(emailContent);

            // Gửi email
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    @Override
    public List<KhachHang> findByAll(String ten, String sdt, int trangthai, Date ngaysinh) {
        return khachHangRepostory.findByAll(ten, sdt, trangthai, ngaysinh);
    }

    @Override
    public KhachHang getOne(int id) {
        return khachHangRepostory.getReferenceById(id);
    }

    @Override
    public List<String> getCities() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.geonames.org/childrenJSON?geonameId=1562822&username=thnhdq";

        try {
            ResponseEntity<GeoNamesResponse> responseEntity = restTemplate.getForEntity(url, GeoNamesResponse.class);
            List<String> cities = new ArrayList<>();
            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                GeoNamesResponse response = responseEntity.getBody();
                if (response.getGeonames() != null) {
                    for (GeoName geoName : response.getGeonames()) {
                        String cityName = geoName.getName();
                        cityName = cityName.replaceAll("(?i)province", "").trim();
                        cities.add(cityName);
                    }
                }
            }
            return cities;
        } catch (RestClientException e) {
            // Xử lý exception nếu có
            return null;
        }
    }

    @Override
    public List<String> getDistricts(String cityId) {
        String username = "thnhdq";
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.geonames.org/childrenJSON?geonameId=" + cityId + "&username=" + username;
        try {
            ResponseEntity<GeoNamesResponse> responseEntity = restTemplate.getForEntity(url, GeoNamesResponse.class);
            List<String> districts = new ArrayList<>();
            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                GeoNamesResponse response = responseEntity.getBody();
                if (response.getGeonames() != null) {
                    for (GeoName geoName : response.getGeonames()) {
                        if ("ADM2".equals(geoName.getFcode())) { // Kiểm tra nếu là quận/huyện (ADM2)
                            districts.add(geoName.getName());
                        }
                    }
                }
            }
            return districts;
        } catch (RestClientException e) {
            return null;
        }
    }


}
