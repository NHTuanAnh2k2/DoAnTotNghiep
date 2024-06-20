package com.example.demo.service.impl;

import com.example.demo.entity.DiaChi;
import com.example.demo.info.KhachHangInfo;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.hosokhachhang.DoiMatKhauNguoiDung;
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
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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
//        int usernameLength = 8;
//        int passwordLength = 10;
//        String username = generateRandomPassword(usernameLength);
//        String password = generateRandomPassword(passwordLength);
//
//        LocalDateTime currentDate = LocalDateTime.now();
//
//        nguoiDung.setTaikhoan(username);
//        nguoiDung.setMatkhau(password);
//        nguoiDung.setNgaytao(Timestamp.valueOf(currentDate));
//        nguoiDung.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
//        nguoiDung.setTrangthai(true);
//        nguoiDungRepository.save(nguoiDung);
//
//        diaChi.setTenduong(tenduong);
//        diaChi.setQuanhuyen(quanhuyen);
//        diaChi.setXaphuong(xaphuong);
//        diaChi.setSdtnguoinhan(nguoiDung.getSodienthoai());
//        diaChi.setNguoidung(nguoiDung);
//        diaChi.setTrangthai(nguoiDung.getTrangthai());
//        diaChi.setTinhthanhpho(tinhthanhpho);
//        diaChi.setNgaytao(nguoiDung.getNgaytao());
//        diaChi.setLancapnhatcuoi(nguoiDung.getLancapnhatcuoi());
//        diaChi.setHotennguoinhan(nguoiDung.getHovaten());
//        diaChiRepository.save(diaChi);
//
//        String maKH = "KH" + (totalCustomer() + 1);
//        khachHang.setMakhachhang(maKH);
//        khachHang.setNguoidung(nguoiDung);
//        khachHang.setTrangthai(nguoiDung.getTrangthai());
//        khachHang.setNgaytao(nguoiDung.getNgaytao());
//        khachHang.setLancapnhatcuoi(nguoiDung.getLancapnhatcuoi());
//        khachHangRepostory.save(khachHang);
//
//        String nguoiNhan = diaChi.getNguoidung().getEmail();
//        String tenNguoiNhan = nguoiDung.getHovaten();
//        this.sendEmail(nguoiNhan, username, password, tenNguoiNhan);
        return khachHang;
    }

    @Override
    public DiaChi addDiaChi(DiaChi diaChi) {
        diaChiRepository.save(diaChi);
        return diaChi;
    }
    @Override
    public KhachHang addKhachHang(KhachHang khachHang) {
        khachHangRepostory.save(khachHang);
        return khachHang;
    }

    @Override
    public NguoiDung addNguoiDung(NguoiDung nguoiDung) {
        nguoiDungRepository.save(nguoiDung);
        return nguoiDung;
    }

    @Override
    public DiaChi updateDiaChi(DiaChi diaChi) {
        diaChi.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        diaChiRepository.save(diaChi);
        return diaChi;
    }

    @Override
    public KhachHang updateKhachHang(KhachHang khachHang) {
        return khachHangRepostory.save(khachHang);
    }

    @Override
    public NguoiDung updateNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    public String generateRandomPassword(int length) {
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
    public void sendEmailQuenMatKhau(String recipient, String name, String maDoiMatKhau) {
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
            message.setSubject("Mã Xác Nhận Đặt Lại Mật Khẩu");

            // Thiết lập nội dung email
            String emailContent = "Xin chào, " + name + "\n\n";
            emailContent += "Đây là mã đặt lại mật khẩu của bạn: " + maDoiMatKhau + "\n\n";
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
    public List<KhachHangInfo> displayKhachHang() {
        List<KhachHang> lstKhachHang = this.findAllKhachHang();
        Collections.sort(lstKhachHang, Comparator.comparing(KhachHang::getNgaytao).reversed());

        List<KhachHangInfo> lstkhachhanginfo = new ArrayList<>();

        for (KhachHang khachHang : lstKhachHang) {
            int idNguoiDung = khachHang.getNguoidung().getId();
            List<DiaChi> lstDiaChi = diaChiRepository.findDiaChiByIdNd(idNguoiDung);
            Collections.sort(lstDiaChi, Comparator.comparing(DiaChi::getNgaytao).reversed());

            if (!lstDiaChi.isEmpty()) {
                KhachHangInfo khachHangInfo = new KhachHangInfo();
                khachHangInfo.setKhachhang(khachHang);
                khachHangInfo.setDiachi(lstDiaChi.get(0));
                lstkhachhanginfo.add(khachHangInfo);
            }
        }

        return lstkhachhanginfo;
    }


    @Override
    public KhachHang findKhachHangByIdNguoiDung(Integer id) {
        return khachHangRepostory.findByNguoiDung(id);
    }

    @Override
    public List<KhachHangInfo> findByTenSdtMa(String tenSdtMa) {
        List<KhachHang> lstKhachHang = khachHangRepostory.findByTenSdtMa(tenSdtMa);
        Collections.sort(lstKhachHang, Comparator.comparing(KhachHang::getNgaytao).reversed());

        List<KhachHangInfo> lstkhachhanginfo = new ArrayList<>();

        // Lấy danh sách địa chỉ cho từng khách hàng
        for (KhachHang khachHang : lstKhachHang) {
            KhachHangInfo khachHangInfo = new KhachHangInfo();
            int ndId = khachHang.getNguoidung().getId();
            List<DiaChi> lstDiaChi = diaChiRepository.findDiaChiByIdNd(ndId);

            // Kiểm tra xem số lượng địa chỉ và số lượng khách hàng có phù hợp không
            if (lstDiaChi.size() >= 1) { // Kiểm tra ít nhất phải có một địa chỉ tương ứng
                DiaChi diaChi = lstDiaChi.get(0); // Lấy địa chỉ tương ứng với khách hàng
                khachHangInfo.setKhachhang(khachHang);
                khachHangInfo.setDiachi(diaChi);
                lstkhachhanginfo.add(khachHangInfo);
            }
        }

        return lstkhachhanginfo;
    }

    @Override
    public NguoiDung findByEmail(String email) {
        return nguoiDungRepository.findNguoiDungByEmail(email);
    }



    @Override
    public void scanQr(){
        return;
    }

    @Override
    public String readQRCode(File qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(bufferedImage)));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(binaryBitmap);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public KhachHang getOne(int id) {
        return khachHangRepostory.getReferenceById(id);
    }

    @Override
    public KhachHang findKhachHangById(int id) {
        return khachHangRepostory.getReferenceById(id);
    }

    @Override
    public DiaChi findDiaChiById(int id) {
        return diaChiRepository.getReferenceById(id);
    }

    @Override
    public NguoiDung findNguoiDungById(int id) {
        return nguoiDungRepository.getReferenceById(id);
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


    @Override
    public NguoiDung findNguoiDungByTaikhoan(String taikhoan) {
        NguoiDung nd = nguoiDungRepository.findNguoiDungByTaikhoan(taikhoan);
        return nd;
    }

    @Override
    public DiaChi findDiaChiByIdNguoidung(Integer idNd) {
        DiaChi dc = diaChiRepository.findDiaChiByIdNguoidung(idNd);
        return dc;
    }

    @Override
    public NguoiDung doimatkhau(DoiMatKhauNguoiDung nguoidung, int id) {
        NguoiDung nd = this.findNguoiDungById(id);
        nd.setMatkhau(nguoidung.getMatkhau());
        nd.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        nguoiDungRepository.save(nd);
        return nd;
    }


}
