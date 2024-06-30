package com.example.demo.controller.thongke;

import com.example.demo.service.impl.ThongKeImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class ThongKeController {
    @Autowired
    ThongKeImpl thongKe;

    @GetMapping("/admin/qlthongke")
    public String admin(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        int slhdtt = thongKe.thongKeTheoThang();
        int ttt = thongKe.thongKeTienTheoThang();
        int slhdtn = thongKe.thongKeTheoNgay();
        int ttn = thongKe.thongKeTienTheoNgay();
        int sptt = thongKe.soLuongsp();
        List<Object[]>  slt = thongKe.soLuongTon();
        BigDecimal ttdsn = thongKe.ttdsn();
        BigDecimal ttdst = thongKe.ttdst();
        int ttspn = thongKe.ttspn();
        int ttspt = thongKe.ttspt();
        int tthdn = thongKe.tthdn();
        int tthdt = thongKe.tthdt();
        int ptdsn = thongKe.ptdsn();
        int ptdst = thongKe.ptdst();
        int ptspn = thongKe.ptspn();
        int ptspt = thongKe.ptspt();
        int pthdn = thongKe.pthdn();
        int pthdt = thongKe.pthdt();
        List<Object[]> dayData = thongKe.dayData();
        List<Object[]> thangData = thongKe.thangData();
        List<Object[]> namData = thongKe.namData();
        Map<String, Object> khoangngayData = thongKe.khoangngayData();
        //xử lý dữ liệu biểu đồ tròn
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (Object[] row : thongKe.bdTron()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("value", row[1]);
            formattedData.add(map);
        }
        System.out.println(formattedData);
        //xử lý dữ liệu của biểu đồ cột theo ngày
        List<Map<String, Object>> formatteddayData = new ArrayList<>();
        for (Object[] row : thongKe.dayData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formatteddayData.add(map);
        }
        System.out.println(formatteddayData);
        //xử lý dữ liệu của biểu đồ cột theo tháng
        List<Map<String, Object>> formattedthangData = new ArrayList<>();
        for (Object[] row : thongKe.thangData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattedthangData.add(map);
        }
        System.out.println(formattedthangData);
        //xử lý dữ liệu của biểu đồ cột theo năm
        List<Map<String, Object>> formattednamData = new ArrayList<>();
        for (Object[] row : thongKe.namData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattednamData.add(map);
        }
        System.out.println(formattednamData);
        List<Map<String, Object>> formattednknData = new ArrayList<>();
        if(startDate != null && endDate != null) {
            System.out.println(startDate);
            System.out.println(endDate);

            for (Object[] row : thongKe.khoangngay(startDate, endDate)) {
                Map<String, Object> map = new HashMap<>();
                map.put("label", row[0]);
                map.put("sp", row[1]);
                map.put("hd", row[2]);
                formattednknData.add(map);
            }
            System.out.println(formattednknData);
        }

        model.addAttribute("bdTron", formattedData);
        model.addAttribute("slhdtt", slhdtt);
        model.addAttribute("ttt", ttt);
        model.addAttribute("slhdtn", slhdtn);
        model.addAttribute("ttn", ttn);
        model.addAttribute("sptt", sptt);
        model.addAttribute("sp", sp);
        model.addAttribute("slt", slt);
        model.addAttribute("ttdsn", ttdsn);
        model.addAttribute("ttdst", ttdst);
        model.addAttribute("ttspn", ttspn);
        model.addAttribute("ttspt", ttspt);
        model.addAttribute("tthdn", tthdn);
        model.addAttribute("tthdt", tthdt);
        model.addAttribute("ptdsn", ptdsn);
        model.addAttribute("ptdst", ptdst);
        model.addAttribute("ptspn", ptspn);
        model.addAttribute("ptspt", ptspt);
        model.addAttribute("pthdn", pthdn);
        model.addAttribute("pthdt", pthdt);
        model.addAttribute("dayData", formatteddayData);
        model.addAttribute("thangData", formattedthangData);
        model.addAttribute("namData", formattednamData);
        model.addAttribute("knData", formattednknData);
        return "admin/qlthongke";
    }

    @GetMapping("/khngay")
    public String admin1(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        int slhdtt = thongKe.thongKeTheoThang();
        int ttt = thongKe.thongKeTienTheoThang();
        int slhdtn = thongKe.thongKeTheoNgay();
        int ttn = thongKe.thongKeTienTheoNgay();
        int sptt = thongKe.soLuongsp();
        List<Object[]>  slt = thongKe.soLuongTon();
        BigDecimal ttdsn = thongKe.ttdsn();
        BigDecimal ttdst = thongKe.ttdst();
        int ttspn = thongKe.ttspn();
        int ttspt = thongKe.ttspt();
        int tthdn = thongKe.tthdn();
        int tthdt = thongKe.tthdt();
        int ptdsn = thongKe.ptdsn();
        int ptdst = thongKe.ptdst();
        int ptspn = thongKe.ptspn();
        int ptspt = thongKe.ptspt();
        int pthdn = thongKe.pthdn();
        int pthdt = thongKe.pthdt();
        List<Object[]> dayData = thongKe.dayData();
        List<Object[]> thangData = thongKe.thangData();
        List<Object[]> namData = thongKe.namData();
        Map<String, Object> khoangngayData = thongKe.khoangngayData();
        //xử lý dữ liệu biểu đồ tròn
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (Object[] row : thongKe.bdTron()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("value", row[1]);
            formattedData.add(map);
        }
        System.out.println(formattedData);
        //xử lý dữ liệu của biểu đồ cột theo ngày
        List<Map<String, Object>> formatteddayData = new ArrayList<>();
        for (Object[] row : thongKe.dayData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formatteddayData.add(map);
        }
        System.out.println(formatteddayData);
        //xử lý dữ liệu của biểu đồ cột theo tháng
        List<Map<String, Object>> formattedthangData = new ArrayList<>();
        for (Object[] row : thongKe.thangData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattedthangData.add(map);
        }
        System.out.println(formattedthangData);
        //xử lý dữ liệu của biểu đồ cột theo năm
        List<Map<String, Object>> formattednamData = new ArrayList<>();
        for (Object[] row : thongKe.namData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattednamData.add(map);
        }
        System.out.println(formattednamData);
        if(startDate != null && endDate != null) {
            System.out.println(startDate);
            System.out.println(endDate);
            List<Object[]> kn = thongKe.khoangngay(startDate, endDate);
            System.out.println(kn);
        }

        model.addAttribute("bdTron", formattedData);
        model.addAttribute("slhdtt", slhdtt);
        model.addAttribute("ttt", ttt);
        model.addAttribute("slhdtn", slhdtn);
        model.addAttribute("ttn", ttn);
        model.addAttribute("sptt", sptt);
        model.addAttribute("sp", sp);
        model.addAttribute("slt", slt);
        model.addAttribute("ttdsn", ttdsn);
        model.addAttribute("ttdst", ttdst);
        model.addAttribute("ttspn", ttspn);
        model.addAttribute("ttspt", ttspt);
        model.addAttribute("tthdn", tthdn);
        model.addAttribute("tthdt", tthdt);
        model.addAttribute("ptdsn", ptdsn);
        model.addAttribute("ptdst", ptdst);
        model.addAttribute("ptspn", ptspn);
        model.addAttribute("ptspt", ptspt);
        model.addAttribute("pthdn", pthdn);
        model.addAttribute("pthdt", pthdt);
        model.addAttribute("dayData", formatteddayData);
        model.addAttribute("thangData", formattedthangData);
        model.addAttribute("namData", formattednamData);
        model.addAttribute("khoangngayData", khoangngayData);
        return "admin/qlthongke";
    }

    @PostMapping("/export-excel")
    @ResponseBody
    public ResponseEntity<byte[]> exportToExcel() {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        List<Object[]>  slt = thongKe.soLuongTon();
        // Dữ liệu mẫu
        String[][] data = {
                {"1", "08:00", "50", "5000"},
                {"2", "09:00", "45", "4500"},
                {"3", "10:00", "60", "6000"}
        };
        String[][] data2 = {
                {"1", "SP001", "Product A", "10000", "50"},
                {"2", "SP002", "Product B", "12000", "45"},
                {"3", "SP003", "Product C", "15000", "60"}
        };

        // Tạo Workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Doanh số theo ngày");
        Sheet sheet2 = workbook.createSheet("Doanh số theo tháng");
        Sheet sheet3 = workbook.createSheet("Doanh số theo năm");
        Sheet sheet4 = workbook.createSheet("Sản phẩm bán chạy theo tháng");
        Sheet sheet5 = workbook.createSheet("Sản phẩm sắp hết hàng");

        //sheet1
        // Gộp ô trên dòng mặc định 1
        CellRangeAddress mergedRegion1 = new CellRangeAddress(0, 0, 0, 3); // Gộp từ ô A1 đến D1
        sheet1.addMergedRegion(mergedRegion1);

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow1 = sheet1.createRow(0);
        Cell cell11 = headerRow1.createCell(0);
        cell11.setCellValue("DOANH SỐ THEO NGÀY");

        // Tạo dòng mặc định 2
        Row defaultRow1 = sheet1.createRow(1);
        Cell cell1A2 = defaultRow1.createCell(0);
        cell1A2.setCellValue("STT");
        Cell cell1B2 = defaultRow1.createCell(1);
        cell1B2.setCellValue("Thời gian");
        Cell cell1C2 = defaultRow1.createCell(2);
        cell1C2.setCellValue("Số hàng bán được");
        Cell cell1D2 = defaultRow1.createCell(3);
        cell1D2.setCellValue("Doanh thu");

        // Dịch chuyển dữ liệu xuống sau hai dòng mặc định
        int rowIndex1 = 2;
        for (String[] rowData1 : data) {
            Row row = sheet1.createRow(rowIndex1++);
            int cellIndex = 0;
            for (String cellData : rowData1) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(cellData);
            }
        }

        //sheet2
        // Gộp ô trên dòng mặc định 1
        CellRangeAddress mergedRegion2 = new CellRangeAddress(0, 0, 0, 3); // Gộp từ ô A1 đến D1
        sheet2.addMergedRegion(mergedRegion2);

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow2 = sheet2.createRow(0);
        Cell cell21 = headerRow2.createCell(0);
        cell21.setCellValue("DOANH SỐ THEO NGÀY");

        // Tạo dòng mặc định 2
        Row defaultRow2 = sheet2.createRow(1);
        Cell cell2A2 = defaultRow2.createCell(0);
        cell2A2.setCellValue("STT");
        Cell cell2B2 = defaultRow2.createCell(1);
        cell2B2.setCellValue("Thời gian");
        Cell cell2C2 = defaultRow2.createCell(2);
        cell2C2.setCellValue("Số hàng bán được");
        Cell cell2D2 = defaultRow2.createCell(3);
        cell2D2.setCellValue("Doanh thu");

        // Dịch chuyển dữ liệu xuống sau hai dòng mặc định
        int rowIndex2 = 2;
        for (String[] rowData2 : data) {
            Row row = sheet2.createRow(rowIndex2++);
            int cellIndex = 0;
            for (String cellData : rowData2) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(cellData);
            }
        }
        //sheet3
        // Gộp ô trên dòng mặc định 1
        CellRangeAddress mergedRegion3 = new CellRangeAddress(0, 0, 0, 3); // Gộp từ ô A1 đến D1
        sheet3.addMergedRegion(mergedRegion3);

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow3 = sheet3.createRow(0);
        Cell cell31 = headerRow3.createCell(0);
        cell31.setCellValue("DOANH SỐ THEO NGÀY");

        // Tạo dòng mặc định 2
        Row defaultRow3 = sheet3.createRow(1);
        Cell cell3A2 = defaultRow3.createCell(0);
        cell3A2.setCellValue("STT");
        Cell cell3B2 = defaultRow3.createCell(1);
        cell3B2.setCellValue("Thời gian");
        Cell cell3C2 = defaultRow3.createCell(2);
        cell3C2.setCellValue("Số hàng bán được");
        Cell cell3D2 = defaultRow3.createCell(3);
        cell3D2.setCellValue("Doanh thu");

        // Dịch chuyển dữ liệu xuống sau hai dòng mặc định
        int rowIndex3 = 2;
        for (String[] rowData3 : data) {
            Row row = sheet3.createRow(rowIndex3++);
            int cellIndex = 0;
            for (String cellData : rowData3) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(cellData);
            }
        }
        //sheet4
        // Gộp ô trên dòng mặc định 1 (A1:E1)
        CellRangeAddress mergedRegion4 = new CellRangeAddress(0, 0, 0, 4); // Gộp từ ô A1 đến E1
        sheet4.addMergedRegion(mergedRegion4);

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow4 = sheet4.createRow(0);
        Cell cell4A1 = headerRow4.createCell(0);
        cell4A1.setCellValue("SẢN PHẨM BÁN CHẠY THÁNG NÀY");
        // Thiết lập style cho ô
