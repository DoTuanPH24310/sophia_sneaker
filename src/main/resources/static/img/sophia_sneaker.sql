USE [Sophia_Sneaker]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Anh](
	[Id] [uniqueidentifier] NOT NULL,
	[ChiTietGiay] [uniqueidentifier] NULL,
	[duongDan] [nvarchar](50) NOT NULL,
	[anhChinh] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChiTietGiay](
	[Id] [uniqueidentifier] NOT NULL,
	[IdGiay] [uniqueidentifier] NULL,
	[IdKichCo] [uniqueidentifier] NULL,
	[IdDeGiay] [uniqueidentifier] NULL,
	[IdHang] [uniqueidentifier] NULL,
	[IdLoaiGiay] [uniqueidentifier] NULL,
	[IdMauSac] [uniqueidentifier] NULL,
	[ma] [nvarchar](50) NOT NULL,
	[gia] [money] NOT NULL,
	[soLuong] [int] NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DeGiay]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DeGiay](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
 CONSTRAINT [PK_DeGiay] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DiaChi]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DiaChi](
	[Id] [uniqueidentifier] NOT NULL,
	[IdTaiKhoan] [uniqueidentifier] NULL,
	[ten] [nvarchar](50) NULL,
	[sdt] [nvarchar](50) NULL,
	[diaChiCuThe] [nvarchar](50) NULL,
	[phuongXa] [nvarchar](50) NOT NULL,
	[quanHuyen] [nvarchar](50) NOT NULL,
	[tinh] [nvarchar](50) NOT NULL,
	[diaChiMacDinh] int NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Giay]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Giay](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GioHang]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GioHang](
	[Id] [uniqueidentifier] NOT NULL,
	[IdTaiKhoan] [uniqueidentifier] NULL,
	[ngayTao] [date] NOT NULL,
	[ngaySua] [date] NULL,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GioHangChiTiet]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GioHangChiTiet](
	[IdGioHang] [uniqueidentifier] NOT NULL,
	[IdChiTietGiay] [uniqueidentifier] NOT NULL,
	[soLuong] [int] NOT NULL,
	[ngayTao] [date] NOT NULL,
	[ngaySua] [date] NULL,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
 CONSTRAINT [PK_GioHangCT] PRIMARY KEY CLUSTERED 
(
	[IdGioHang] ASC,
	[IdChiTietGiay] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Hang]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Hang](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
 CONSTRAINT [PK_Hang] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HinhThucThanhToan]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HinhThucThanhToan](
	[Id] [uniqueidentifier] NOT NULL,
	[IdHoaDon] [uniqueidentifier] NULL,
	[ma] [nvarchar](50) NULL,
	[ten] [nvarchar](50) NULL,
	[soTien] [money] NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[Id] [uniqueidentifier] NOT NULL,
	[IdVoucher] [uniqueidentifier] NULL,
	[IdTaiKhoan] [uniqueidentifier] NULL,
	[maHoaDOn] [nvarchar](50) NOT NULL,
	[loaiHoaDon] [int] NULL,
	[tenKhachHang] [nvarchar](50) NULL,
	[soDienThoai] [nvarchar](50) NULL,
	[diaChi] [nvarchar](50) NULL,
	[phiShip] [money] NULL,
	[tienThua] [money] NULL,
	[tongTien] [money] NULL,
	[ngayTao] [date] NULL,
	[ngayChuyenKhoan] [date] NULL,
	[ngayShip] [date] NULL,
	[ngayMongMuonNhan] [date] NULL,
	[NgayNhan] [date] NULL,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDonChiTiet]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDonChiTiet](
	[Id] [uniqueidentifier] NOT NULL,
	[IdChiTietGiay] [uniqueidentifier] NULL,
	[IdHoaDon] [uniqueidentifier] NULL,
	[soLuong] [int] NULL,
	[donGia] [money] NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KichCo]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KichCo](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
 CONSTRAINT [PK_KichCo] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LichSuHoaDon]    Script Date: 10/2/2023 5:01:30 PM ******/

