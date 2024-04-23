package com.example.demo.service.impl;

import com.example.demo.entity.DiaChi;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.restcontroller.khachhang.District;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.restcontroller.khachhang.Ward;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<KhachHang> findAllKhachHang() {
        return khachHangRepostory.findAllKhachHang();
    }

    @Override
    public List<KhachHang> findAll() {
        return khachHangRepostory.findAll();
    }

    @Override
    public List<NguoiDung> findAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    @Override

    public KhachHang add(KhachHang khachHang, NguoiDung nguoiDung, DiaChi diaChi, String tinhthanhpho, String quanhuyen, String xaphuong, String tenduong) {
        int usernameLength = 8;
        int passwordLength = 10;
        String username = generateRandomPassword(usernameLength);
        String password = generateRandomPassword(passwordLength);

        LocalDateTime currentDate = LocalDateTime.now();

        nguoiDung.setTaikhoan(username);
        nguoiDung.setMatkhau(password);
        nguoiDung.setNgaytao(Timestamp.valueOf(currentDate));
        nguoiDung.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nguoiDung.setTrangthai(true);
        nguoiDungRepository.save(nguoiDung);

        diaChi.setTenduong(tenduong);
        diaChi.setQuanhuyen(quanhuyen);
        diaChi.setXaphuong(xaphuong);
        diaChi.setSdtnguoinhan(nguoiDung.getSodienthoai());
        diaChi.setNguoidung(nguoiDung);
        diaChi.setTrangthai(nguoiDung.getTrangthai());
        diaChi.setTinhthanhpho(tinhthanhpho);
        diaChi.setNgaytao(nguoiDung.getNgaytao());
        diaChi.setLancapnhatcuoi(nguoiDung.getLancapnhatcuoi());
        diaChiRepository.save(diaChi);

        khachHang.setNguoidung(nguoiDung);
        khachHang.setTrangthai(nguoiDung.getTrangthai());
        khachHang.setNgaytao(nguoiDung.getNgaytao());
        khachHang.setLancapnhatcuoi(nguoiDung.getLancapnhatcuoi());
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
        String senderPassword = "isniscmsyjdmqjmo";

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
    public List<KhachHang> findByAll(String ten, String sdt, Date ngaysinh) {
        return khachHangRepostory.findByAll(ten, sdt, ngaysinh);
    }

    @Override
    public KhachHang getOne(int id) {
        return khachHangRepostory.getReferenceById(id);
    }

    @Override
    public List<Province> getCities() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://java6-f03d7-default-rtdb.firebaseio.com/province/-Nw8-WWjhZbyOqV-oeyy.json";

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JSONArray jsonArray = new JSONArray(jsonResponse);
            List<Province> lstProvince = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Province province = new Province();

                // Check if "Name" key exists
                if (jsonObject.has("Name")) {
                    province.setName(jsonObject.getString("Name"));
                } else {
                    // Handle missing key
                    province.setName("Unknown");
                }

                JSONArray districtArray = jsonObject.getJSONArray("Districts");
                List<District> districtList = new ArrayList<>();
                for (int j = 0; j < districtArray.length(); j++) {
                    JSONObject districtObject = districtArray.getJSONObject(j);
                    District district = new District();

                    // Check if "Name" key exists
                    if (districtObject.has("Name")) {
                        district.setName(districtObject.getString("Name"));
                    } else {
                        // Handle missing key
                        district.setName("Unknown");
                    }

                    JSONArray wardArray = districtObject.getJSONArray("Wards");
                    List<Ward> wardList = new ArrayList<>();
                    for (int k = 0; k < wardArray.length(); k++) {
                        JSONObject wardObject = wardArray.getJSONObject(k);
                        Ward ward = new Ward();

                        // Check if "Name" key exists
                        if (wardObject.has("Name")) {
                            ward.setName(wardObject.getString("Name"));
                        } else {
                            // Handle missing key
                            ward.setName("Unknown");
                        }

                        // Populate other ward attributes as needed
                        wardList.add(ward);
                    }
                    district.setWards(wardList);

                    // Populate other district attributes as needed
                    districtList.add(district);
                }
                province.setDistricts(districtList);

                lstProvince.add(province);
            }

            return lstProvince;
        } catch (RestClientException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
