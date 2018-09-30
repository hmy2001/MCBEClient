package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class LoginPacket extends GamePacket{
    byte NETWORK_ID = ProtocolInfo.LOGIN_PACKET;

    public int protocol;
    public int bodyLength;
    public int chainDataLength;
    public byte[] chainData;
    public int clientDataLength;
    public byte[] clientData;

    public byte getPacketId() {
        return NETWORK_ID;
    }

    public void encodeBody(){
        writeInt(protocol);
        writeUnsignedVarInt(bodyLength);
        writeIntLE(chainDataLength);
        write(chainData);
        writeIntLE(clientDataLength);
        write(clientData);
    }

}
