package entity;

public class Toa {
    private String maToa;
    private String loaiToa;
    private int soGhe;
    private String viTriToa;
    private boolean trangThai;
	
	
	public Toa(String maToa, String loaiToa, int soGhe, String viTriToa, boolean trangThai) {
		super();
		this.maToa = maToa;
		this.loaiToa = loaiToa;
		this.soGhe = soGhe;
		this.viTriToa = viTriToa;
		this.trangThai = trangThai;
	}
	public String getMaToa() {
		return maToa;
	}
	public void setMaToa(String maToa) {
		maToa=maToa.trim();
		if (!maToa.matches("^TOA[0-9]{3}$")) {
			throw new IllegalArgumentException("Mã Toa không hợp lệ");
		}
		this.maToa = maToa;
	}
	public String getLoaiToa() {
		return loaiToa;
	}
	public void setLoaiToa(String loaiToa) {
		if (loaiToa.trim().equals(""))
			throw new IllegalArgumentException("Loại toa không được để trống");
		this.loaiToa = loaiToa;
	}
	
	public int getSoGhe() {
		return soGhe;
	}
	public void setSoGhe(int soGhe) {
		if (soGhe < 0) {
			throw new IllegalArgumentException("Số ghế không được nhỏ hơn 0");
        }
		this.soGhe = +soGhe;
	}
	public String getViTriToa() {
		return viTriToa;
	}
	public void setViTriToa(String viTriToa) {
		if (viTriToa.trim().equals(""))
			throw new IllegalArgumentException("Vị trí toa không được để trống");
		this.viTriToa = viTriToa;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "Toa [maToa=" + maToa + ", loaiToa=" + loaiToa + ", soGhe=" + soGhe + ", viTriToa=" + viTriToa
				+ ", trangThai=" + trangThai + "]";
	}
	
    
}