//        CellStyle style = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 16);
//        style.setFont(font);
//        cell4A1.setCellStyle(style);

        // Tạo dòng mặc định 2
        Row defaultRow4 = sheet4.createRow(1);
        String[] headers4 = {"STT", "Mã SP", "Tên SP", "Giá bán", "Số lượng đã bán"};
        for (int i = 0; i < headers4.length; i++) {
            Cell cell = defaultRow4.createCell(i);
            cell.setCellValue(headers4[i]);
        }

        // Dịch chuyển dữ liệu xuống sau hai dòng mặc định
        int rowIndex4 = 2;
        int stt4 = 1; // Biến để tính toán STT
        for (Object[] rowData4 : sp) {
            Row row = sheet4.createRow(rowIndex4++);
            Cell sttCell = row.createCell(0); // Cột STT là cột 0
            sttCell.setCellValue(stt4++); // Tăng giá trị STT và gán vào ô

            // Lặp qua các giá trị còn lại và gán vào các ô tương ứng
            for (int cellIndex = 1; cellIndex < rowData4.length + 1; cellIndex++) {
                Cell cell = row.createCell(cellIndex);
                Object cellData = rowData4[cellIndex - 1];
                // Chuyển đổi Object thành chuỗi và đưa vào ô
                cell.setCellValue(cellData != null ? cellData.toString() : "");
            }
        }
