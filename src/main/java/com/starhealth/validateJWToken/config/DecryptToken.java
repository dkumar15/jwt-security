package com.starhealth.validateJWToken.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.starhealth.validateJWToken.entities.JwtTokenResponse;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.DecoderException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

@Component
public class DecryptToken {

    private final int keySize = 128;
    private final int iterationCount = 1000;
    private final Cipher cipher;


    public DecryptToken() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    public SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(),
                    hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.
                    generateSecret(spec).getEncoded(), "AES");
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Invalid Algorithm");
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        } catch (InvalidKeyException
                 | InvalidAlgorithmParameterException
                 | IllegalBlockSizeException
                 | BadPaddingException e) {
            System.out.println("Invalid Algorithm inside do final");
            throw fail(e);
        }
    }

    public String decrypt(String salt, String iv, String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    public static JwtTokenResponse convertJwtTokenToPojo(String token) {
        JwtTokenResponse jwtTokenResponse = null;
        try {
            if (token != null) {
                SignedJWT signedJwtObj = SignedJWT.parse(token);
                JSONObject jSONObject = (JSONObject) signedJwtObj.getPayload().toJSONObject();
                ObjectMapper mapper = new ObjectMapper();
                jwtTokenResponse = mapper.readValue((jSONObject.toJSONString()
                        .getBytes(StandardCharsets.UTF_8)), JwtTokenResponse.class);
            }
        } catch (ParseException | IOException e) {
            //log.error(e.getMessage(), e);
        }
        return jwtTokenResponse;
    }

    public static String base64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str.getBytes());
    }

    public static String hex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

}
