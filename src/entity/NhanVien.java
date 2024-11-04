package entity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NhanVien {
	private String maNV;
    private String cccd;
    private String chucVu;
    private boolean trangThai;
    private String tenNV;
    private String sdt;
    private boolean gioiTinh;
    private Date ngaySinh;
    private String diaChi;
    private	Date ngayVaoLam;

    public NhanVien() {
    }

    public NhanVien(String maNV) {
        setMaNV(maNV);
    }

    

    public NhanVien(String maNV, String cccd, String chucVu, boolean trangThai, String tenNV, String sdt,
			boolean gioiTinh, Date ngaySinh, String diaChi, Date ngayVaoLam) {
		setMaNV(maNV);
		setCccd(cccd);
		setChucVu(chucVu);
		setTrangThai(trangThai);
		setTenNV(tenNV);
		setSdt(sdt);
		setGioiTinh(gioiTinh);
		setNgaySinh(ngaySinh);
		setDiaChi(diaChi);
		setNgayVaoLam(ngayVaoLam);
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public Date getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(Date ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public String getCccd() {
		return cccd;
	}

	public String cccd() {
        return cccd;
    }

    public void setCccd(String cccd) throws IllegalArgumentException {
    	cccd = cccd.trim();
        if (!cccd.matches("\\d{12}")) {
            throw new IllegalArgumentException("Mã định danh phải gồm đúng 12 chữ số!");
        }
        this.cccd = cccd;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String ten) throws IllegalArgumentException {
        ten = ten.trim();
        if (ten.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng!");
        }
        if (!ten.matches("^[\\p{L} ]+$")) {
            throw new IllegalArgumentException("Họ tên chỉ được chứa kí tự chữ và khoảng trắng!");
        }
        this.tenNV = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) throws IllegalArgumentException {
    	sdt = sdt.trim();
        if (!sdt.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
        }
        this.sdt = sdt;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) throws IllegalArgumentException {
        if(java.sql.Date.valueOf(LocalDate.now()).getYear() - ngaySinh.getYear() < 18)
            throw new IllegalArgumentException("Nhân viên phải đủ 18 tuổi trở lên");
        this.ngaySinh = ngaySinh;
    }

    @Override
    public String toString() {
        return maNV +" "+ tenNV;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.maNV);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NhanVien other = (NhanVien) obj;
        return Objects.equals(this.maNV, other.maNV);
    }
	
	
}
