package jp.dip.hmy2001.mcbeClient;

import com.google.gson.Gson;
import jp.dip.hmy2001.mcbeClient.network.mcbe.json.JwtHeader;
import jp.dip.hmy2001.mcbeClient.utils.NetworkCipher;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.security.*;
import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MCBEClient {
    private String username;
    private String clientUUID;
    private SecureRandom secureRandom;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private InetAddress serverAddress;
    private int serverPort;
    private ClientSession clientSession;
    private NetworkCipher networkCipher;

    public MCBEClient(String serverAddress, int serverPort, String username, String clientUUID){
        this.username = username;
        this.clientUUID = clientUUID;
        generatePrivateKey();

        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
            this.serverPort = serverPort;

            System.out.println("Client connect to " + serverAddress + ":" + this.serverPort);

            createClientSession();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createClientSession(){
        clientSession = new ClientSession(this, this.serverAddress, this.serverPort, username, clientUUID);
        clientSession.start();
    }

    private void generatePrivateKey(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            secureRandom = SecureRandom.getInstance("NativePRNG");

            keyPairGenerator.initialize(384, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PrivateKey getPrivateKey(){
        return privateKey;
    }

    public String getEncodedPrivateKey(){
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }

    public String getEncodedPublicKey(){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public void generateAESKey(String encodedServerPublicKey, String encodedServerToken){//TODO: Move to Chiper
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(encodedServerPublicKey));
            PublicKey serverPublicKey = KeyFactory.getInstance("EC").generatePublic(x509EncodedKeySpec);
            byte[] serverToken = Base64.getDecoder().decode(encodedServerToken);

            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(serverPublicKey, true);
            byte[] sharedSecret = keyAgreement.generateSecret();

            byte[] temporalData = new byte[serverToken.length + sharedSecret.length];//server token + sharedSecret
            System.arraycopy(serverToken, 0, temporalData, 0, serverToken.length);
            System.arraycopy(sharedSecret, 0, temporalData, serverToken.length, sharedSecret.length);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(temporalData);
            byte[] sharedKey = messageDigest.digest();

            //System.out.println("serverToken: " + DatatypeConverter.printHexBinary(serverToken));
            //System.out.println("aesKey: " + DatatypeConverter.printHexBinary(sharedKey));

            networkCipher = new NetworkCipher(sharedKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public NetworkCipher getNetworkCipher() {
        return networkCipher;
    }

    private String createJwtHeader(){
        JwtHeader jwtHeader = new JwtHeader("ES384", getEncodedPublicKey());

        Gson gson = new Gson();
        String json = gson.toJson(jwtHeader, JwtHeader.class);
        //System.out.println(json);
        return Base64.getUrlEncoder().encodeToString(json.getBytes());
    }

    public String createJwt(String jwt){
        String jwtBody = createJwtHeader() + "." + jwt;
        String signature = signature(jwtBody.getBytes());

        return jwtBody + "." + signature;
    }

    public String signature(byte[] signatureData){
        try{
            Signature dsa = Signature.getInstance("SHA384withECDSA");
            dsa.initSign(privateKey, secureRandom);
            dsa.update(signatureData);

            byte[] derSignature = dsa.sign();

            //System.out.println("derSignature");
            //System.out.println(DatatypeConverter.printHexBinary(derSignature));

            DataInputStream derSignatureBuffer = new DataInputStream(new ByteArrayInputStream(derSignature));
            derSignatureBuffer.readByte();//0x30
            derSignatureBuffer.readByte();//len
            derSignatureBuffer.readByte();//0x20

            int leftLen = new Byte(derSignatureBuffer.readByte()).intValue();
            byte[] leftData = new byte[leftLen];
            int reallyLeftLen = derSignatureBuffer.read(leftData, 0, leftLen);

            derSignatureBuffer.readByte();//0x20

            int rightLen = new Byte(derSignatureBuffer.readByte()).intValue();
            byte[] rightData = new byte[rightLen];
            int reallyRightLen = derSignatureBuffer.read(rightData, 0, rightLen);

            int leftDataOffset = 0;
            if(leftData[0] == 0x00 && leftData.length > 48){
                leftLen -= 1;
                leftDataOffset = 1;
            }

            int rightDataOffset = 0;
            if(rightData[0] == 0x00 && rightData.length > 48){
                rightLen -= 1;
                rightDataOffset = 1;
            }

            byte[] rawSignature = new byte[96];
            System.arraycopy(leftData, leftDataOffset, rawSignature, 0, leftLen);
            System.arraycopy(rightData, rightDataOffset, rawSignature, leftLen, rightLen);

            //System.out.println(DatatypeConverter.printHexBinary(rawSignature));
            return Base64.getUrlEncoder().encodeToString(rawSignature);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public void shutdown(){
        clientSession.shutdown();
    }

}
