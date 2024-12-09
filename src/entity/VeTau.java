package entity;

import java.util.Date;

public class VeTau {
    private String gaDi;
    private String gaDen;
    private String hoTenKH;
    private String soDienThoai;
    private String maVT;
    private Date ngayTao;
    private double tienKhachDua;
    private double tienThua;
    private String loaiToa;
    private String viTriGhe;
    private int giaVe;
    private String maTau;
    private String maHoaDon;
    private String maKH;

    // Constructor
    public VeTau(String gaDi, String gaDen, String hoTenKH, String soDienThoai, 
                 String maVT, Date ngayTao, double tienKhachDua, double tienThua, 
                 String loaiToa, String viTriGhe, int giaVe, String maTau, String maHoaDon, String maKH) {
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.hoTenKH = hoTenKH;
        this.soDienThoai = soDienThoai;
        this.maVT = maVT;
        this.ngayTao = ngayTao;
        this.tienKhachDua = tienKhachDua;
        this.tienThua = tienThua;
        this.loaiToa = loaiToa;
        this.viTriGhe = viTriGhe;
        this.giaVe = giaVe;
        this.maTau = maTau;
        this.maHoaDon = maHoaDon;
        this.maKH = maKH;
    }

    // Getters and Setters
    public String getGaDi() {
        return gaDi;
    }

    public void setGaDi(String gaDi) {
        this.gaDi = gaDi;
    }

    public String getGaDen() {
        return gaDen;
    }

    public void setGaDen(String gaDen) {
        this.gaDen = gaDen;
    }

    public String getHoTenKH() {
        return hoTenKH;
    }

    public void setHoTenKH(String hoTenKH) {
        this.hoTenKH = hoTenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    public double getTienThua() {
        return tienThua;
    }

    public void setTienThua(double tienThua) {
        this.tienThua = tienThua;
    }

    public String getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(String loaiToa) {
        this.loaiToa = loaiToa;
    }

    public String getViTriGhe() {
        return viTriGhe;
    }

    public void setViTriGhe(String viTriGhe) {
        this.viTriGhe = viTriGhe;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(int giaVe) {
        this.giaVe = giaVe;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    
    public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	@Override
    public String toString() {
        return "VeTau{" +
                "gaDi='" + gaDi + '\'' +
                ", gaDen='" + gaDen + '\'' +
                ", hoTenKH='" + hoTenKH + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", maVT='" + maVT + '\'' +
                ", ngayTao=" + ngayTao +
                ", tienKhachDua=" + tienKhachDua +
                ", tienThua=" + tienThua +
                ", loaiToa='" + loaiToa + '\'' +
                ", viTriGhe='" + viTriGhe + '\'' +
                ", giaVe=" + giaVe +
                ", maTau='" + maTau + '\'' +
                ", maHoaDon='" + maHoaDon + '\'' +
                '}';
    }
}

