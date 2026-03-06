package com.joao.tibia_scrapper.service;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class TotpService {

    public String generateSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    public String getQrCodeImageUri(String secret, String label) throws QrGenerationException {
        String safeLabel = label.replaceAll("\\s+", "");

        QrData data = new QrData.Builder()
                .label(safeLabel)
                .secret(secret)
                .issuer("TibiaBuilder")
                .algorithm(dev.samstevens.totp.code.HashingAlgorithm.SHA1)
                .digits(6)
                .period(30) 
                .build();

        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] imageData = qrGenerator.generate(data);
        
        return getDataUriForImage(imageData, qrGenerator.getImageMimeType());
    }

    public boolean verifyCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return codeVerifier.isValidCode(secret, code);
    }
}