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
    byte TEXT_PACKET = (byte) 0x09;
    byte SET_TIME_PACKET = (byte) 0x0a;
    byte START_GAME_PACKET = (byte) 0x0b;
    byte ADD_PLAYER_PACKET = (byte) 0x0c;
    byte ADD_ENTITY_PACKET = (byte) 0x0d;
    byte REMOVE_ENTITY_PACKET = (byte) 0x0e;
    byte ADD_ITEM_ENTITY_PACKET = (byte) 0x0f;

    byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x11;
    byte MOVE_ENTITY_ABSOLUTE_PACKET = (byte) 0x12;
    byte MOVE_PLAYER_PACKET = (byte) 0x13;
    byte RIDER_JUMP_PACKET = (byte) 0x14;
    byte UPDATE_BLOCK_PACKET = (byte) 0x15;
    byte ADD_PAINTING_PACKET = (byte) 0x16;
    byte EXPLODE_PACKET = (byte) 0x17;
    byte LEVEL_SOUND_EVENT_PACKET = (byte) 0x18;
    byte LEVEL_EVENT_PACKET = (byte) 0x19;
    byte BLOCK_EVENT_PACKET = (byte) 0x1a;
    byte ENTITY_EVENT_PACKET = (byte) 0x1b;
    byte MOB_EFFECT_PACKET = (byte) 0x1c;
    byte UPDATE_ATTRIBUTES_PACKET = (byte) 0x1d;
    byte INVENTORY_TRANSACTION_PACKET = (byte) 0x1e;
    byte MOB_EQUIPMENT_PACKET = (byte) 0x1f;
    byte MOB_ARMOR_EQUIPMENT_PACKET = (byte) 0x20;
    byte INTERACT_PACKET = (byte) 0x21;
    byte BLOCK_PICK_REQUEST_PACKET = (byte) 0x22;
    byte ENTITY_PICK_REQUEST_PACKET = (byte) 0x23;
    byte PLAYER_ACTION_PACKET = (byte) 0x24;
    byte ENTITY_FALL_PACKET = (byte) 0x25;
    byte HURT_ARMOR_PACKET = (byte) 0x26;
    byte SET_ENTITY_DATA_PACKET = (byte) 0x27;
    byte SET_ENTITY_MOTION_PACKET = (byte) 0x28;
    byte SET_ENTITY_LINK_PACKET = (byte) 0x29;
    byte SET_HEALTH_PACKET = (byte) 0x2a;
    byte SET_SPAWN_POSITION_PACKET = (byte) 0x2b;
    byte ANIMATE_PACKET = (byte) 0x2c;
    byte RESPAWN_PACKET = (byte) 0x2d;
    byte CONTAINER_OPEN_PACKET = (byte) 0x2e;
    byte CONTAINER_CLOSE_PACKET = (byte) 0x2f;
    byte PLAYER_HOTBAR_PACKET = (byte) 0x30;
    byte INVENTORY_CONTENT_PACKET = (byte) 0x31;
    byte INVENTORY_SLOT_PACKET = (byte) 0x32;
    byte CONTAINER_SET_DATA_PACKET = (byte) 0x33;
    byte CRAFTING_DATA_PACKET = (byte) 0x34;
    byte CRAFTING_EVENT_PACKET = (byte) 0x35;
    byte GUI_DATA_PICK_ITEM_PACKET = (byte) 0x36;
    byte ADVENTURE_SETTINGS_PACKET = (byte) 0x37;
    byte BLOCK_ENTITY_DATA_PACKET = (byte) 0x38;
    byte PLAYER_INPUT_PACKET = (byte) 0x39;
    byte FULL_CHUNK_DATA_PACKET = (byte) 0x3a;
    byte SET_COMMANDS_ENABLED_PACKET = (byte) 0x3b;
    byte SET_DIFFICULTY_PACKET = (byte) 0x3c;
    byte CHANGE_DIMENSION_PACKET = (byte) 0x3d;
    byte SET_PLAYER_GAME_TYPE_PACKET = (byte) 0x3e;
    byte PLAYER_LIST_PACKET = (byte) 0x3f;
    byte SIMPLE_EVENT_PACKET = (byte) 0x40;
    byte EVENT_PACKET = (byte) 0x41;
    byte SPAWN_EXPERIENCE_ORB_PACKET = (byte) 0x42;
    byte CLIENTBOUND_MAP_ITEM_DATA_PACKET = (byte) 0x43;
    byte MAP_INFO_REQUEST_PACKET = (byte) 0x44;
    byte REQUEST_CHUNK_RADIUS_PACKET = (byte) 0x45;
    byte CHUNK_RADIUS_UPDATED_PACKET = (byte) 0x46;
    byte ITEM_FRAME_DROP_ITEM_PACKET = (byte) 0x47;
    byte GAME_RULES_CHANGED_PACKET = (byte) 0x48;
    byte CAMERA_PACKET = (byte) 0x49;
    byte BOSS_EVENT_PACKET = (byte) 0x4a;
    byte SHOW_CREDITS_PACKET = (byte) 0x4b;
    byte AVAILABLE_COMMANDS_PACKET = (byte) 0x4c;
    byte COMMAND_REQUEST_PACKET = (byte) 0x4d;
    byte COMMAND_BLOCK_UPDATE_PACKET = (byte) 0x4e;
    byte COMMAND_OUTPUT_PACKET = (byte) 0x4f;
    byte UPDATE_TRADE_PACKET = (byte) 0x50;
    byte UPDATE_EQUIP_PACKET = (byte) 0x51;
    byte RESOURCE_PACK_DATA_INFO_PACKET = (byte) 0x52;
    byte RESOURCE_PACK_CHUNK_DATA_PACKET = (byte) 0x53;
    byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = (byte) 0x54;
    byte TRANSFER_PACKET = (byte) 0x55;
    byte PLAY_SOUND_PACKET = (byte) 0x56;
    byte STOP_SOUND_PACKET = (byte) 0x57;
    byte SET_TITLE_PACKET = (byte) 0x58;
    byte ADD_BEHAVIOR_TREE_PACKET = (byte) 0x59;
    byte STRUCTURE_BLOCK_UPDATE_PACKET = (byte) 0x5a;
    byte SHOW_STORE_OFFER_PACKET = (byte) 0x5b;
    byte PURCHASE_RECEIPT_PACKET = (byte) 0x5c;
    byte PLAYER_SKIN_PACKET = (byte) 0x5d;
    byte SUB_CLIENT_LOGIN_PACKET = (byte) 0x5e;
    byte W_S_CONNECT_PACKET = (byte) 0x5f;
    byte SET_LAST_HURT_BY_PACKET = (byte) 0x60;
    byte BOOK_EDIT_PACKET = (byte) 0x61;
    byte NPC_REQUEST_PACKET = (byte) 0x62;
    byte PHOTO_TRANSFER_PACKET = (byte) 0x63;
    byte MODAL_FORM_REQUEST_PACKET = (byte) 0x64;
    byte MODAL_FORM_RESPONSE_PACKET = (byte) 0x65;
    byte SERVER_SETTINGS_REQUEST_PACKET = (byte) 0x66;
    byte SERVER_SETTINGS_RESPONSE_PACKET = (byte) 0x67;
    byte SHOW_PROFILE_PACKET = (byte) 0x68;
    byte SET_DEFAULT_GAME_TYPE_PACKET = (byte) 0x69;
    byte REMOVE_OBJECTIVE_PACKET = (byte) 0x6a;
    byte SET_DISPLAY_OBJECTIVE_PACKET = (byte) 0x6b;
    byte SET_SCORE_PACKET = (byte) 0x6c;
    byte LAB_TABLE_PACKET = (byte) 0x6d;
    byte UPDATE_BLOCK_SYNCED_PACKET = (byte) 0x6e;
    byte MOVE_ENTITY_DELTA_PACKET = (byte) 0x6f;
    byte SET_SCOREBOARD_IDENTITY_PACKET = (byte) 0x70;
    byte SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET = (byte) 0x71;
    byte UPDATE_SOFT_ENUM_PACKET = (byte) 0x72;
    byte NETWORK_STACK_LATENCY_PACKET = (byte) 0x73;

    byte SCRIPT_CUSTOM_EVENT_PACKET = 0x75;
    byte SPAWN_PARTICLE_EFFECT_PACKET = 0x76;

    byte BATCH_PACKET = (byte) 0xfe;

}
