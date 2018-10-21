package jp.dip.hmy2001.mcbeClient;

import com.google.gson.Gson;
import com.whirvis.jraknet.RakNetPacket;
import com.whirvis.jraknet.client.RakNetClientListener;
import com.whirvis.jraknet.protocol.Reliability;
import com.whirvis.jraknet.session.RakNetServerSession;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;
import jp.dip.hmy2001.mcbeClient.network.mcbe.json.*;
import jp.dip.hmy2001.mcbeClient.network.mcbe.protocol.*;
import jp.dip.hmy2001.mcbeClient.utils.*;

import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.zip.Deflater;

public class ClientListener implements RakNetClientListener{
    private MCBEClient client;
    private ClientSession clientSession;
    private String username;
    private String clientUUID;
    private boolean isEncryption = false;
    private int entityUniqueId;
    private int entityRuntimeId;

    public ClientListener(MCBEClient client, ClientSession clientSession, String username, String clientUUID){
        this.client = client;
        this.clientSession = clientSession;
        this.username = username;
        this.clientUUID = clientUUID;

        CommandReader.getInstance().stashLine();
        System.out.println("displayName: " + username);
        CommandReader.getInstance().unstashLine();
    }

    @Override
    public void onConnect(RakNetServerSession session) {
        CommandReader.getInstance().stashLine();
        System.out.println("Connected.");
        CommandReader.getInstance().unstashLine();

        LoginPacket loginPacket = new LoginPacket();
        loginPacket.protocol = 291;

        HugeChainData hugeChainData = new HugeChainData();
        hugeChainData.chain = new String[]{client.createJwt(createChainData())};
        Gson gson = new Gson();
        String json = gson.toJson(hugeChainData, HugeChainData.class);
        //System.out.println(json);

        loginPacket.chainData = json.getBytes();
        loginPacket.chainDataLength = loginPacket.chainData.length;
        loginPacket.clientData = client.createJwt(createClientData()).getBytes();
        loginPacket.clientDataLength = loginPacket.clientData.length;
        loginPacket.bodyLength = loginPacket.chainDataLength + loginPacket.clientDataLength + 8;//8 is LInt length.

        sendBatchPacket(session, loginPacket);
    }

    @Override
    public void onDisconnect(RakNetServerSession session, String reason) {
        CommandReader.getInstance().stashLine();
        System.out.println("Disconnect.");
        CommandReader.getInstance().unstashLine();
        //TODO: Finish software
    }

