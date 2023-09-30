# sneaker_sophia
create database GIAYRIU
go

use GIAYRIU
go

create Table Giay(
	Id uniqueidentifier DEFAULT newid() primary key,
	TenGiay nvarchar(50),
	CreateAt DateTime,
	UpdateAt DateTime,
)

create Table KichCo(
	Id uniqueidentifier DEFAULT newid() primary key,
	Size int,
)

create Table LoaiGiay(
	Id uniqueidentifier DEFAULT newid() primary key,
	LoaiGiay nvarchar(50),
)

create Table Hang(
	Id uniqueidentifier DEFAULT newid() primary key,
	TenHang nvarchar(50),
)

create Table DeGiay(
	Id uniqueidentifier DEFAULT newid() primary key,
	TenDeGiay nvarchar(50),
)

create Table MauSac(
	Id uniqueidentifier DEFAULT newid() primary key,
	TenMau nvarchar(50),
)

create Table ChiTietGiay(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdGiay uniqueidentifier REFERENCES Giay(Id),
	IdKichCo uniqueidentifier REFERENCES KichCo(Id), 
	IdLoaiGiay uniqueidentifier REFERENCES LoaiGiay(Id), 
	IdHang uniqueidentifier REFERENCES Hang(Id), 
	IdDeGiay uniqueidentifier REFERENCES DeGiay(Id), 
	IdMauSac uniqueidentifier REFERENCES MauSac(Id), 
	Code VARCHAR(30) UNIQUE,
	DonGia MONEY,
	SoLuong int,
	TrangThai int
)

create Table Anh(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdChiTietGiay uniqueidentifier REFERENCES ChiTietGiay(Id),
	TenAnh nvarchar(100),
	AnhChinh BIT DEFAULT 0
)

create Table VaiTro(
	Id uniqueidentifier DEFAULT newid() primary key,
	TenVaiTro nvarchar(50),
	TrangThai INT
)

create Table NguoiDung(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdVaiTro uniqueidentifier REFERENCES VaiTro(Id),
	TenNguoiDung nvarchar(100),
	MatKhau nvarchar(30),
	Email nvarchar(50) UNIQUE,
	AnhDaiDien nvarchar(100),
	TrangThai int
)

create Table GioHang(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdNguoiDung uniqueidentifier REFERENCES NguoiDung(Id),
	CreateAt DATETIME,
	UpdateAt DATETIME,
)

create Table GioHangChiTiet(
	IdGioHang uniqueidentifier REFERENCES GioHang(Id),
	IdChiTietGiay uniqueidentifier REFERENCES ChiTietGiay(Id),
	SoLuong INT,
	CreateAt DATETIME,
	UpdateAt DATETIME,
)

create Table DiaChi(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdNguoiDung uniqueidentifier REFERENCES NguoiDung(Id),
	TenDiaChi nvarchar(100),
	SoDienThoai varchar(15),
	DiaChiCuThe nvarchar(200),
	PhuongXa nvarchar(100),
	QuanHuyen nvarchar(100),
	Tinh nvarchar(50),
	DiaChiMacDinh BIT DEFAULT 0
)

create Table ThongBao(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdNguoiDung uniqueidentifier REFERENCES NguoiDung(Id),
	Kieu nvarchar(50),
	TieuDe nvarchar(200),
	NoiDung nvarchar(200),
	HanhDong int
)

create Table Voucher(
	Id uniqueidentifier DEFAULT newid() primary key,
	Code varchar(30) UNIQUE,
	TenVoucher nvarchar(100),
	SoLuong int,
	PhanTramGiam Float,
	GiaTriHoaDonToiThieu MONEY,
	NgayBatDau DATE,
	NgayKetThuc DATE
)

create Table NguoiDungVoucher(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdVoucher uniqueidentifier REFERENCES Voucher(Id),
	IdNguoiDung uniqueidentifier REFERENCES NguoiDung(Id),
	TrangThai int
)

