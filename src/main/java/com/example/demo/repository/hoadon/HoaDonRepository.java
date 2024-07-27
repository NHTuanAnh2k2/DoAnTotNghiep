package com.example.demo.repository.hoadon;

import com.example.demo.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT h FROM HoaDon h WHERE h.mahoadon =?1 OR h.email =?1 OR h.sdt =?1 ORDER BY h.ngaytao DESC")
    List<HoaDon> findHoaDonByMaOrSdtOrEmail(String inputSearch);
    @Query("SELECT h FROM HoaDon h where h.id =?1")
    HoaDon findHoaDonById(Integer id);
    @Query("SELECT h FROM HoaDon h where h.khachhang.id =?1 ORDER BY h.id DESC")
    List<HoaDon> findAllByKhachHang(Integer id);
    @Query("SELECT h FROM HoaDon h where h.trangthai =?1 AND h.khachhang.id =?2 ORDER BY h.id DESC")
    List<HoaDon> findHoaDonByTrangThaiAndKhachhang(Integer trangthai, Integer id);
    Page<HoaDon> findAllByTrangthaiAndLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(Integer trangThai, Boolean loaihd, Date tu, Date den, Pageable p);

    @Query("SELECT h FROM HoaDon h ORDER BY h.id DESC")
    Page<HoaDon> findAlls(Pageable p);

    @Query("SELECT h FROM HoaDon h where h.trangthai=?1 ORDER BY h.id DESC ")
    Page<HoaDon> findAllByTrangthai(Integer trangThai, Pageable p);

    Long countAllByTrangthai(Integer trangThai);

    Page<HoaDon> findAllByLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(Boolean loaihd, Date tu, Date den, Pageable p);

    Page<HoaDon> findAllByTrangthaiAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(Integer trangThai, Date tu, Date den, Pageable p);

    Page<HoaDon> findAllByTrangthaiAndLoaihoadon(Integer trangThai, Boolean loaihd, Pageable p);

    Page<HoaDon> findAllByNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(Date tu, Date den, Pageable p);

    Page<HoaDon> findAllByLoaihoadon(Boolean loaihd, Pageable p);

    List<HoaDon> findAllById(Integer id);

    List<HoaDon> findAllByTrangthaiAndLoaihoadon(Integer id, Boolean loadHD);

    @Query("SELECT h FROM HoaDon h ORDER BY h.id DESC")
    List<HoaDon> timHDGanNhat();

    @Query("SELECT h FROM HoaDon h  WHERE h.mahoadon =?1")
    HoaDon timHDTheoMaHD(String mahd);

    boolean existsById(Integer id);
}