    @Override
    public void handleMessage(RakNetServerSession session, RakNetPacket packet, int channel) {
        if(packet.array()[0] == ProtocolInfo.BATCH_PACKET){
            BatchPacket batchPacket = new BatchPacket();

            if(isEncryption){
                byte[] temporalData = new byte[packet.array().length - 1];
                System.arraycopy(packet.array(), 1, temporalData, 0, packet.array().length - 1);

                //System.out.println("receive encrypt: " + DatatypeConverter.printHexBinary(temporalData));
                //System.out.println("receive: " + DatatypeConverter.printHexBinary(packet.array()));

                byte[] payload = client.getNetworkCipher().decrypt(temporalData);
                byte[] decryptPayload = new byte[payload.length + 1];
                decryptPayload[0] = (byte) 0xfe;
                System.arraycopy(payload, 0, decryptPayload, 1, payload.length);

               // System.out.println("decryptPayload: " + DatatypeConverter.printHexBinary(decryptPayload));

                batchPacket.setBuffer(decryptPayload);
            }else{
                batchPacket.setBuffer(packet.array());
            }
            batchPacket.decode();

            BinaryStream binaryStream = new BinaryStream();
            binaryStream.setBuffer(batchPacket.payload);

            while (binaryStream.remaining() > 0){
                int pkLen = binaryStream.readUnsignedVarInt();
                byte[] pk = binaryStream.read(pkLen);

                CommandReader.getInstance().stashLine();
                //System.out.println("packetId: " + pk[0]);

                switch (pk[0]){
                    case ProtocolInfo.PLAY_STATUS_PACKET:{
                        PlayStatusPacket receivePk = new PlayStatusPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();

                        if(receivePk.status == 3){//Player Spawn
                            SetLocalPlayerAsInitializedPacket sendPk = new SetLocalPlayerAsInitializedPacket();
                            sendPk.entityRuntimeId = this.entityRuntimeId;
                            sendBatchPacket(session, sendPk);
                        }
                    break;}
                    case ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET:{
                        ServerToClientHandshakePacket receivePk = new ServerToClientHandshakePacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();

                        String[] jwtData = receivePk.jwt.split(Pattern.quote("."));

                        Gson gson = new Gson();
                        JwtHeader jwtHeader = gson.fromJson(new String(Base64.getUrlDecoder().decode(jwtData[0])), JwtHeader.class);
                        SaltData saltData = gson.fromJson(new String(Base64.getUrlDecoder().decode(jwtData[1])), SaltData.class);

                        client.generateAESKey(jwtHeader.x5u, saltData.salt);
                        isEncryption = true;

                        System.out.println("Enable Encryption!!!");

                        ClientToServerHandshakePacket sendPk = new ClientToServerHandshakePacket();
                        sendBatchPacket(session, sendPk);
                    break;}
                    case ProtocolInfo.DISCONNECT_PACKET:{
                        DisconnectPacket receivePk = new DisconnectPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();

                        if(!receivePk.hideDisconnectionScreen){
                            System.out.println("DisconnectMessage: " + receivePk.message);
                        }
                    break;}
                    case ProtocolInfo.RESOURCE_PACKS_INFO_PACKET:{
                        ResourcePackClientResponsePacket sendPk = new ResourcePackClientResponsePacket();
                        sendPk.status = 3;

                        sendBatchPacket(session, sendPk);
                    break;}
                    case ProtocolInfo.RESOURCE_PACK_STACK_PACKET:{
                        ResourcePackClientResponsePacket sendPk = new ResourcePackClientResponsePacket();
                        sendPk.status = 4;

                        sendBatchPacket(session, sendPk);
                    break;}
                    case ProtocolInfo.MOB_EQUIPMENT_PACKET:{
                        MobEquipmentPacket receivePk = new MobEquipmentPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();
                    break;}
                    case ProtocolInfo.START_GAME_PACKET:{
                        StartGamePacket receivePk = new StartGamePacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();

                        entityUniqueId = receivePk.entityUniqueId;
                        entityRuntimeId = receivePk.entityRuntimeId;

                        System.out.println("clientUniqueId: " + entityUniqueId);
                        System.out.println("clientRuntimeId: " + entityRuntimeId);

                        RequestChunkRadiusPacket sendPk = new RequestChunkRadiusPacket();
                        sendPk.radius = 8;
                        sendBatchPacket(session, sendPk);
                    break;}
                    case ProtocolInfo.INVENTORY_CONTENT_PACKET:{
                        InventoryContentPacket receivePk = new InventoryContentPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();
                    break;}
                    case ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET:{
                        ClientboundMapItemDataPacket receivePk = new ClientboundMapItemDataPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();
                    break;}
                    case ProtocolInfo.FULL_CHUNK_DATA_PACKET:{
                        FullChunkDataPacket receivePk = new FullChunkDataPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();
                    break;}
                    case ProtocolInfo.BOSS_EVENT_PACKET:{
                        BossEventPacket receivePk = new BossEventPacket();
                        receivePk.setBuffer(pk);
                        receivePk.decode();
                    break;}
                }

                CommandReader.getInstance().unstashLine();
            }
        }
    }

    public void sendBatchPacket(RakNetServerSession session, GamePacket gamePacket){
        gamePacket.encode();

        BinaryStream binaryStream = new BinaryStream();
        binaryStream.writeUnsignedVarInt(gamePacket.array().length);
        binaryStream.write(gamePacket.array());

        BatchPacket batchPacket = new BatchPacket();
        if(isEncryption){
            batchPacket.isEncryption = true;

            byte[] output = new byte[100000];
            Deflater compresser = new Deflater(Deflater.DEFLATED);
            compresser.setInput(binaryStream.array());
            compresser.finish();
            int length = compresser.deflate(output);
            compresser.end();

            //System.out.println(DatatypeConverter.printHexBinary(Arrays.copyOf(output, length)));

            batchPacket.payload = client.getNetworkCipher().encrypt(Arrays.copyOf(output, length));
        }else{
            batchPacket.payload = binaryStream.array();
        }
        batchPacket.encode();

        //System.out.println("batch: " + DatatypeConverter.printHexBinary(batchPacket.array()));

        session.sendMessage(Reliability.UNRELIABLE, batchPacket);

        CommandReader.getInstance().stashLine();
        System.out.println("Sent!!!");
        CommandReader.getInstance().unstashLine();
    }

    private String createChainData(){
        ExtraData extraData = new ExtraData();
        extraData.XUID = "";
        extraData.displayName = username;
        extraData.identity = clientUUID;

        ChainData chainData = new ChainData();
        chainData.nbf = Instant.now().getEpochSecond();
        chainData.exp = chainData.nbf + 10000;
        chainData.extraData = extraData;
        chainData.identityPublicKey = client.getEncodedPublicKey();

        Gson gson = new Gson();
        String json = gson.toJson(chainData, ChainData.class);
        return Base64.getUrlEncoder().encodeToString(json.getBytes());
    }