create Table HoaDon(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdVoucher uniqueidentifier REFERENCES Voucher(Id),
	IdNguoiDung uniqueidentifier REFERENCES NguoiDung(Id),
	CodeHoaDon varchar(30) UNIQUE,
	LoaiHoaDon int,
	TenKhachHang nvarchar(100),
	SoDienThoai varchar(15),
	DiaChi nvarchar(200),
	PhiShip MONEY,
	TienThua MONEY,
	TongTien MONEY,
	NgayTao DATE,
	NgayChuyenKhoan DATE,
	NgayShip DATE,
	NgayMongMuonNhan DATE,
	NgayNhan DATE,
	TrangThai int
)

create Table HoaDonChiTiet(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdChiTietGiay uniqueidentifier REFERENCES ChiTietGiay(Id),
	IdHoaDon uniqueidentifier REFERENCES HoaDon(Id),
	SoLuong int,
	DonGia MONEY,
	TrangThai int
)

create Table LichSuHoaDon(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdHoaDon uniqueidentifier REFERENCES HoaDon(Id),
	GhiChu nvarchar(200),
)

create Table HinhThucThanhToan(
	Id uniqueidentifier DEFAULT newid() primary key,
	IdHoaDon uniqueidentifier REFERENCES HoaDon(Id),
	MaHinhThuc nchar(15),
	TenHinhThuc nvarchar(100),
	SoTien MONEY,
	TrangThai int
)



-- Bảng Giay
INSERT INTO Giay (Id, TenGiay, CreateAt, UpdateAt)
VALUES
    (NEWID(), N'Giày thể thao', GETDATE(), GETDATE()),
    (NEWID(), N'Giày chạy', GETDATE(), GETDATE()),
    (NEWID(), N'Giày cao gót', GETDATE(), GETDATE()),
    (NEWID(), N'Giày dép', GETDATE(), GETDATE()),
    (NEWID(), N'Giày lười', GETDATE(), GETDATE());

-- Bảng KichCo
INSERT INTO KichCo (Id, Size)
VALUES
    (NEWID(), 36),
    (NEWID(), 37),
    (NEWID(), 38),
    (NEWID(), 39),
    (NEWID(), 40);

-- Bảng LoaiGiay
INSERT INTO LoaiGiay (Id, LoaiGiay)
VALUES
    (NEWID(), N'Nam'),
    (NEWID(), N'Nữ'),
    (NEWID(), N'Trẻ em'),
    (NEWID(), N'Unisex');

-- Bảng Hang
INSERT INTO Hang (Id, TenHang)
VALUES
    (NEWID(), N'Nike'),
    (NEWID(), N'Adidas'),
    (NEWID(), N'Puma'),
    (NEWID(), N'Converse'),
    (NEWID(), N'Vans');

-- Bảng DeGiay
INSERT INTO DeGiay (Id, TenDeGiay)
VALUES
    (NEWID(), N'Chạy bộ'),
    (NEWID(), N'Đi làm'),
    (NEWID(), N'Đi tiệc'),
    (NEWID(), N'Đi biển'),
    (NEWID(), N'Thể thao');

-- Bảng MauSac
INSERT INTO MauSac (Id, TenMau)
VALUES
    (NEWID(), N'Trắng'),
    (NEWID(), N'Đen'),
    (NEWID(), N'Xanh'),
    (NEWID(), N'Đỏ'),
    (NEWID(), N'Hồng');


