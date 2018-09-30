package jp.dip.hmy2001.mcbeClient.network.mcbe.protocol;

public interface ProtocolInfo {

    byte LOGIN_PACKET = (byte) 0x01;
    byte PLAY_STATUS_PACKET = (byte) 0x02;
    byte SERVER_TO_CLIENT_HANDSHAKE_PACKET = (byte) 0x03;
    byte CLIENT_TO_SERVER_HANDSHAKE_PACKET = (byte) 0x04;
    byte DISCONNECT_PACKET = (byte) 0x05;
    byte RESOURCE_PACKS_INFO_PACKET = (byte) 0x06;
    byte RESOURCE_PACK_STACK_PACKET = (byte) 0x07;
    byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = (byte) 0x08;

    byte START_GAME_PACKET = (byte) 0x0b;

    byte MOVE_PLAYER_PACKET = (byte) 0x13;

    byte MOB_EQUIPMENT_PACKET = (byte) 0x1f;

    byte INVENTORY_CONTENT_PACKET = (byte) 0x31;

    byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = (byte) 0x43;

    byte REQUEST_CHUNK_RADIUS_PACKET = (byte) 0x45;

    byte SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET = (byte) 0x71;

    byte BATCH_PACKET = (byte) 0xfe;

}
