package com.example.demo.controller.kichco;

import com.example.demo.entity.DeGiay;
import com.example.demo.entity.KichCo;
import com.example.demo.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KichCoRestController {
    @Autowired
    KichCoRepository kichCoRepository;

    @GetMapping("/updateKichCo/{id}")
    public ResponseEntity<KichCo> getKichCo(@PathVariable Integer id) {
        KichCo kichCo = kichCoRepository.findById(id).orElse(null);
        return ResponseEntity.ok(kichCo);
    }

    @PutMapping("/updateKichCo/{id}")
    public ResponseEntity<String> updateKichCo(@PathVariable Integer id, @RequestBody KichCo updatedKicCo) {
        KichCo existingKichCo = kichCoRepository.findById(id).orElse(null);
        if (existingKichCo == null) {
            return ResponseEntity.notFound().build();
        }
        String trimmedTenKichCo = (updatedKicCo.getTen() != null)
                ? updatedKicCo.getTen().trim().replaceAll("\\s+", " ")
                : null;
        existingKichCo.setTen(trimmedTenKichCo);
        kichCoRepository.save(existingKichCo);

        // Trả về redirect
        return ResponseEntity.ok("redirect:/listKichCo");
    }

    @GetMapping("/checkTenKichCo")
    public ResponseEntity<Boolean> checkTenKichCo(@RequestParam String ten) {
        boolean exists = kichCoRepository.existsByTen(ten);
        return ResponseEntity.ok(exists);
    }

}