-- Bảng ChiTietGiay
select * from ChiTietGiay
DECLARE @Giay1 uniqueidentifier, @KichCo1 uniqueidentifier, @LoaiGiay1 uniqueidentifier, @Hang1 uniqueidentifier, @DeGiay1 uniqueidentifier, @MauSac1 uniqueidentifier;
DECLARE @Giay2 uniqueidentifier, @KichCo2 uniqueidentifier, @LoaiGiay2 uniqueidentifier, @Hang2 uniqueidentifier, @DeGiay2 uniqueidentifier, @MauSac2 uniqueidentifier;
DECLARE @Giay3 uniqueidentifier, @KichCo3 uniqueidentifier, @LoaiGiay3 uniqueidentifier, @Hang3 uniqueidentifier, @DeGiay3 uniqueidentifier, @MauSac3 uniqueidentifier;
DECLARE @Giay4 uniqueidentifier, @KichCo4 uniqueidentifier, @LoaiGiay4 uniqueidentifier, @Hang4 uniqueidentifier, @DeGiay4 uniqueidentifier, @MauSac4 uniqueidentifier;
DECLARE @Giay5 uniqueidentifier, @KichCo5 uniqueidentifier, @LoaiGiay5 uniqueidentifier, @Hang5 uniqueidentifier, @DeGiay5 uniqueidentifier, @MauSac5 uniqueidentifier;

SELECT TOP 5 @Giay1 = Id FROM Giay;
SELECT TOP 5 @KichCo1 = Id FROM KichCo;
SELECT TOP 5 @LoaiGiay1 = Id FROM LoaiGiay;
SELECT TOP 5 @Hang1 = Id FROM Hang;
SELECT TOP 5 @DeGiay1 = Id FROM DeGiay;
SELECT TOP 5 @MauSac1 = Id FROM MauSac;


INSERT INTO ChiTietGiay (Id, IdGiay, IdKichCo, IdLoaiGiay, IdHang, IdDeGiay, IdMauSac, Code, DonGia, SoLuong, TrangThai)
VALUES
    (NEWID(), @Giay1, @KichCo1, @LoaiGiay1, @Hang1, @DeGiay1, @MauSac1, 'ABC123', 100000, 50, 1),
    (NEWID(), @Giay1, @KichCo2, @LoaiGiay2, @Hang2, @DeGiay2, @MauSac2, 'XYZ456', 150000, 30, 1),
    (NEWID(), @Giay2, @KichCo3, @LoaiGiay3, @Hang3, @DeGiay3, @MauSac3, '123ABC', 200000, 25, 1),
    (NEWID(), @Giay3, @KichCo4, @LoaiGiay4, @Hang4, @DeGiay4, @MauSac4, '456XYZ', 180000, 40, 1),
    (NEWID(), @Giay4, @KichCo5, @LoaiGiay5, @Hang5, @DeGiay5, @MauSac5, '789DEF', 220000, 20, 1);


-- Bảng Anh
INSERT INTO Anh (Id, IdChiTietGiay, TenAnh, AnhChinh)
VALUES
    (NEWID(), (SELECT TOP 1 Id FROM ChiTietGiay), 'anh1.jpg', 1),
    (NEWID(), (SELECT TOP 2 Id FROM ChiTietGiay), 'anh2.jpg', 0),
    (NEWID(), (SELECT TOP 3 Id FROM ChiTietGiay), 'anh3.jpg', 0),
    (NEWID(), (SELECT TOP 4 Id FROM ChiTietGiay), 'anh4.jpg', 1),
    (NEWID(), (SELECT TOP 5 Id FROM ChiTietGiay), 'anh5.jpg', 0);


	-- Bảng VaiTro
INSERT INTO VaiTro (Id, TenVaiTro, TrangThai)
VALUES
    (NEWID(), N'Admin', 1),
    (NEWID(), N'User', 1),
    (NEWID(), N'Moderator', 1),
    (NEWID(), N'Guest', 0),
    (NEWID(), N'Editor', 1);

-- Bảng NguoiDung
DECLARE @VaiTro1 uniqueidentifier, @VaiTro2 uniqueidentifier, @VaiTro3 uniqueidentifier, @VaiTro4 uniqueidentifier, @VaiTro5 uniqueidentifier;

