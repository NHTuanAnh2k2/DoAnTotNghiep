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
    @GetMapping("/admin/qlthongkee")
    @ResponseBody
    public Map<String, Object> admin(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {


        List<Map<String, Object>> formattednknData = new ArrayList<>();
        if(startDate != null && endDate != null) {
            for (Object[] row : thongKe.khoangngay(startDate, endDate)) {
                Map<String, Object> map = new HashMap<>();
                map.put("label", row[0]);
                map.put("sp", row[1]);
                map.put("hd", row[2]);
                formattednknData.add(map);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("knData", formattednknData);

        return response;
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
        List<Object[]> sp = thongKe.soLuongDaBan();
        List<Object[]> slt = thongKe.soLuongTon();
        List<Object[]> day = thongKe.dayex();
        List<Object[]> thang = thongKe.thangex();
        List<Object[]> nam = thongKe.namex();

        // Tạo Workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("Doanh số theo ngày");
        Sheet sheet2 = workbook.createSheet("Doanh số theo tháng");
        Sheet sheet3 = workbook.createSheet("Doanh số theo năm");
        Sheet sheet4 = workbook.createSheet("Sản phẩm bán chạy theo tháng");
        Sheet sheet5 = workbook.createSheet("Sản phẩm sắp hết hàng");

        // Thiết lập font và style cho các sheet
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Times New Roman");
        titleFont.setFontHeightInPoints((short) 18);

        Font headerFont = workbook.createFont();
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeightInPoints((short) 16);

        Font cellFont = workbook.createFont();
        cellFont.setFontName("Times New Roman");
        cellFont.setFontHeightInPoints((short) 14);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setWrapText(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setWrapText(true); // Tự động dãn dòng cho header

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setWrapText(true); // Tự động dãn dòng cho dữ liệu

        // Tạo CellStyle cho định dạng tiền tệ
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setFont(cellFont);
        currencyStyle.setAlignment(HorizontalAlignment.CENTER);
        currencyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        currencyStyle.setBorderBottom(BorderStyle.THIN);
        currencyStyle.setBorderTop(BorderStyle.THIN);
        currencyStyle.setBorderLeft(BorderStyle.THIN);
        currencyStyle.setBorderRight(BorderStyle.THIN);
        currencyStyle.setWrapText(true);
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("#,##0₫"));

        // Thiết lập dòng mặc định 1 trên các sheet 1, 2, 3
        setHeaderRow(sheet1, titleStyle, headerStyle, "STT", "Thời gian", "Số lượng đã bán", "Doanh thu");
        setHeaderRow(sheet2, titleStyle, headerStyle, "STT", "Thời gian", "Số lượng đã bán", "Doanh thu");
        setHeaderRow(sheet3, titleStyle, headerStyle, "STT", "Thời gian", "Số lượng đã bán", "Doanh thu");
        setHeaderRow(sheet4, titleStyle, headerStyle, "STT", "Mã SP", "Tên SP", "Giá bán", "Số lượng đã bán");
        setHeaderRow(sheet5, titleStyle, headerStyle, "STT", "Mã SP", "Tên SP", "Giá bán", "Số lượng");

        // Thiết lập dữ liệu cho sheet 4 và sheet 5
        setSheetData(sheet1,day,cellStyle,currencyStyle);
        setSheetData(sheet2,thang,cellStyle,currencyStyle);
        setSheetData(sheet3,nam,cellStyle,currencyStyle);
        setSheetData(sheet4, sp, cellStyle,currencyStyle);
        setSheetData(sheet5, slt, cellStyle,currencyStyle);
        // Thiết lập chiều cao cho dòng 1 và các dòng còn lại
        sheet1.getRow(0).setHeightInPoints(40);
        sheet2.getRow(0).setHeightInPoints(40);
        sheet3.getRow(0).setHeightInPoints(40);
        sheet4.getRow(0).setHeightInPoints(40);
        sheet5.getRow(0).setHeightInPoints(40);
        

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

    private void setHeaderRow(Sheet sheet, CellStyle titleStyle, CellStyle headerStyle,
                              String... headers) {
        // Gộp ô trên dòng mặc định 1
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        // Tạo dòng mặc định 1 và thiết lập giá trị
        Row headerRow = sheet.createRow(0);
        Cell titleCell = headerRow.createCell(0);
        titleCell.setCellValue(sheet.getSheetName().toUpperCase()); // Tiêu đề sheet
        titleCell.setCellStyle(titleStyle);

        // Tạo dòng mặc định 2 (STT, Thời gian, Số lượng đã bán, Doanh thu)
        Row defaultRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = defaultRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i); // Tự động dãn cột để vừa với nội dung
        }
    }

    private void setSheetData(Sheet sheet, List<Object[]> data, CellStyle cellStyle, CellStyle currencyStyle) {
        // Thiết lập header cho sheet 4
        //setHeaderRow(sheet, null, null, "STT", "Mã SP", "Tên SP", "Giá bán", "Số lượng đã bán");

        // Đưa dữ liệu vào từ dòng thứ 2 trở đi
        int rowIndex = 2;
        int stt = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowIndex++);
            Cell sttCell = row.createCell(0);
            sttCell.setCellValue(stt++);
            sttCell.setCellStyle(cellStyle);

            for (int cellIndex = 1; cellIndex < rowData.length + 1; cellIndex++) {
                Cell cell = row.createCell(cellIndex);
                Object cellData = rowData[cellIndex - 1];
                if (currencyStyle != null && cellIndex == 3) { // Cột "Giá bán"
                    cell.setCellValue(cellData != null ? Double.parseDouble(cellData.toString()) : 0);
                    cell.setCellStyle(currencyStyle);
                } else {
                    cell.setCellValue(cellData != null ? cellData.toString() : "");
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        // Tự động dãn cột để vừa với nội dung
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
