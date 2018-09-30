package jp.dip.hmy2001.mcbeClient.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class NetworkCipher {
    private byte[] sharedKey;
    private Cipher decryptCipher;
    private int decryptCounter = 0;
    private Cipher encryptCipher;
    private int encryptCounter = 0;

    public NetworkCipher(byte[] sharedKey){
        this.sharedKey = sharedKey;

        byte[] iv = new byte[16];
        System.arraycopy(sharedKey, 0, iv, 0, 16);

        SecretKeySpec secretKey = new SecretKeySpec(sharedKey, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        //System.out.println(DatatypeConverter.printHexBinary(sharedKey));
        //System.out.println(DatatypeConverter.printHexBinary(iv));

        try{
            decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] decrypt(byte[] encrypted){
        try {
            //System.out.println("decrypt");

            byte[] decrypted = decryptCipher.update(encrypted);
            byte[] payload = new byte[decrypted.length - 8];
            byte[] checkSum = new byte[8];
            System.arraycopy(decrypted, 0, payload, 0, decrypted.length - 8);
            System.arraycopy(decrypted, decrypted.length - 8, checkSum, 0, 8);

            byte[] calculateCheckSum = calculateChecksum(decryptCounter++, payload);

            //System.out.println("calculateCheckSum: " + DatatypeConverter.printHexBinary(calculateCheckSum));
            //System.out.println("decrypted checksum: " + DatatypeConverter.printHexBinary(checkSum));

            if(!Arrays.equals(calculateCheckSum, checkSum)){
                CommandReader.getInstance().stashLine();
                System.out.println("Can not decrypt!");

                System.out.println("calculateCheckSum: " + DatatypeConverter.printHexBinary(calculateCheckSum));
                System.out.println("decrypted checksum: " + DatatypeConverter.printHexBinary(checkSum));
                CommandReader.getInstance().unstashLine();

                return new byte[0];
            }

            //System.out.println(DatatypeConverter.printHexBinary(decrypted));
            //System.out.println(DatatypeConverter.printHexBinary(payload));

            return payload;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new byte[0];
    }

    public byte[] encrypt(byte[] payload){
        try {
            //System.out.println("encrypt");

            BinaryStream binaryStream = new BinaryStream();
            binaryStream.write(payload);
            binaryStream.write(calculateChecksum(encryptCounter++, payload));
            byte[] encryptPayload = binaryStream.array();

            byte[] encrypted = encryptCipher.update(encryptPayload);

            //System.out.println("encryptPayload: " + DatatypeConverter.printHexBinary(encryptPayload));
            //System.out.println("encrypted: " + DatatypeConverter.printHexBinary(encrypted));

            return encrypted;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new byte[0];
    }

    private byte[] calculateChecksum(int counter, byte[] payload){
        try {
            BinaryStream binaryStream = new BinaryStream();
            binaryStream.writeLongLE(counter);
            binaryStream.write(payload);
            binaryStream.write(sharedKey);

            //System.out.println(counter);
            //System.out.println(DatatypeConverter.printHexBinary(binaryStream.array()));

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(binaryStream.array());
            byte[] result = messageDigest.digest();
            byte[] checkSum = new byte[8];
            System.arraycopy(result, 0, checkSum, 0, 8);

            //System.out.println("encrypt: " + DatatypeConverter.printHexBinary(result));
            //System.out.println("checksum: " + DatatypeConverter.printHexBinary(checkSum));

            return checkSum;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new byte[0];
    }


}
