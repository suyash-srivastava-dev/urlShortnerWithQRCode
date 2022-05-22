package com.springboot.redis.tinyurl.model;

//public class GenerateQRCode {
//}
import java.io.File;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;
        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.EncodeHintType;
        import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
        import com.google.zxing.client.j2se.MatrixToImageWriter;
        import com.google.zxing.common.BitMatrix;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateQRCode
{
    //static function that creates QR Code
    @Autowired
    public static GenerateQRCode generateQRcode(String data) throws WriterException, IOException
    {
//the BitMatrix class represents the 2D matrix of bits
        int h=200, w=200;
        String path = "Quote.png";
//Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
        return null;
    }
    //main() method
//    public static void main(String args[]) throws WriterException, IOException, NotFoundException
//    {
////data that we want to store in the QR code
////        String str= "THE HABIT OF PERSISTENCE IS THE HABIT OF VICTORY.";
////path where we want to get QR Code
//
//
////invoking the user-defined method that creates the QR code
////        generateQRcode(str, hashMap, 200, 200);//increase or decrease height and width accodingly
////prints if the QR code is generated
//        System.out.println("QR Code created successfully.");
//    }
}