SELECT TOP 5 @VaiTro1 = Id FROM VaiTro WHERE TenVaiTro = N'Admin';
SELECT TOP 5 @VaiTro2 = Id FROM VaiTro WHERE TenVaiTro = N'User';
SELECT TOP 5 @VaiTro3 = Id FROM VaiTro WHERE TenVaiTro = N'Moderator';
SELECT TOP 5 @VaiTro4 = Id FROM VaiTro WHERE TenVaiTro = N'Guest';
SELECT TOP 5 @VaiTro5 = Id FROM VaiTro WHERE TenVaiTro = N'Editor';

INSERT INTO NguoiDung (Id, IdVaiTro, TenNguoiDung, MatKhau, Email, AnhDaiDien, TrangThai)
VALUES
    (NEWID(), @VaiTro1, N'JohnDoe', N'password123', N'john@example.com', NULL, 1),
    (NEWID(), @VaiTro2, N'JaneSmith', N'pass456', N'jane@example.com', NULL, 1),
    (NEWID(), @VaiTro2, N'MichaelBrown', N'123abc', N'michael@example.com', NULL, 1),
    (NEWID(), @VaiTro3, N'SusanLee', N'susanpass', N'susan@example.com', NULL, 0),
    (NEWID(), @VaiTro4, N'DavidWilson', N'davidpw', N'david@example.com', NULL, 1);


-- Bảng GioHang
INSERT INTO GioHang (Id, IdNguoiDung, CreateAt, UpdateAt)
VALUES
    (NEWID(), (SELECT TOP 1 Id FROM NguoiDung), GETDATE(), GETDATE()),
    (NEWID(), (SELECT TOP 2 Id FROM NguoiDung), GETDATE(), GETDATE()),
    (NEWID(), (SELECT TOP 3 Id FROM NguoiDung), GETDATE(), GETDATE()),
    (NEWID(), (SELECT TOP 4 Id FROM NguoiDung), GETDATE(), GETDATE()),
    (NEWID(), (SELECT TOP 5 Id FROM NguoiDung), GETDATE(), GETDATE());



--  bảng Voucher
INSERT INTO Voucher (Code, TenVoucher, SoLuong, PhanTramGiam, GiaTriHoaDonToiThieu, NgayBatDau, NgayKetThuc)
VALUES
    ('CODE001', 'Voucher 1', 10, 10.0, 100000.00, '2023-08-01', '2023-08-31'),
    ('CODE002', 'Voucher 2', 20, 15.0, 150000.00, '2023-09-01', '2023-09-30'),
    ('CODE003', 'Voucher 3', 5, 20.0, 200000.00, '2023-10-01', '2023-10-31'),
    ('CODE004', 'Voucher 4', 8, 12.0, 80000.00, '2023-11-01', '2023-11-30'),
    ('CODE005', 'Voucher 5', 15, 18.0, 120000.00, '2023-12-01', '2023-12-31');

-- DiaChi
DECLARE @DiaChi1 uniqueidentifier, @DiaChi2 uniqueidentifier, @DiaChi3 uniqueidentifier, @DiaChi4 uniqueidentifier, @DiaChi5 uniqueidentifier;

SELECT TOP 5 @DiaChi1 = Id FROM NguoiDung WHERE TenNguoiDung = N'JohnDoe';
SELECT TOP 5 @DiaChi2 = Id FROM NguoiDung WHERE TenNguoiDung = N'JaneSmith';
SELECT TOP 5 @DiaChi3 = Id FROM NguoiDung WHERE TenNguoiDung = N'MichaelBrown';
SELECT TOP 5 @DiaChi4 = Id FROM NguoiDung WHERE TenNguoiDung = N'SusanLee';
SELECT TOP 5 @DiaChi5 = Id FROM NguoiDung WHERE TenNguoiDung = N'DavidWilson';

