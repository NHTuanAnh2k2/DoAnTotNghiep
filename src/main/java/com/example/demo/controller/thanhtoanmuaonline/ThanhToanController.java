package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ThanhToanController {
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @RequestMapping("/view-thanh-toan")
    public String viewthanhtoan(Model model){
        List<PhieuGiamGia> lst= phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG= new ArrayList<>();
        for(PhieuGiamGia p : lst){
            if(p.getTrangthai()==1 && p.getKieuphieu()==false){
                lstPGG.add(p);
            }
        }
        model.addAttribute("lstPGG",lstPGG);
        return "customer/thanhtoan";
    }
}
