-- Tạo database ở đường dẫn sql mặc định
USE master
GO
-- Xóa cái trước đó nếu mà đã có
if exists (select * from sysdatabases where name='QLBanVe')
		drop database QLBanVe
GO

-- Tìm vị trí lưu trữ của máy tính
DECLARE @device_directory nvarchar(520)
SELECT @device_directory = SUBSTRING(filename, 1, CHARINDEX(N'master.mdf', LOWER(filename)) - 1)
FROM master.dbo.sysaltfiles WHERE dbid = 1 AND fileid = 1

-- Tạo database
EXECUTE (N'CREATE DATABASE QLBanVe
  ON PRIMARY (NAME = N''QLBanVe'', FILENAME = N''' + @device_directory + N'QLBanVe.mdf'')
  LOG ON (NAME = N''QLBanVe_log'',  FILENAME = N''' + @device_directory + N'QLBanVe.ldf'')')

GO

use QLBanVe

CREATE TABLE TaiKhoan (
  maNV varchar(255) NOT NULL, 
  password   nvarchar(255) NOT NULL, 
  PRIMARY KEY (maNV));

CREATE TABLE KhachHang (
  maKH varchar(255) NOT NULL, 
  tenKH        nvarchar(255) NOT NULL, 
  ngaySinh date NOT NULL, 
  gioiTinh      bit NOT NULL, 
  sdt varchar(255) NOT NULL, 
  cccd varchar(255) NOT NULL, 
  diaChi     nvarchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  PRIMARY KEY (maKH));

CREATE TABLE NhanVien (
  maNV            varchar(255) NOT NULL, 
  tenNV                  nvarchar(255) NOT NULL, 
  chucVu                  nvarchar(255) NOT NULL, 
  trangThai                bit NOT NULL, 
  ngaySinh           date NOT NULL, 
  gioiTinh                bit NOT NULL, 
  sdt           varchar(255) NOT NULL, 
  cccd varchar(255) NOT NULL, 
  ngayVaoLam             date NOT NULL,  
  diaChi nvarchar(255) NOT NULL,
  PRIMARY KEY (maNV));

CREATE TABLE HoaDon (
  maHD            varchar(255) NOT NULL, 
  maNV         varchar(255) NOT NULL, 
  maKH         varchar(255) NOT NULL, 
  trangThai             int NOT NULL, 
  ngayLap            datetime NOT NULL, 
  tongTien           float(10) NOT NULL, 
  tienKhachDua         float(10) NOT NULL, 
  PRIMARY KEY (maHD));

CREATE TABLE ChiTietHoaDon (
  maHD          varchar(255) NOT NULL, 
  maVT        varchar(255) NOT NULL, 
  soLuong         int NOT NULL, 
  gia            float(10) NOT NULL, 
  thue              float(10) NOT NULL,  
  tongTien	      float(10) NOT NULL,
  PRIMARY KEY (maHD, maVT));

CREATE TABLE HoaDonDoiTra (
  maHDT varchar(255) NOT NULL, 
  maHD       varchar(255) NOT NULL, 
  loai          int NULL, 
  trangThai        int NOT NULL, 
  ngayLap     date NULL, 
  maNV    varchar(255) NOT NULL, 
  tienTra        money NOT NULL,
  lyDo        nvarchar(255) NOT NULL,
  PRIMARY KEY (maHDT));

CREATE TABLE ChiTietHoaDonDoiTra (
  maVT     varchar(255) NOT NULL, 
  maHDT varchar(255) NOT NULL, 
  soLuong      int NULL, 
  gia      money NULL,
  PRIMARY KEY (maVT, 
  maHDT));

CREATE TABLE Phien (
  maPhien    varchar(255) NOT NULL, 
  maNV varchar(255) NOT NULL, 
  thoiGianBD  datetime NOT NULL, 
  thoiGianKT    datetime NOT NULL, 
  PRIMARY KEY (maPhien));

CREATE TABLE ChuyenTau(
	maCT varchar(255) NOT NULL,
	ngayKhoiHanh date NOT NULL,
	gioKhoiHanh time NOT NULL,
	trangThai int NOT NULL,
	maTau varchar(255) NOT NULL,
	maTT varchar(255) NOT NULL,
	PRIMARY KEY(maCT));

CREATE TABLE TuyenTau(
	maTT varchar(255) NOT NULL,
	gaDi nvarchar(255) NOT NULL,
	gaDen nvarchar(255) NOT NULL,
	khoangCach int NOT NULL,
	PRIMARY KEY(maTT));

CREATE TABLE Tau(
	maTau varchar(255) NOT NULL,
	tenTau nvarchar(255) NOT NULL,
	soLuongToa int NOT NULL,
	PRIMARY KEY(maTau));

CREATE TABLE Toa(
	maToa varchar(255) NOT NULL,
	loaiToa nvarchar(255) NOT NULL,
	soGhe int NOT NULL,
	viTriToa varchar(255) NOT NULL,
	trangThai int NOT NULL,
	PRIMARY KEY(maToa));

CREATE TABLE Tau_Toa (
    maTau VARCHAR(255),
    maToa VARCHAR(255),
    PRIMARY KEY (maTau, maToa),
    FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    FOREIGN KEY (maToa) REFERENCES Toa(maToa)
);
	
CREATE TABLE VeTau (
	maVT varchar(255) NOT NULL,
	maKH varchar(255) NOT NULL,
	maNV varchar(255) NOT NULL,
	maCT varchar(255) NOT NULL,
	maToa varchar(255) NOT NULL,
	viTriGhe int NOT NULL,
	soLuongVe int NOT NULL,
	giaVe float(10) NOT NULL,
	PRIMARY KEY(maVT));

ALTER TABLE HoaDon 
ADD CONSTRAINT FK_HoaDon_NhanVien 
FOREIGN KEY (maNV) REFERENCES NhanVien(maNV);

ALTER TABLE HoaDon 
ADD CONSTRAINT FK_HoaDon_KhachHang 
FOREIGN KEY (maKH) REFERENCES KhachHang(maKH);

ALTER TABLE ChiTietHoaDon 
ADD CONSTRAINT FK_ChiTietHoaDon_HoaDon 
FOREIGN KEY (maHD) REFERENCES HoaDon(maHD);

ALTER TABLE ChiTietHoaDon 
ADD CONSTRAINT FK_ChiTietHoaDon_VeTau 
FOREIGN KEY (maVT) REFERENCES VeTau(maVT);

ALTER TABLE HoaDonDoiTra 
ADD CONSTRAINT FK_HoaDonDoiTra_HoaDon 
FOREIGN KEY (maHD) REFERENCES HoaDon(maHD);

ALTER TABLE HoaDonDoiTra 
ADD CONSTRAINT FK_HoaDonDoiTra_NhanVien 
FOREIGN KEY (maNV) REFERENCES NhanVien(maNV);

ALTER TABLE ChiTietHoaDonDoiTra 
ADD CONSTRAINT FK_ChiTietHoaDonDoiTra_HoaDonDoiTra 
FOREIGN KEY (maHDT) REFERENCES HoaDonDoiTra(maHDT);

ALTER TABLE ChiTietHoaDonDoiTra 
ADD CONSTRAINT FK_ChiTietHoaDonDoiTra_VeTau 
FOREIGN KEY (maVT) REFERENCES VeTau(maVT);

ALTER TABLE Phien ADD CONSTRAINT FK_Phien_TaiKhoan FOREIGN KEY (maNV) REFERENCES TaiKhoan (maNV);
ALTER TABLE TaiKhoan ADD CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien (maNV);

ALTER TABLE ChuyenTau 
ADD CONSTRAINT FK_ChuyenTau_Tau 
FOREIGN KEY (maTau) REFERENCES Tau(maTau);

ALTER TABLE ChuyenTau 
ADD CONSTRAINT FK_ChuyenTau_TuyenTau 
FOREIGN KEY (maTT) REFERENCES TuyenTau(maTT);

ALTER TABLE VeTau 
ADD CONSTRAINT FK_VeTau_KhachHang 
FOREIGN KEY (maKH) REFERENCES KhachHang(maKH);

ALTER TABLE VeTau 
ADD CONSTRAINT FK_VeTau_NhanVien 
FOREIGN KEY (maNV) REFERENCES NhanVien(maNV);

ALTER TABLE VeTau 
ADD CONSTRAINT FK_VeTau_ChuyenTau 
FOREIGN KEY (maCT) REFERENCES ChuyenTau(maCT);

ALTER TABLE VeTau 
ADD CONSTRAINT FK_VeTau_Toa 
FOREIGN KEY (maToa) REFERENCES Toa(maToa);

INSERT INTO NhanVien (maNV, tenNV, chucVu, trangThai, ngaySinh, gioiTinh, sdt, cccd, ngayVaoLam, diaChi)
VALUES (N'NV120042024001', N'Bùi Tấn Phát', N'Quản Lí', 1, CAST(N'2004-02-14' AS Date), 1, N'0929528914', N'051204000290', CAST(N'2004-10-14' AS Date), N'Quận Bình Tân, TP HCM');

INSERT INTO TaiKhoan (maNV, password)
VALUES ('NV120042024001', N'c22a880fe966e5b74a1f5adaefcac0ad');

ALTER TABLE VeTau ADD gaDi varchar(255) NOT NULL;
ALTER TABLE VeTau ADD gaDen varchar(255) NOT NULL;
ALTER TABLE VeTau ADD hoTenKH varchar(255) NOT NULL;
ALTER TABLE VeTau ADD soDienThoai varchar(15) NOT NULL;
ALTER TABLE VeTau ADD ngayTao date NOT NULL;
ALTER TABLE VeTau ADD tienKhachDua float(10) NOT NULL;
ALTER TABLE VeTau ADD tienThua float(10) NOT NULL;
ALTER TABLE VeTau ADD loaiToa varchar(255) NOT NULL;

EXEC sp_columns VeTau;

ALTER TABLE VeTau
ALTER COLUMN viTriGhe nvarchar(10) NOT NULL;

ALTER TABLE VeTau
ALTER COLUMN loaiToa varchar(255) NULL;
ALTER TABLE VeTau
ADD CONSTRAINT CHK_LoaiToa CHECK (loaiToa IN ('Toa 1', 'Toa 2', 'Toa 3', 'Toa 4', 'Toa 5'));

ALTER TABLE VeTau
ADD CONSTRAINT DF_maCT DEFAULT 'DefaultValue' FOR maCT;

ALTER TABLE VeTau DROP CONSTRAINT FK_VeTau_KhachHang;
ALTER TABLE VeTau DROP CONSTRAINT FK_VeTau_NhanVien;
ALTER TABLE VeTau DROP CONSTRAINT FK_VeTau_ChuyenTau;
ALTER TABLE VeTau DROP CONSTRAINT FK_VeTau_Toa;

ALTER TABLE VeTau DROP COLUMN maKH;
ALTER TABLE VeTau DROP COLUMN maNV;
ALTER TABLE VeTau DROP COLUMN maCT;
ALTER TABLE VeTau DROP COLUMN maToa;
SELECT maVT, gaDi, gaDen, hoTenKH, soDienThoai, ngayTao, tienKhachDua, tienThua, loaiToa, viTriGhe, giaVe
FROM VeTau;
ALTER TABLE VeTau DROP COLUMN soLuongVe;

ALTER TABLE Tau
ADD 
    danhSachGhe NVARCHAR(255),
    gaDi NVARCHAR(50),
    gaDen NVARCHAR(50),
    thoiGianDi DATETIME,
    thoiGianDen DATETIME;
    
ALTER TABLE Tau
DROP COLUMN soLuongToa;

delete from TaiKhoan
where maNV = 'NV120042024001'
delete from NhanVien
where maNV = 'NV120042024001'
INSERT INTO NhanVien (maNV, tenNV, chucVu, trangThai, ngaySinh, gioiTinh, sdt, cccd, ngayVaoLam, diaChi)
VALUES (N'NV120042024001', N'Bùi Tấn Phát', N'Quản Lý', 1, CAST(N'2004-02-14' AS Date), 1, N'0929528914', N'051204000290', CAST(N'2024-10-14' AS Date), N'Quận Bình Tân, TP HCM');

INSERT INTO TaiKhoan (maNV, password)
VALUES ('NV120042024001', N'c22a880fe966e5b74a1f5adaefcac0ad');

DELETE FROM Tau;
EXEC sp_rename 'dbo.Tau.danhSachGhe', 'loaiToa', 'COLUMN';

ALTER TABLE Tau
ADD trangThai NVARCHAR(50);

ALTER TABLE Tau ADD soGhe INT;