/****** Object:  Table [dbo].[LoaiGiay]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoaiGiay](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
 CONSTRAINT [PK_LoaiGiay] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MauSac]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MauSac](
	[Id] [uniqueidentifier] NOT NULL,
	ma nvarchar(50) not null,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
 CONSTRAINT [PK_MauSac] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TaiKhoan]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan](
	[Id] [uniqueidentifier] NOT NULL,
	[IdVaiTro] [uniqueidentifier] NULL,
	[taiKhoan] [nvarchar](50)  NULL,
	[ten] [nvarchar](50)  NULL,
	[email] [nvarchar](50)  NULL,
	[matKhau] [nvarchar](50)  NULL,
	[canCuoc] [nvarchar](50)  NULL,
	[ngaySinh] [date]  NULL,
	[anhDaiDien] [nvarchar](50)  NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TaiKhoan_Voucher]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TaiKhoan_Voucher](
	[IdVoucher] [uniqueidentifier] NOT NULL,
	[IdTaiKhoan] [uniqueidentifier] NOT NULL,
	[trangThai] [int] NULL,
 CONSTRAINT [PK_TaiKhoan_Voucher] PRIMARY KEY CLUSTERED 
(
	[IdVoucher] ASC,
	[IdTaiKhoan] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ThongBao]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ThongBao](
	[Id] [uniqueidentifier] NOT NULL,
	[IdTaiKhoan] [uniqueidentifier] NULL,
	[tieuDe] [nvarchar](50) NOT NULL,
	[kieu] [nvarchar](50) NULL,
	[noiDung] [nvarchar](50) NOT NULL,
	[hanhDong] [nvarchar](50) NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[VaiTro]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[VaiTro](
	[Id] [uniqueidentifier] NOT NULL,
	[ten] [nvarchar](50) NOT NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	[trangThai] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Voucher]    Script Date: 10/2/2023 5:01:30 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Voucher](
	[Id] [uniqueidentifier] NOT NULL,
	[ma] [nvarchar](50) NOT NULL,
	[ten] [nvarchar](50) NULL,
	[soLuong] [int] NULL,
	[phanTramGiam] [float] NULL,
	[soTienGiam] [money] NULL,
	[giaTriToiThieu] [money] NULL,
	[ngayBatDau] [date] NULL,
	[ngayKetThuc] [date] NULL,
	ngayTao date,
	ngaySua date,
	nguoiTao [uniqueidentifier],
	nguoiSua [uniqueidentifier],
	trangThai int
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Anh] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[ChiTietGiay] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[DeGiay] ADD  CONSTRAINT [DF_DeGiay_Id]  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[DiaChi] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[Giay] ADD  CONSTRAINT [DF_Giay_Id]  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[GioHang] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[Hang] ADD  CONSTRAINT [DF_Hang_Id]  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[HinhThucThanhToan] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[HoaDonChiTiet] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[KichCo] ADD  CONSTRAINT [DF_KichCo_Id]  DEFAULT (newid()) FOR [Id]
GO

ALTER TABLE [dbo].[LoaiGiay] ADD  CONSTRAINT [DF_LoaiGiay_Id]  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[MauSac] ADD  CONSTRAINT [DF_MauSac_Id]  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[TaiKhoan] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[ThongBao] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[VaiTro] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[Voucher] ADD  DEFAULT (newid()) FOR [Id]
GO
ALTER TABLE [dbo].[Anh]  WITH CHECK ADD  CONSTRAINT [FK7] FOREIGN KEY([ChiTietGiay])
REFERENCES [dbo].[ChiTietGiay] ([Id])
GO
ALTER TABLE [dbo].[Anh] CHECK CONSTRAINT [FK7]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK1] FOREIGN KEY([IdGiay])
REFERENCES [dbo].[Giay] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK1]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK2] FOREIGN KEY([IdKichCo])
REFERENCES [dbo].[KichCo] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK2]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK3] FOREIGN KEY([IdDeGiay])
REFERENCES [dbo].[DeGiay] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK3]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK4] FOREIGN KEY([IdHang])
REFERENCES [dbo].[Hang] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK4]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK5] FOREIGN KEY([IdLoaiGiay])
REFERENCES [dbo].[LoaiGiay] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK5]
GO
ALTER TABLE [dbo].[ChiTietGiay]  WITH CHECK ADD  CONSTRAINT [FK6] FOREIGN KEY([IdMauSac])
REFERENCES [dbo].[MauSac] ([Id])
GO
ALTER TABLE [dbo].[ChiTietGiay] CHECK CONSTRAINT [FK6]
GO
ALTER TABLE [dbo].[DiaChi]  WITH CHECK ADD  CONSTRAINT [FK10] FOREIGN KEY([IdTaiKhoan])
REFERENCES [dbo].[TaiKhoan] ([Id])
GO
ALTER TABLE [dbo].[DiaChi] CHECK CONSTRAINT [FK10]
GO
ALTER TABLE [dbo].[GioHang]  WITH CHECK ADD  CONSTRAINT [FK9] FOREIGN KEY([IdTaiKhoan])
REFERENCES [dbo].[TaiKhoan] ([Id])
GO
ALTER TABLE [dbo].[GioHang] CHECK CONSTRAINT [FK9]
GO
ALTER TABLE [dbo].[GioHangChiTiet]  WITH CHECK ADD  CONSTRAINT [FK1_IdGioHang] FOREIGN KEY([IdGioHang])
REFERENCES [dbo].[GioHang] ([Id])
GO
ALTER TABLE [dbo].[GioHangChiTiet] CHECK CONSTRAINT [FK1_IdGioHang]
GO
ALTER TABLE [dbo].[GioHangChiTiet]  WITH CHECK ADD  CONSTRAINT [FK2_IdChiTietGiay] FOREIGN KEY([IdChiTietGiay])
REFERENCES [dbo].[ChiTietGiay] ([Id])
GO
ALTER TABLE [dbo].[GioHangChiTiet] CHECK CONSTRAINT [FK2_IdChiTietGiay]
GO
ALTER TABLE [dbo].[HinhThucThanhToan]  WITH CHECK ADD  CONSTRAINT [FK2_IdHoaDon3] FOREIGN KEY([IdHoaDon])
REFERENCES [dbo].[HoaDon] ([Id])
GO
ALTER TABLE [dbo].[HinhThucThanhToan] CHECK CONSTRAINT [FK2_IdHoaDon3]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK1_IdVoucher1] FOREIGN KEY([IdVoucher])
REFERENCES [dbo].[Voucher] ([Id])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK1_IdVoucher1]
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD  CONSTRAINT [FK2_IdTaiKhoan1] FOREIGN KEY([IdTaiKhoan])
REFERENCES [dbo].[TaiKhoan] ([Id])
GO
ALTER TABLE [dbo].[HoaDon] CHECK CONSTRAINT [FK2_IdTaiKhoan1]
GO
ALTER TABLE [dbo].[HoaDonChiTiet]  WITH CHECK ADD  CONSTRAINT [FK1_IdChiTietGiay1] FOREIGN KEY([IdChiTietGiay])
REFERENCES [dbo].[ChiTietGiay] ([Id])
GO
ALTER TABLE [dbo].[HoaDonChiTiet] CHECK CONSTRAINT [FK1_IdChiTietGiay1]
GO
ALTER TABLE [dbo].[HoaDonChiTiet]  WITH CHECK ADD  CONSTRAINT [FK2_IdHoaDon1] FOREIGN KEY([IdHoaDon])
REFERENCES [dbo].[HoaDon] ([Id])
GO
ALTER TABLE [dbo].[HoaDonChiTiet] CHECK CONSTRAINT [FK2_IdHoaDon1]
GO




ALTER TABLE [dbo].[TaiKhoan]  WITH CHECK ADD  CONSTRAINT [FK8] FOREIGN KEY([IdVaiTro])
REFERENCES [dbo].[VaiTro] ([Id])
GO
ALTER TABLE [dbo].[TaiKhoan] CHECK CONSTRAINT [FK8]
GO
ALTER TABLE [dbo].[TaiKhoan_Voucher]  WITH CHECK ADD  CONSTRAINT [FK1_IdVoucher] FOREIGN KEY([IdVoucher])
REFERENCES [dbo].[Voucher] ([Id])
GO
ALTER TABLE [dbo].[TaiKhoan_Voucher] CHECK CONSTRAINT [FK1_IdVoucher]
GO
ALTER TABLE [dbo].[TaiKhoan_Voucher]  WITH CHECK ADD  CONSTRAINT [FK2_IdTaiKhoan] FOREIGN KEY([IdTaiKhoan])
REFERENCES [dbo].[TaiKhoan] ([Id])
GO
ALTER TABLE [dbo].[TaiKhoan_Voucher] CHECK CONSTRAINT [FK2_IdTaiKhoan]
GO
ALTER TABLE [dbo].[ThongBao]  WITH CHECK ADD  CONSTRAINT [FK11] FOREIGN KEY([IdTaiKhoan])
REFERENCES [dbo].[TaiKhoan] ([Id])
GO
ALTER TABLE [dbo].[ThongBao] CHECK CONSTRAINT [FK11]
GO



--Anh

-- Chèn bản ghi thứ 1
INSERT INTO [dbo].[Anh] ([Id], [duongDan], [anhChinh], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), 'duongdan1.jpg', 'anh1.jpg', '2023-10-05', '2023-10-05', 'nguoitao1', 'nguoisua1', 1);

-- Chèn bản ghi thứ 2
INSERT INTO [dbo].[Anh] ([Id], [duongDan], [anhChinh], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), 'duongdan2.jpg', 'anh2.jpg', '2023-10-05', '2023-10-05', 'nguoitao2', 'nguoisua2', 1);

-- Chèn bản ghi thứ 3
INSERT INTO [dbo].[Anh] ([Id], [duongDan], [anhChinh], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), 'duongdan3.jpg', 'anh3.jpg', '2023-10-05', '2023-10-05', 'nguoitao3', 'nguoisua3', 1);

--Hang

INSERT INTO Hang (Id, ma, ten, ngayTao, ngaySua, nguoiTao, nguoiSua, trangThai)
VALUES
    (NEWID(), 'H001', 'Nike', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    (NEWID(), 'H002', 'Adidas', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    (NEWID(), 'H003', 'Puma', GETDATE(), GETDATE(), NEWID(), NEWID(), 1);

--LoaiGiay:

INSERT INTO LoaiGiay (Id, ma, ten, ngayTao, ngaySua, nguoiTao, nguoiSua, trangThai)
VALUES
    (NEWID(), 'LG001', 'Sneaker', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    (NEWID(), 'LG002', 'Sandal', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    (NEWID(), 'LG003', 'Boots', GETDATE(), GETDATE(), NEWID(), NEWID(), 1);

--MauSac
	INSERT INTO [dbo].[MauSac] ([ma], [ten], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES 
    ('M001', 'Đen', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    ('M002', 'Trắng', GETDATE(), GETDATE(), NEWID(), NEWID(), 1),
    ('M003', 'Xám', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)


INSERT INTO [dbo].[KichCo] ([ma], [ten], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES ('K001', '40', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng DeGiay
INSERT INTO [dbo].[DeGiay] ([ma], [ten], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES ('DG001', 'Cao cổ', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng Giay
INSERT INTO [dbo].[Giay] ([ma], [ten], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES ('G001', 'Nike Air Max 90', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng ChiTietGiay
INSERT INTO [dbo].[ChiTietGiay] ([IdGiay], [IdKichCo], [IdDeGiay], [IdHang], [IdLoaiGiay], [IdMauSac], [ma], [gia], [soLuong], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), NEWID(), NEWID(), NEWID(), NEWID(), NEWID(), 'CG001', 1000000, 10, GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng Voucher
INSERT INTO [dbo].[Voucher] ([ma], [ten], [soLuong], [phanTramGiam], [soTienGiam], [giaTriToiThieu], [ngayBatDau], [ngayKetThuc], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES ('V001', 'Voucher 1', 100, 10.0, 0, 500000, '2023-10-05', '2023-10-15', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng TaiKhoan
INSERT INTO [dbo].[TaiKhoan] ([IdVaiTro], [taiKhoan], [ten], [email], [matKhau], [canCuoc], [ngaySinh], [anhDaiDien], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), 'user1', 'User 1', 'user1@example.com', 'hashedpassword1', '1234567890', '1990-01-01', 'avatar1.jpg', GETDATE(), GETDATE(), NEWID(), NEWID(), 1)

-- Insert dữ liệu cho bảng DiaChi
INSERT INTO [dbo].[DiaChi] ([IdTaiKhoan], [ten], [sdt], [diaChiCuThe], [phuongXa], [quanHuyen], [tinh], [diaChiMacDinh], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES (NEWID(), 'Địa chỉ 1', '1234567890', '123 Đường ABC', 'Phường XYZ', 'Quận ABC', 'Tỉnh XYZ', 1, GETDATE(), GETDATE(), NEWID(), NEWID(), 1)


-- Hóa đơn:

INSERT INTO [dbo].[HoaDon] ([Id], [IdVoucher], [IdTaiKhoan], [maHoaDOn], [loaiHoaDon], [tenKhachHang], [soDienThoai], [diaChi], [phiShip], [tienThua], [tongTien], [ngayTao], [ngayChuyenKhoan], [ngayShip], [ngayMongMuonNhan], [NgayNhan], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES
    (NEWID(), NULL, NEWID(), 'HD001', 1, 'Khách hàng 1', '0123456789', 'Địa chỉ giao hàng 1', 20000.0, 0.0, 50000.0, '2023-10-05', NULL, '2023-10-07', '2023-10-10', '2023-10-11', NULL, NULL, 1),
    (NEWID(), NULL, NEWID(), 'HD002', 2, 'Khách hàng 2', '0987654321', 'Địa chỉ giao hàng 2', 15000.0, 0.0, 60000.0, '2023-10-06', NULL, '2023-10-08', '2023-10-09', '2023-10-12', NULL, NULL, 1),
    (NEWID(), NULL, NEWID(), 'HD003', 1, 'Khách hàng 3', '0369845271', 'Địa chỉ giao hàng 3', 10000.0, 0.0, 70000.0, '2023-10-07', NULL, '2023-10-09', '2023-10-11', '2023-10-13', NULL, NULL, 1);


-- Chèn 3 bản ghi vào bảng GioHang
INSERT INTO [dbo].[GioHang] ([Id], [IdTaiKhoan], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua], [trangThai])
VALUES
    (NEWID(), NEWID(), '2023-10-05', NULL, NULL, NULL, 1),
    (NEWID(), NEWID(), '2023-10-06', NULL, NULL, NULL, 1),
    (NEWID(), NEWID(), '2023-10-07', NULL, NULL, NULL, 1);

-- Chèn 3 bản ghi vào bảng GioHangChiTiet
INSERT INTO [dbo].[GioHangChiTiet] ([IdGioHang], [IdChiTietGiay], [soLuong], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    ((SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id]), NEWID(), 2, '2023-10-05', NULL, NULL, NULL),
    ((SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id]), NEWID(), 1, '2023-10-06', NULL, NULL, NULL),
    ((SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id]), NEWID(), 3, '2023-10-07', NULL, NULL, NULL);


-- Chèn 3 bản ghi vào bảng HoaDonChiTiet
INSERT INTO [dbo].[HoaDonChiTiet] ([IdChiTietGiay], [IdHoaDon], [soLuong], [donGia], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    ((SELECT TOP 1 [IdChiTietGiay] FROM [dbo].[GioHangChiTiet] WHERE [IdGioHang] = (SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id])), (SELECT TOP 1 [Id] FROM [dbo].[HoaDon] ORDER BY [Id]), 2, 25000.0, '2023-10-10', NULL, NULL, NULL),
    ((SELECT TOP 2 [IdChiTietGiay] FROM [dbo].[GioHangChiTiet] WHERE [IdGioHang] = (SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id])), (SELECT TOP 2 [Id] FROM [dbo].[HoaDon] ORDER BY [Id]), 1, 30000.0, '2023-10-11', NULL, NULL, NULL),
    ((SELECT TOP 3 [IdChiTietGiay] FROM [dbo].[GioHangChiTiet] WHERE [IdGioHang] = (SELECT TOP 1 [Id] FROM [dbo].[GioHang] ORDER BY [Id])), (SELECT TOP 3 [Id] FROM [dbo].[HoaDon] ORDER BY [Id]), 3, 20000.0, '2023-10-12', NULL, NULL, NULL);


-- Chèn 3 bản ghi vào bảng ThongBao
INSERT INTO [dbo].[ThongBao] ([Id], [tieuDe], [noiDung], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    (NEWID(), 'Thông báo 1', 'Nội dung thông báo 1', '2023-10-05', NULL, NULL, NULL),
    (NEWID(), 'Thông báo 2', 'Nội dung thông báo 2', '2023-10-06', NULL, NULL, NULL),
    (NEWID(), 'Thông báo 3', 'Nội dung thông báo 3', '2023-10-07', NULL, NULL, NULL);


-- Chèn 3 bản ghi vào bảng TaiKhoan
INSERT INTO [dbo].[TaiKhoan] ([Id], [tenDangNhap], [matKhau], [tenHienThi], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    (NEWID(), 'user1', 'password1', 'Người dùng 1', '2023-10-05', NULL, NULL, NULL),
    (NEWID(), 'user2', 'password2', 'Người dùng 2', '2023-10-06', NULL, NULL, NULL),
    (NEWID(), 'user3', 'password3', 'Người dùng 3', '2023-10-07', NULL, NULL, NULL);


-- Chèn 3 bản ghi vào bảng VouCher
INSERT INTO [dbo].[VouCher] ([Id], [maVouCher], [soLuong], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    (NEWID(), 'VC001', 10, '2023-10-05', NULL, NULL, NULL),
    (NEWID(), 'VC002', 15, '2023-10-06', NULL, NULL, NULL),
    (NEWID(), 'VC003', 20, '2023-10-07', NULL, NULL, NULL);


-- Chèn 3 bản ghi vào bảng TaiKhoanVouCher
INSERT INTO [dbo].[TaiKhoanVouCher] ([IdTaiKhoan], [IdVouCher], [soLuong], [ngayTao], [ngaySua], [nguoiTao], [nguoiSua])
VALUES
    ((SELECT TOP 1 [Id] FROM [dbo].[TaiKhoan] ORDER BY [Id]), (SELECT TOP 1 [Id] FROM [dbo].[VouCher] ORDER BY [Id]), 5, '2023-10-05', NULL, NULL, NULL),
    ((SELECT TOP 2 [Id] FROM [dbo].[TaiKhoan] ORDER BY [Id]), (SELECT TOP 2 [Id] FROM [dbo].[VouCher] ORDER BY [Id]), 8, '2023-10-06', NULL, NULL, NULL),
    ((SELECT TOP 3 [Id] FROM [dbo].[TaiKhoan] ORDER BY [Id]), (SELECT TOP 3 [Id] FROM [dbo].[VouCher] ORDER BY [Id]), 10, '2023-10-07', NULL, NULL, NULL);
