    private String createClientData(){
        ClientData clientData = new ClientData();
        clientData.CapeData = "";
        clientData.ClientRandomId = 111111;
        clientData.CurrentInputMode = 2;
        clientData.DefaultInputMode = 2;
        clientData.DeviceId = "8E9E5199E7C049859EE40780D6E9014C";
        clientData.DeviceModel = "iPhone9,1";
        clientData.DeviceOS = 2;
        clientData.GameVersion = "1.6.1";
        clientData.GuiScale = 0;
        clientData.LanguageCode = "ja_JP";
        clientData.PlatformOfflineId = "";
        clientData.PlatformOnlineId = "";
        clientData.PremiumSkin = false;
        clientData.SelfSignedId = clientUUID;
        clientData.ServerAddress = clientSession.getServerAddress() + ":" + clientSession.getServerPort();
        clientData.SkinData = Base64.getEncoder().encodeToString(new byte[16384]);
        clientData.SkinGeometry = getSkinGeometry();
        clientData.SkinGeometryName = "geometry.humanoid.custom";
        clientData.SkinId = "c18e65aa-7b21-4637-9b63-8ad63622ef01_Custom";
        clientData.ThirdPartyName = "";
        clientData.UIProfile = 1;

        Gson gson = new Gson();
        String json = gson.toJson(clientData, ClientData.class);
        return Base64.getUrlEncoder().encodeToString(json.getBytes());
    }

