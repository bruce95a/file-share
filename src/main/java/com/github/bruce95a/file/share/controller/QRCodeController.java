package com.github.bruce95a.file.share.controller;

import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.util.QRCodeUtil;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class QRCodeController {
    private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);

    @Autowired
    private ConfigService configService;

    @RequestMapping("/code")
    public StreamingResponseBody getQRCode(@RequestParam("id") String uuid,
                                           HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=qr-code.png");
        String address = configService.getSiteAddress();
        String url = String.format("%s/download?id=%s", address, uuid);
        return outputStream -> {
            try {
                MatrixToImageWriter.writeToStream(QRCodeUtil.getQRCode(url), "PNG", outputStream);
            } catch (WriterException e) {
                logger.error("=== QR ERR ===", e);
            } finally {
                outputStream.flush();
                outputStream.close();
            }
        };
    }
}