//        int rowIndex4 = 2;
//        for (String[] rowData4 : data2) {
//            Row row = sheet4.createRow(rowIndex4++);
//            int cellIndex = 0;
//            for (String cellData : rowData4) {
//                Cell cell = row.createCell(cellIndex++);
//                cell.setCellValue(cellData);
//            }
//        }
        //sheet5
        // Gộp ô trên dòng mặc định 1 (A1:E1)
        CellRangeAddress mergedRegion5 = new CellRangeAddress(0, 0, 0, 4); // Gộp từ ô A1 đến E1
        sheet5.addMergedRegion(mergedRegion5);

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow5 = sheet5.createRow(0);
        Cell cell5A1 = headerRow5.createCell(0);
        cell5A1.setCellValue("SẢN PHẨM SẮP HẾT HÀNG");
        // Thiết lập style cho ô
//        CellStyle style = workbook.createCellStyle();
//        Font font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 16);
//        style.setFont(font);
//        cell4A1.setCellStyle(style);

        // Tạo dòng mặc định 2
        Row defaultRow5 = sheet5.createRow(1);
        String[] headers5 = {"STT", "Mã SP", "Tên SP", "Giá bán", "Số lượng"};
        for (int i = 0; i < headers5.length; i++) {
            Cell cell = defaultRow5.createCell(i);
            cell.setCellValue(headers5[i]);
        }

        // Dịch chuyển dữ liệu xuống sau hai dòng mặc định
        int rowIndex5 = 2;
        int stt5 = 1; // Biến để tính toán STT
        for (Object[] rowData5 : slt) {
            Row row = sheet5.createRow(rowIndex5++);
            Cell sttCell = row.createCell(0); // Cột STT là cột 0
            sttCell.setCellValue(stt5++); // Tăng giá trị STT và gán vào ô

            // Lặp qua các giá trị còn lại và gán vào các ô tương ứng
            for (int cellIndex = 1; cellIndex < rowData5.length + 1; cellIndex++) {
                Cell cell = row.createCell(cellIndex);
                Object cellData = rowData5[cellIndex - 1];
                // Chuyển đổi Object thành chuỗi và đưa vào ô
                cell.setCellValue(cellData != null ? cellData.toString() : "");
            }
        }
        // Ghi Workbook ra một mảng byte[]
        byte[] excelBytes = null;
        try {
            excelBytes = workbookToBytes(workbook);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Thiết lập header cho phản hồi
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "doanhthu.xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
