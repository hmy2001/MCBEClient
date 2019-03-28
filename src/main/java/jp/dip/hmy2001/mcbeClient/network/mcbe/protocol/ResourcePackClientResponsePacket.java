package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

import jp.dip.hmy2001.mcbeClient.network.mcbe.GamePacket;

public class ResourcePackClientResponsePacket extends GamePacket {

    public int status;

    public byte getPacketId() {
        return ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;
    }

    public void encodeBody(){
        writeByte(status);
        writeShortLE(0);//TODO
    }

}
