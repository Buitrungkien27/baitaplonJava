package entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class KhachHang {

    private String maKH;
    private String tenKH;
    private boolean gioiTinh;
    private Date ngaySinh;
    private String sdt;
    private String diaChi;
    private String cccd;

    public KhachHang(String maKH, String tenKH, boolean gioiTinh, Date ngaySinh, String sdt, String diaChi,
                     String cccd) throws Exception {
        setMaKH(maKH);
        setTenKH(tenKH);
        setGioiTinh(gioiTinh);
        setNgaySinh(ngaySinh);
        setSdt(sdt);
        setDiaChi(diaChi);
        setCccd(cccd);
    }

    public KhachHang(String maKH) throws Exception {
        setMaKH(maKH);
    }

    public KhachHang() {
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) throws IllegalArgumentException {
        if (maKH == null || maKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được phép rỗng!");
        }
        String patternCustomerID = "^KH[0-9]{4}[0-9]{1}[0-9]{4}$";
        if (!Pattern.matches(patternCustomerID, maKH)) {
            throw new IllegalArgumentException("Mã khách hàng phải đúng định dạng!");
        }
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String ten) throws IllegalArgumentException {
        if (ten == null || ten.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng!");
        }
        if (!ten.matches("^[\\p{L} ]+$")) {
            throw new IllegalArgumentException("Họ tên chỉ được chứa kí tự chữ và khoảng trắng!");
        }
        this.tenKH = ten.trim();
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gender) {
        this.gioiTinh = gender;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) throws IllegalArgumentException {
        if (java.sql.Date.valueOf(LocalDate.now()).getYear() - ngaySinh.getYear() < 18) {
            throw new IllegalArgumentException("Khách hàng phải đủ 18 tuổi trở lên");
        }
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) throws IllegalArgumentException {
        if (sdt == null || sdt.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được phép rỗng!");
        }
        if (!sdt.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
        }
        this.sdt = sdt.trim();
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String address) throws IllegalArgumentException {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được phép rỗng!");
        }
        this.diaChi = address.trim();
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) throws IllegalArgumentException {
        if (cccd == null || cccd.trim().isEmpty()) {
            throw new IllegalArgumentException("Căn cước công dân không được phép rỗng!");
        }
        if (!cccd.matches("\\d{12}")) {
            throw new IllegalArgumentException("Mã định danh phải gồm đúng 12 chữ số!");
        }
        this.cccd = cccd.trim();
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKH, other.maKH);
    }

    @Override
    public String toString() {
        return maKH;
    }
}