INSERT INTO DiaChi (IdNguoiDung, TenDiaChi, SoDienThoai, DiaChiCuThe, PhuongXa, QuanHuyen, Tinh, DiaChiMacDinh)
VALUES
    (@DiaChi1, 'Địa chỉ 1', '123456789', 'Địa chỉ số 1', 'Phường/Xã 1', 'Quận/Huyện 1', 'Tỉnh/Thành phố 1', 1),
    (@DiaChi2, 'Địa chỉ 2', '987654321', 'Địa chỉ số 2', 'Phường/Xã 2', 'Quận/Huyện 2', 'Tỉnh/Thành phố 2', 0),
    (@DiaChi3, 'Địa chỉ 3', '111222333', 'Địa chỉ số 3', 'Phường/Xã 3', 'Quận/Huyện 3', 'Tỉnh/Thành phố 3', 0),
    (@DiaChi4, 'Địa chỉ 4', '444555666', 'Địa chỉ số 4', 'Phường/Xã 4', 'Quận/Huyện 4', 'Tỉnh/Thành phố 4', 1),
    (@DiaChi5, 'Địa chỉ 5', '777888999', 'Địa chỉ số 5', 'Phường/Xã 5', 'Quận/Huyện 5', 'Tỉnh/Thành phố 5', 0);


-- ThongBao
DECLARE @ThongBao1 uniqueidentifier, @ThongBao2 uniqueidentifier, @ThongBao3 uniqueidentifier, @ThongBao4 uniqueidentifier, @ThongBao5 uniqueidentifier;

SELECT TOP 5 @ThongBao1 = Id FROM NguoiDung WHERE TenNguoiDung = N'JohnDoe';
SELECT TOP 5 @ThongBao2 = Id FROM NguoiDung WHERE TenNguoiDung = N'JaneSmith';
SELECT TOP 5 @ThongBao3 = Id FROM NguoiDung WHERE TenNguoiDung = N'MichaelBrown';
SELECT TOP 5 @ThongBao4 = Id FROM NguoiDung WHERE TenNguoiDung = N'SusanLee';
SELECT TOP 5 @ThongBao5 = Id FROM NguoiDung WHERE TenNguoiDung = N'DavidWilson';

INSERT INTO ThongBao (IdNguoiDung, Kieu, TieuDe, NoiDung, HanhDong)
VALUES
    (@ThongBao1, 'Loại thông báo 1', 'Tiêu đề thông báo 1', 'Nội dung thông báo 1', 1),
    (@ThongBao2, 'Loại thông báo 2', 'Tiêu đề thông báo 2', 'Nội dung thông báo 2', 2),
    (@ThongBao3, 'Loại thông báo 3', 'Tiêu đề thông báo 3', 'Nội dung thông báo 3', 1),
    (@ThongBao4, 'Loại thông báo 4', 'Tiêu đề thông báo 4', 'Nội dung thông báo 4', 2),
    (@ThongBao5, 'Loại thông báo 5', 'Tiêu đề thông báo 5', 'Nội dung thông báo 5', 1);


	-- Tạo các biến để lưu Id của các hóa đơn
DECLARE @HoaDon1 uniqueidentifier, @HoaDon2 uniqueidentifier, @HoaDon3 uniqueidentifier, @HoaDon4 uniqueidentifier, @HoaDon5 uniqueidentifier;
select * from HoaDon
SELECT TOP 5 @HoaDon1 = Id FROM HoaDon WHERE CodeHoaDon = '123456';
SELECT TOP 5 @HoaDon2 = Id FROM HoaDon WHERE CodeHoaDon = '789012';
SELECT TOP 5 @HoaDon3 = Id FROM HoaDon WHERE CodeHoaDon = '345678';
SELECT TOP 5 @HoaDon4 = Id FROM HoaDon WHERE CodeHoaDon = '901234';
SELECT TOP 5 @HoaDon5 = Id FROM HoaDon WHERE CodeHoaDon = '567890';