    public String getSkinGeometry(){
        return "ewogICJnZW9tZXRyeS5odW1hbm9pZCI6IHsKICAgICJib25lcyI6IFsKICAgICAgewogICAgICAgICJuYW1lIjogImJvZHkiLAogICAgICAgICJwaXZvdCI6IFsgMC4wLCAyNC4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC00LjAsIDEyLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDgsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMTYsIDE2IF0KICAgICAgICAgIH0KICAgICAgICBdCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAid2Fpc3QiLAogICAgICAgICJuZXZlclJlbmRlciI6IHRydWUsCiAgICAgICAgInBpdm90IjogWyAwLjAsIDEyLjAsIDAuMCBdCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAiaGVhZCIsCiAgICAgICAgInBpdm90IjogWyAwLjAsIDI0LjAsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTQuMCwgMjQuMCwgLTQuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgOCwgOCwgOCBdLAogICAgICAgICAgICAidXYiOiBbIDAsIDAgXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJoYXQiLAogICAgICAgICJwaXZvdCI6IFsgMC4wLCAyNC4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC00LjAsIDI0LjAsIC00LjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDgsIDgsIDggXSwKICAgICAgICAgICAgInV2IjogWyAzMiwgMCBdLAogICAgICAgICAgICAiaW5mbGF0ZSI6IDAuNQogICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgIm5ldmVyUmVuZGVyIjogdHJ1ZQogICAgICB9LAoKICAgICAgewogICAgICAgICJuYW1lIjogInJpZ2h0QXJtIiwKICAgICAgICAicGl2b3QiOiBbIC01LjAsIDIyLjAsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTguMCwgMTIuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyA0MCwgMTYgXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJsZWZ0QXJtIiwKICAgICAgICAicGl2b3QiOiBbIDUuMCwgMjIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyA0LjAsIDEyLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgNDAsIDE2IF0KICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJtaXJyb3IiOiB0cnVlCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAicmlnaHRMZWciLAogICAgICAgICJwaXZvdCI6IFsgLTEuOSwgMTIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtMy45LCAwLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMCwgMTYgXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJsZWZ0TGVnIiwKICAgICAgICAicGl2b3QiOiBbIDEuOSwgMTIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtMC4xLCAwLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMCwgMTYgXQogICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgIm1pcnJvciI6IHRydWUKICAgICAgfQogICAgXQogIH0sCgogICJnZW9tZXRyeS5jYXBlIjogewogICAgInRleHR1cmV3aWR0aCI6IDY0LAogICAgInRleHR1cmVoZWlnaHQiOiAzMiwKCiAgICAiYm9uZXMiOiBbCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJjYXBlIiwKICAgICAgICAicGl2b3QiOiBbIDAuMCwgMjQuMCwgLTMuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTUuMCwgOC4wLCAtMy4wIF0sCiAgICAgICAgICAgICJzaXplIjogWyAxMCwgMTYsIDEgXSwKICAgICAgICAgICAgInV2IjogWyAwLCAwIF0KICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfQogICAgXQogIH0sCiAgImdlb21ldHJ5Lmh1bWFub2lkLmN1c3RvbTpnZW9tZXRyeS5odW1hbm9pZCI6IHsKICAgICJib25lcyI6IFsKICAgICAgewogICAgICAgICJuYW1lIjogImhhdCIsCiAgICAgICAgIm5ldmVyUmVuZGVyIjogZmFsc2UsCiAgICAgICAgIm1hdGVyaWFsIjogImFscGhhIiwKICAgICAgICAicGl2b3QiOiBbIDAuMCwgMjQuMCwgMC4wIF0KICAgICAgfSwKICAgICAgewogICAgICAgICJuYW1lIjogImxlZnRBcm0iLAogICAgICAgICJyZXNldCI6IHRydWUsCiAgICAgICAgIm1pcnJvciI6IGZhbHNlLAogICAgICAgICJwaXZvdCI6IFsgNS4wLCAyMi4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIDQuMCwgMTIuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyAzMiwgNDggXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJyaWdodEFybSIsCiAgICAgICAgInJlc2V0IjogdHJ1ZSwKICAgICAgICAicGl2b3QiOiBbIC01LjAsIDIyLjAsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTguMCwgMTIuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyA0MCwgMTYgXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJyaWdodEl0ZW0iLAogICAgICAgICJwaXZvdCI6IFsgLTYsIDE1LCAxIF0sCiAgICAgICAgIm5ldmVyUmVuZGVyIjogdHJ1ZSwKICAgICAgICAicGFyZW50IjogInJpZ2h0QXJtIgogICAgICB9LAoKICAgICAgewogICAgICAgICJuYW1lIjogImxlZnRTbGVldmUiLAogICAgICAgICJwaXZvdCI6IFsgNS4wLCAyMi4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIDQuMCwgMTIuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyA0OCwgNDggXSwKICAgICAgICAgICAgImluZmxhdGUiOiAwLjI1CiAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAibWF0ZXJpYWwiOiAiYWxwaGEiCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAicmlnaHRTbGVldmUiLAogICAgICAgICJwaXZvdCI6IFsgLTUuMCwgMjIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtOC4wLCAxMi4wLCAtMi4wIF0sCiAgICAgICAgICAgICJzaXplIjogWyA0LCAxMiwgNCBdLAogICAgICAgICAgICAidXYiOiBbIDQwLCAzMiBdLAogICAgICAgICAgICAiaW5mbGF0ZSI6IDAuMjUKICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJsZWZ0TGVnIiwKICAgICAgICAicmVzZXQiOiB0cnVlLAogICAgICAgICJtaXJyb3IiOiBmYWxzZSwKICAgICAgICAicGl2b3QiOiBbIDEuOSwgMTIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtMC4xLCAwLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMTYsIDQ4IF0KICAgICAgICAgIH0KICAgICAgICBdCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAibGVmdFBhbnRzIiwKICAgICAgICAicGl2b3QiOiBbIDEuOSwgMTIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtMC4xLCAwLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMCwgNDggXSwKICAgICAgICAgICAgImluZmxhdGUiOiAwLjI1CiAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAicG9zIjogWyAxLjksIDEyLCAwIF0sCiAgICAgICAgIm1hdGVyaWFsIjogImFscGhhIgogICAgICB9LAoKICAgICAgewogICAgICAgICJuYW1lIjogInJpZ2h0UGFudHMiLAogICAgICAgICJwaXZvdCI6IFsgLTEuOSwgMTIuMCwgMC4wIF0sCiAgICAgICAgImN1YmVzIjogWwogICAgICAgICAgewogICAgICAgICAgICAib3JpZ2luIjogWyAtMy45LCAwLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDQsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMCwgMzIgXSwKICAgICAgICAgICAgImluZmxhdGUiOiAwLjI1CiAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAicG9zIjogWyAtMS45LCAxMiwgMCBdLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJqYWNrZXQiLAogICAgICAgICJwaXZvdCI6IFsgMC4wLCAyNC4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC00LjAsIDEyLjAsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDgsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgMTYsIDMyIF0sCiAgICAgICAgICAgICJpbmZsYXRlIjogMC4yNQogICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgIm1hdGVyaWFsIjogImFscGhhIgogICAgICB9CiAgICBdCiAgfSwKICAiZ2VvbWV0cnkuaHVtYW5vaWQuY3VzdG9tU2xpbTpnZW9tZXRyeS5odW1hbm9pZCI6IHsKCiAgICAiYm9uZXMiOiBbCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJoYXQiLAogICAgICAgICJuZXZlclJlbmRlciI6IGZhbHNlLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfSwKICAgICAgewogICAgICAgICJuYW1lIjogImxlZnRBcm0iLAogICAgICAgICJyZXNldCI6IHRydWUsCiAgICAgICAgIm1pcnJvciI6IGZhbHNlLAogICAgICAgICJwaXZvdCI6IFsgNS4wLCAyMS41LCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIDQuMCwgMTEuNSwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgMywgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyAzMiwgNDggXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJyaWdodEFybSIsCiAgICAgICAgInJlc2V0IjogdHJ1ZSwKICAgICAgICAicGl2b3QiOiBbIC01LjAsIDIxLjUsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTcuMCwgMTEuNSwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgMywgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyA0MCwgMTYgXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAicGl2b3QiOiBbIC02LCAxNC41LCAxIF0sCiAgICAgICAgIm5ldmVyUmVuZGVyIjogdHJ1ZSwKICAgICAgICAibmFtZSI6ICJyaWdodEl0ZW0iLAogICAgICAgICJwYXJlbnQiOiAicmlnaHRBcm0iCiAgICAgIH0sCgogICAgICB7CiAgICAgICAgIm5hbWUiOiAibGVmdFNsZWV2ZSIsCiAgICAgICAgInBpdm90IjogWyA1LjAsIDIxLjUsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgNC4wLCAxMS41LCAtMi4wIF0sCiAgICAgICAgICAgICJzaXplIjogWyAzLCAxMiwgNCBdLAogICAgICAgICAgICAidXYiOiBbIDQ4LCA0OCBdLAogICAgICAgICAgICAiaW5mbGF0ZSI6IDAuMjUKICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJyaWdodFNsZWV2ZSIsCiAgICAgICAgInBpdm90IjogWyAtNS4wLCAyMS41LCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC03LjAsIDExLjUsIC0yLjAgXSwKICAgICAgICAgICAgInNpemUiOiBbIDMsIDEyLCA0IF0sCiAgICAgICAgICAgICJ1diI6IFsgNDAsIDMyIF0sCiAgICAgICAgICAgICJpbmZsYXRlIjogMC4yNQogICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgIm1hdGVyaWFsIjogImFscGhhIgogICAgICB9LAoKICAgICAgewogICAgICAgICJuYW1lIjogImxlZnRMZWciLAogICAgICAgICJyZXNldCI6IHRydWUsCiAgICAgICAgIm1pcnJvciI6IGZhbHNlLAogICAgICAgICJwaXZvdCI6IFsgMS45LCAxMi4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC0wLjEsIDAuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyAxNiwgNDggXQogICAgICAgICAgfQogICAgICAgIF0KICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJsZWZ0UGFudHMiLAogICAgICAgICJwaXZvdCI6IFsgMS45LCAxMi4wLCAwLjAgXSwKICAgICAgICAiY3ViZXMiOiBbCiAgICAgICAgICB7CiAgICAgICAgICAgICJvcmlnaW4iOiBbIC0wLjEsIDAuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgNCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyAwLCA0OCBdLAogICAgICAgICAgICAiaW5mbGF0ZSI6IDAuMjUKICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJtYXRlcmlhbCI6ICJhbHBoYSIKICAgICAgfSwKCiAgICAgIHsKICAgICAgICAibmFtZSI6ICJyaWdodFBhbnRzIiwKICAgICAgICAicGl2b3QiOiBbIC0xLjksIDEyLjAsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTMuOSwgMC4wLCAtMi4wIF0sCiAgICAgICAgICAgICJzaXplIjogWyA0LCAxMiwgNCBdLAogICAgICAgICAgICAidXYiOiBbIDAsIDMyIF0sCiAgICAgICAgICAgICJpbmZsYXRlIjogMC4yNQogICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgIm1hdGVyaWFsIjogImFscGhhIgogICAgICB9LAoKICAgICAgewogICAgICAgICJuYW1lIjogImphY2tldCIsCiAgICAgICAgInBpdm90IjogWyAwLjAsIDI0LjAsIDAuMCBdLAogICAgICAgICJjdWJlcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgIm9yaWdpbiI6IFsgLTQuMCwgMTIuMCwgLTIuMCBdLAogICAgICAgICAgICAic2l6ZSI6IFsgOCwgMTIsIDQgXSwKICAgICAgICAgICAgInV2IjogWyAxNiwgMzIgXSwKICAgICAgICAgICAgImluZmxhdGUiOiAwLjI1CiAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAibWF0ZXJpYWwiOiAiYWxwaGEiCiAgICAgIH0KICAgIF0KICB9Cgp9Cg==";
    }

}
