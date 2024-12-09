package entity;

import java.util.Date;

public class HoaDon {
    private String maHD;      // Mã hóa đơn
    private String maNV;      // Mã nhân viên
    private String maKH;      // Mã khách hàng
    private int trangThai; // 1 là đã thanh toán, 0 là chưa
    private Date ngayLap;     // Ngày lập hóa đơn
    private double tongTien;  // Tổng tiền
    private double tienKhachDua; // Tiền khách đưa

    // Constructor không tham số
    public HoaDon() {
    }

    // Constructor đầy đủ tham số
    public HoaDon(String maHD, String maNV, String maKH, int trangThai, Date ngayLap, double tongTien, double tienKhachDua) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.trangThai = trangThai;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.tienKhachDua = tienKhachDua;
    }

    // Getter và Setter
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maKH='" + maKH + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", ngayLap=" + ngayLap +
                ", tongTien=" + tongTien +
                ", tienKhachDua=" + tienKhachDua +
                '}';
    }
}

