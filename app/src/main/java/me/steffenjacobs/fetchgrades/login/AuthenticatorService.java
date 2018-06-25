package me.steffenjacobs.fetchgrades.login;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class AuthenticatorService {
    private static final String KEY_NAME = "FetchGrades";
    private KeyStore keyStore;

    public AuthenticatorService(Context context) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            generateKeyIfNecessary(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateKeyIfNecessary(Context context) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(KEY_NAME)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 10);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(KEY_NAME)
                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
            Log.e(AuthenticatorService.class.getSimpleName(), Log.getStackTraceString(e));
        }
    }

    public String encryptString(String plaintext) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_NAME, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            System.out.println("Public key: " + publicKey.getEncoded());
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(plaintext.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            //Ignore
        }
        return "";
    }

    private RSAPrivateKey toPrivateKey(final PrivateKey privateKey){
        final RSAKey key = (RSAKey)privateKey;
       /* try {
            Class clazz = privateKey;
            Field f = clazz.getField("mModulus");
            f.setAccessible(true);
            Object mmod = f.get(privateKey);
            final BigInteger mModulo =  (BigInteger) mmod;

*/
        return new RSAPrivateKey() {
            @Override
            public BigInteger getPrivateExponent() {
                return null;
            }

            @Override
            public String getAlgorithm() {
                return privateKey.getAlgorithm();
            }

            @Override
            public String getFormat() {
                return privateKey.getFormat();
            }

            @Override
            public byte[] getEncoded() {
                return privateKey.getEncoded();
            }

            @Override
            public BigInteger getModulus() {
                return key.getModulus();
            }
        };
        /*} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    public String decryptString(String ciphertext) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_NAME, null);
            RSAPrivateKey privateKey = toPrivateKey(privateKeyEntry.getPrivateKey());

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(ciphertext, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            return new String(bytes, 0, bytes.length, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
