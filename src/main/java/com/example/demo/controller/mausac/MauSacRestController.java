package com.example.demo.controller.mausac;

import com.example.demo.entity.MauSac;
import com.example.demo.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MauSacRestController {
    @Autowired
    MauSacRepository mauSacRepository;

    @GetMapping("/updateMauSac/{id}")
    public ResponseEntity<MauSac> getMauSac(@PathVariable Integer id) {
        MauSac mauSac = mauSacRepository.findById(id).orElse(null);
        return ResponseEntity.ok(mauSac);
    }

    @PutMapping("/updateMauSac/{id}")
    public ResponseEntity<String> updateMauSac(@PathVariable Integer id, @RequestBody MauSac updatedMauSac) {
        MauSac existingMauSac = mauSacRepository.findById(id).orElse(null);
        if (existingMauSac == null) {
            return ResponseEntity.notFound().build();
        }

        existingMauSac.setTen(updatedMauSac.getTen());
        mauSacRepository.save(existingMauSac);

        // Trả về redirect
        return ResponseEntity.ok("redirect:/listMauSac");
    }

    @GetMapping("/checkTenMauSac")
    public ResponseEntity<Boolean> checkTenMauSac(@RequestParam String ten) {
        boolean exists = mauSacRepository.existsByTen(ten);
        return ResponseEntity.ok(exists);
    }


}
