package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class KhachHang implements Serializable{
		public static final String ID_EMPTY = "Mã khách hàng không được phép rỗng";
	    public static final String NAME_EMPTY = "Họ tên không được phép rỗng";
	    public static final String DATEBIRTH_ERROR = "Ngày sinh phải nhỏ hơn ngày hiện tại";
	    public static final String PHONENUMBER_ERROR = "Số điện thoại gồm 10 chữ số, bắt đầu từ số 0";
	    public static final String GENDER_EMPTY = "Giới tính không được phép rỗng";
	    public static final String ADDRESS_EMPTY = "Địa chỉ không được phép rỗng";
	    public static final String CCCD_EMPTY = "Căn cước công dân không được phép rỗng";

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

	    public void setMaKH(String maKH) throws Exception {
	        String patternCustomerID = "^KH[0-9]{4}[0-9]{1}[0-9]{4}$";
	        if (!Pattern.matches(patternCustomerID, maKH)) {
	            throw new Exception(ID_EMPTY);
	        }
	        this.maKH = maKH;
	    }

	    public String getTenKH() {
	        return tenKH;
	    }

	    public void setTenKH(String name) throws Exception {
	        if (name.trim().equals("")) {
	            throw new Exception(NAME_EMPTY);
	        }
	        this.tenKH = name;
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

	    public void setNgaySinh(Date dateOfBirth) throws Exception {
	        if (dateOfBirth.after(java.sql.Date.valueOf(LocalDate.now()))) {
	            throw new Exception(DATEBIRTH_ERROR);
	        }		
	        this.ngaySinh = dateOfBirth;
	    }

	    public String getSdt() {
	        return sdt;
	    }

	    public void setSdt(String phoneNumber) throws Exception {
	   
	        this.sdt = phoneNumber;
	    }

	    public String getDiaChi() {
	        return diaChi;
	    }

	    public void setDiaChi(String address) throws Exception {
	       if (address.trim().equals("")) {
	            throw new Exception(ADDRESS_EMPTY);
	        }
	        this.diaChi = address;
	    }

	    public String getCccd() {
	        return cccd;
	    }

	    public void setCccd(String cccd) throws IllegalArgumentException {
	    	cccd = cccd.trim();
	        if (!cccd.matches("\\d{12}")) {
	            throw new IllegalArgumentException("Mã định danh phải gồm đúng 12 chữ số!");
	        }
	        this.cccd = cccd;
	    }
	    
	    @Override
	    public int hashCode() {
	        int hash = 7;
	        hash = 79 * hash + Objects.hashCode(this.maKH);
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
	        final KhachHang other = (KhachHang) obj;
	        return Objects.equals(this.maKH, other.maKH);
	    }

	    @Override
	    public String toString() {

	        return maKH;
	    }
}
