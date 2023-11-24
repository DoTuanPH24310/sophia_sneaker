package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {
    @Autowired
    ChiTietGiayRepository chiTietGiayRepository;
    public void generateAndSaveQRCodeImages(List<ChiTietGiay> chiTietGiayList, String filePath) {
        for (ChiTietGiay ctg : chiTietGiayList) {
            if (ctg.getQrCode() == null || ctg.getQrCode().isEmpty()) {
                // Nếu mã QR trống hoặc null, tiếp tục với chi tiết giày tiếp theo
                continue;
            }

            int width = 300; // Độ rộng ảnh QR
            int height = 300; // Độ cao ảnh QR

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(ctg.getQrCode(), BarcodeFormat.QR_CODE, width, height, hints);

                // Tạo ảnh từ BitMatrix
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }

                // Lưu ảnh vào file
                File file = new File(filePath);
                ImageIO.write(bufferedImage, "png", file);

                System.out.println("QR Code Image generated and saved at: " + filePath);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                // Xử lý ngoại lệ nếu có
            }
        }
    }

}
