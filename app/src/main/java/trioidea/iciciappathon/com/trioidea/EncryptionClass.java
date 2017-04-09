package trioidea.iciciappathon.com.trioidea;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by asus on 05/04/2017.
 * class file created for encryption algorithm
 */
public class EncryptionClass {
    //key of length 16 converted to BASE64 format
    private static String secretKey = "aWNpY2lhcHBhdGhvbjEyMw==";//XMzDdG4D03CKm2IxIWQw7g==

    public static String symmetricEncrypt(String text) {
        byte[] raw;
        String encryptedString;
        SecretKeySpec skeySpec;
        byte[] encryptText = text.getBytes();
        Cipher cipher;
        try {
            raw = Base64.decode(secretKey,Base64.DEFAULT);
            skeySpec = new SecretKeySpec(raw, "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encryptedString = Base64.encodeToString(cipher.doFinal(encryptText),Base64.DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return encryptedString;
    }

    public static String symmetricDecrypt(String text) {
        Cipher cipher;
        String encryptedString;
        byte[] encryptText = null;
        byte[] raw;
        SecretKeySpec skeySpec;
        try {
            raw = Base64.decode(secretKey,Base64.DEFAULT);
            skeySpec = new SecretKeySpec(raw, "AES");
            encryptText = Base64.decode(text,Base64.DEFAULT);
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            encryptedString = new String(cipher.doFinal(encryptText));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return encryptedString;
    }
}
