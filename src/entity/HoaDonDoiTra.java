package entity;

import java.math.BigDecimal;
import java.util.Date;

public class HoaDonDoiTra {
	private String maHDT;
	private String maHD;
	private int loai;
	private int trangThai;
	private Date ngayLap;
	private String maNV;
	private BigDecimal tienTra;
	private String lyDo;
	
	
	public HoaDonDoiTra(String maHD, int loai, int trangThai, Date ngayLap, String maNV, BigDecimal tienTra,
			String lyDo) {
		super();
		this.maHD = maHD;
		this.loai = loai;
		this.trangThai = trangThai;
		this.ngayLap = ngayLap;
		this.maNV = maNV;
		this.tienTra = tienTra;
		this.lyDo = lyDo;
	}
	public HoaDonDoiTra(String maHDT, String maHD, int loai, int trangThai, Date ngayLap, String maNV,
			BigDecimal tienTra, String lyDo) {
		super();
		this.maHDT = maHDT;
		this.maHD = maHD;
		this.loai = loai;
		this.trangThai = trangThai;
		this.ngayLap = ngayLap;
		this.maNV = maNV;
		this.tienTra = tienTra;
		this.lyDo = lyDo;
	}
	public String getMaHDT() {
		return maHDT;
	}
	public void setMaHDT(String maHDT) {
		this.maHDT = maHDT;
	}
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public int getLoai() {
		return loai;
	}
	public void setLoai(int loai) {
		this.loai = loai;
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
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public BigDecimal getTienTra() {
		return tienTra;
	}
	public void setTienTra(BigDecimal tienTra) {
		this.tienTra = tienTra;
	}
	public String getLyDo() {
		return lyDo;
	}
	public void setLyDo(String lyDo) {
		this.lyDo = lyDo;
	}
	
	
}
