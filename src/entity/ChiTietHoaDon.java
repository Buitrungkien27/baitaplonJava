package entity;

public class ChiTietHoaDon {
    private String maHD;     // Mã hóa đơn (khóa chính, liên kết với bảng Hóa đơn)
    private String maVT;     // Mã vé tàu (khóa chính, liên kết với bảng Vé tàu)
    private int soLuong;     // Số lượng vé tàu
    private double gia;      // Giá vé
    private double thue;     // Thuế
    private double tongTien; // Tổng tiền

    // Constructor không tham số
    public ChiTietHoaDon() {
    }

    // Constructor đầy đủ tham số
    public ChiTietHoaDon(String maHD, String maVT, int soLuong, double gia, double thue, double tongTien) {
        this.maHD = maHD;
        this.maVT = maVT;
        this.soLuong = soLuong;
        this.gia = gia;
        this.thue = thue;
        this.tongTien = tongTien;
    }

    // Getter và Setter
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public double getThue() {
        return thue;
    }

    public void setThue(double thue) {
        this.thue = thue;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maVT='" + maVT + '\'' +
                ", soLuong=" + soLuong +
                ", gia=" + gia +
                ", thue=" + thue +
                ", tongTien=" + tongTien +
                '}';
    }
}

