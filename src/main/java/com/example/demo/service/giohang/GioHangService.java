package com.example.demo.service.giohang;

import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScope
@Service
public class GioHangService {
    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    List<GioHangChiTiet> items = new ArrayList<>();
    public List<GioHangChiTiet> getItems() {
        return items;
    }


    public void add(int id) {
        GioHangChiTiet item = items
                .stream()
                .filter(it -> it.getSanphamchitiet().getId().equals(id))
                .findFirst()
                .orElse(null);
        if (item != null) {
            item.setSoluong(item.getSoluong() + 1);
            return;
        }
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElse(null);
        if (sanPhamChiTiet != null) {
            GioHangChiTiet newItem = new GioHangChiTiet();
            newItem.setSanphamchitiet(sanPhamChiTiet);
            newItem.setSoluong(1);
            newItem.setNgaytao(new Date());
            newItem.setTrangthai(true);
            items.add(newItem);
        }
    }
    public int getTotal(){
        int total = 0;
        for(GioHangChiTiet item : items) {
            total += item.getSoluong();
        }
        return total;
    }

    private List<GioHangChiTiet> listGioHangKhongTK = new ArrayList<>();

    public List<GioHangChiTiet> getListGioHangKhongTK() {
        return listGioHangKhongTK;
    }

    public void addGioHangChiTiet(GioHangChiTiet gioHangChiTiet) {
        listGioHangKhongTK.add(gioHangChiTiet);
    }

    public void clearListGioHangKhongTK() {
        listGioHangKhongTK.clear();
    }
}
