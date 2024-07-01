package com.example.demo.repository.giohang;

import com.example.demo.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungGioHangRepository extends JpaRepository<NguoiDung, Integer> {
@Query(value = """
SELECT n FROM NguoiDung n where n.id=:id
""")
    NguoiDung findByIdND(Integer id);
}