INSERT INTO HinhThucThanhToan (IdHoaDon, MaHinhThuc, TenHinhThuc, SoTien, TrangThai)
VALUES
    (@HoaDon1, N'MaHinhThuc1', N'Hình thức 1', 100000, 1),
    (@HoaDon2, N'MaHinhThuc2', N'Hình thức 2', 150000, 1),
    (@HoaDon3, N'MaHinhThuc3', N'Hình thức 3', 200000, 1),
    (@HoaDon4, N'MaHinhThuc4', N'Hình thức 4', 180000, 1),
    (@HoaDon5, N'MaHinhThuc5', N'Hình thức 5', 220000, 1);



	-- Bảng HoaDon
DECLARE @Voucher1 uniqueidentifier, @Voucher2 uniqueidentifier, @Voucher3 uniqueidentifier, @Voucher4 uniqueidentifier, @Voucher5 uniqueidentifier;
select * from Voucher
SELECT TOP 5 @Voucher1 = Id FROM Voucher WHERE Code = 'CODE004';
SELECT TOP 5 @Voucher2 = Id FROM Voucher WHERE Code = 'CODE003';
SELECT TOP 5 @Voucher3 = Id FROM Voucher WHERE Code = 'CODE005';
SELECT TOP 5 @Voucher4 = Id FROM Voucher WHERE Code = 'CODE002';
SELECT TOP 5 @Voucher5 = Id FROM Voucher WHERE Code = 'CODE001';

DECLARE @NguoiDung1 uniqueidentifier, @NguoiDung2 uniqueidentifier, @NguoiDung3 uniqueidentifier, @NguoiDung4 uniqueidentifier, @NguoiDung5 uniqueidentifier;

SELECT TOP 5 @NguoiDung1 = Id FROM NguoiDung WHERE TenNguoiDung = N'JohnDoe';
SELECT TOP 5 @NguoiDung2 = Id FROM NguoiDung WHERE TenNguoiDung = N'JaneSmith';
SELECT TOP 5 @NguoiDung3 = Id FROM NguoiDung WHERE TenNguoiDung = N'MichaelBrown';
SELECT TOP 5 @NguoiDung4 = Id FROM NguoiDung WHERE TenNguoiDung = N'SusanLee';
SELECT TOP 5 @NguoiDung5 = Id FROM NguoiDung WHERE TenNguoiDung = N'DavidWilson';

INSERT INTO HoaDon (IdVoucher, IdNguoiDung, CodeHoaDon, LoaiHoaDon, TenKhachHang, SoDienThoai, DiaChi, PhiShip, TienThua, TongTien, NgayTao, NgayChuyenKhoan, NgayShip, NgayMongMuonNhan, NgayNhan, TrangThai)
VALUES
    (@Voucher1, @NguoiDung1, '123456', 1, N'Khách hàng 1', '123456789', N'Địa chỉ 1', 10000, 50000, 60000, GETDATE(), GETDATE(), GETDATE(), GETDATE(), GETDATE(), 1),
    (@Voucher2, @NguoiDung2, '789012', 2, N'Khách hàng 2', '987654321', N'Địa chỉ 2', 15000, 20000, 35000, GETDATE(), GETDATE(), GETDATE(), GETDATE(), GETDATE(), 1),
    (@Voucher3, @NguoiDung3, '345678', 1, N'Khách hàng 3', '111222333', N'Địa chỉ 3', 8000, 12000, 20000, GETDATE(), GETDATE(), GETDATE(), GETDATE(), GETDATE(), 1),
    (@Voucher4, @NguoiDung4, '901234', 2, N'Khách hàng 4', '444555666', N'Địa chỉ 4', 12000, 18000, 30000, GETDATE(), GETDATE(), GETDATE(), GETDATE(), GETDATE(), 1),
    (@Voucher5, @NguoiDung5, '567890', 1, N'Khách hàng 5', '777888999', N'Địa chỉ 5', 15000, 25000, 40000, GETDATE(), GETDATE(), GETDATE(), GETDATE(), GETDATE(), 1);

	select * from HinhThucThanhToan
