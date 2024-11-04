package entity;

public class Tau {
    private String maTau;
    private String tenTau;
    private int soLuongToa;
    private Toa dsToa;

	

	public Tau(String maTau, String tenTau, int soLuongToa, Toa dsToa) {
		super();
		this.maTau = maTau;
		this.tenTau = tenTau;
		this.soLuongToa = soLuongToa;
		this.dsToa = dsToa;
	}

	public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
    	maTau=maTau.trim();
		if (!maTau.matches("^T[0-9]{3}$")) {
			throw new IllegalArgumentException("Mã Tàu không hợp lệ");
		}
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

	public int getSoLuongToa() {
		return soLuongToa;
	}

	public void setSoLuongToa(int soLuongToa) {
		this.soLuongToa = soLuongToa;
	}

	public Toa getDsToa() {
		return dsToa;
	}

	public void setDsToa(Toa dsToa) {
		this.dsToa = dsToa;
	}

	@Override
	public String toString() {
		return "Tau [maTau=" + maTau + ", tenTau=" + tenTau + ", soLuongToa=" + soLuongToa + ", dsToa=" + dsToa + "]";
	}
    
}
