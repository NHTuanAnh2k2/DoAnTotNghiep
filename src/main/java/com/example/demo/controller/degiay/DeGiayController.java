package com.example.demo.controller.degiay;

import com.example.demo.entity.DeGiay;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.DeGiayRepository;
import com.example.demo.service.impl.DeGiayImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DeGiayController {
    @Autowired
    DeGiayRepository deGiayRepository;
    @Autowired
    DeGiayImp deGiayImp;

    @GetMapping("/listdegiay")
    public String listdegiay(Model model, @ModelAttribute("degiay") DeGiay deGiay, @ModelAttribute("tim") ThuocTinhInfo info) {
        List<DeGiay> list;
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim().replaceAll("\\s+", " ") : null;
        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);
        if (isKeyEmpty && isTrangthaiNull) {
            list = deGiayRepository.findAllByOrderByNgaytaoDesc();
        } else {
            list = deGiayRepository.getDeGiayByTenOrTrangthai(trimmedKey, info.getTrangthai());
        }
        List<DeGiay> listAll = deGiayRepository.findAll();
        model.addAttribute("listAll", listAll);
        model.addAttribute("list", list);
        model.addAttribute("fillSearch", trimmedKey);
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/qldegiay";
    }



//    @PostMapping("/update/{id}")
//    public String update(@PathVariable Integer id) {
//        deGiayRepository.updateTrangThaiToFalseById(id);
//        return "redirect:/listdegiay";
//    }


//    @GetMapping("/updateDeGiay/{id}")
//    public String delete(@PathVariable Integer id, Model model) {
//        DeGiay degiay = deGiayRepository.findById(id).orElse(null);
//        model.addAttribute("degiay", degiay);
//        return "admin/qldegiay";
//    }

    @PostMapping("/degiay/updateTrangThai/{id}")
    public String updateTrangThaiDeGiay(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        DeGiay existingDeGiay = deGiayRepository.findById(id).orElse(null);
        if (existingDeGiay != null) {
            // Chuyển đổi trạng thái
            existingDeGiay.setTrangthai(!existingDeGiay.getTrangthai());
            deGiayRepository.save(existingDeGiay);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái thành công!");
        }
        return "redirect:/listdegiay";
    }




    @PostMapping("/addSave")
    public String addSave(@ModelAttribute("degiay") DeGiay deGiay) {
        String trimmedTenDeGiay = (deGiay.getTen() != null)
                ? deGiay.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setTen(trimmedTenDeGiay);
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/listdegiay";
    }


    @PostMapping("/addDeGiayModal")
    public String addDeGiayModal(@ModelAttribute("degiay") DeGiay deGiay) {
        String trimmedTenDeGiay = (deGiay.getTen() != null)
                ? deGiay.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setTen(trimmedTenDeGiay);
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addDeGiaySua")
    public String addDeGiaySua(@ModelAttribute("degiay") DeGiay deGiay, @RequestParam("spctId") Integer spctId) {
        String trimmedTenDeGiay = (deGiay.getTen() != null)
                ? deGiay.getTen().trim().replaceAll("\\s+", " ")
                : null;
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setTen(trimmedTenDeGiay);
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/updateCTSP/" + spctId;
    }

    @ModelAttribute("dsdg")
    public List<DeGiay> getDS() {
        return deGiayImp.findAll();
    }


}
