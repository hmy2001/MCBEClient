package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class LoginPacket extends GamePacket{

    public int protocol;
    public byte[] body;

    public byte getPacketId() {
        return ProtocolInfo.LOGIN_PACKET;
    }

    public void encodeBody(){
        writeInt(protocol);
        writeUnsignedVarInt(body.length);
        write(body);
    }

}
