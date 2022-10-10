/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network;

import org.l2jmobius.Config;
import org.l2jmobius.commons.network.PacketWriter;

/**
 * @author UnAfraid
 */
public enum OutgoingPackets
{
	// Packets
	DIE(0x00),
	REVIVE(0x01),
	ATTACK_OUTOF_RANGE(0x02),
	ATTACKIN_COOL_TIME(0x03),
	ATTACK_DEAD_TARGET(0x04),
	SPAWN_ITEM(0x05),
	DELETE_OBJECT(0x08),
	CHARACTER_SELECTION_INFO(0x09),
	LOGIN_FAIL(0x0A),
	CHARACTER_SELECTED(0x0B),
	NPC_INFO(0x0C),
	NEW_CHARACTER_SUCCESS(0x0D),
	NEW_CHARACTER_FAIL(0x0E),
	CHARACTER_CREATE_SUCCESS(0x0F),
	CHARACTER_CREATE_FAIL(0x10),
	ITEM_LIST(0x11),
	SUNRISE(0x12),
	SUNSET(0x13),
	TRADE_START(0x14),
	TRADE_START_OK(0x15),
	DROP_ITEM(0x16),
	GET_ITEM(0x17),
	STATUS_UPDATE(0x18),
	NPC_HTML_MESSAGE(0x19),
	TRADE_OWN_ADD(0x1A),
	TRADE_OTHER_ADD(0x1B),
	TRADE_DONE(0x1C),
	CHARACTER_DELETE_SUCCESS(0x1D),
	CHARACTER_DELETE_FAIL(0x1E),
	ACTION_FAIL(0x1F),
	SERVER_CLOSE(0x20),
	INVENTORY_UPDATE(0x21),
	TELEPORT_TO_LOCATION(0x22),
	TARGET_SELECTED(0x23),
	TARGET_UNSELECTED(0x24),
	AUTO_ATTACK_START(0x25),
	AUTO_ATTACK_STOP(0x26),
	SOCIAL_ACTION(0x27),
	CHANGE_MOVE_TYPE(0x28),
	CHANGE_WAIT_TYPE(0x29),
	MANAGE_PLEDGE_POWER(0x2A),
	CREATE_PLEDGE(0x2B),
	ASK_JOIN_PLEDGE(0x2C),
	JOIN_PLEDGE(0x2D),
	VERSION_CHECK(0x2E),
	MOVE_TO_LOCATION(0x2F),
	NPC_SAY(0x30),
	CHAR_INFO(0x31),
	USER_INFO(0x32),
	ATTACK(0x33),
	WITHDRAWAL_PLEDGE(0x34),
	OUST_PLEDGE_MEMBER(0x35),
	SET_OUST_PLEDGE_MEMBER(0x36),
	DISMISS_PLEDGE(0x37),
	SET_DISMISS_PLEDGE(0x38),
	ASK_JOIN_PARTY(0x39),
	JOIN_PARTY(0x3A),
	WITHDRAWAL_PARTY(0x3B),
	OUST_PARTY_MEMBER(0x3C),
	SET_OUST_PARTY_MEMBER(0x3D),
	DISMISS_PARTY(0x3E),
	SET_DISMISS_PARTY(0x3F),
	USER_ACK(0x40),
	WAREHOUSE_DEPOSIT_LIST(0x41),
	WAREHOUSE_WITHDRAW_LIST(0x42),
	WAREHOUSE_DONE(0x43),
	SHORT_CUT_REGISTER(0x44),
	SHORT_CUT_INIT(0x45),
	SHORT_CUT_DELETE(0x46),
	STOP_MOVE(0x47),
	MAGIC_SKILL_USE(0x48),
	MAGIC_SKILL_CANCELED(0x49),
	SAY2(0x4A),
	EQUIP_UPDATE(0x4B),
	DOOR_INFO(0x4C),
	DOOR_STATUS_UPDATE(0x4D),
	PARTY_SMALL_WINDOW_ALL(0x4E),
	PARTY_SMALL_WINDOW_ADD(0x4F),
	PARTY_SMALL_WINDOW_DELETE_ALL(0x50),
	PARTY_SMALL_WINDOW_DELETE(0x51),
	PARTY_SMALL_WINDOW_UPDATE(0x52),
	TRADE_PRESS_OWN_OK(0x53),
	MAGIC_SKILL_LAUNCHED(0x54),
	FRIEND_ADD_REQUEST_RESULT(0x55),
	FRIEND_ADD(0x56),
	FRIEND_REMOVE(0x57),
	FRIEND_LIST(0x58),
	FRIEND_STATUS(0x59),
	PLEDGE_SHOW_MEMBER_LIST_ALL(0x5A),
	PLEDGE_SHOW_MEMBER_LIST_UPDATE(0x5B),
	PLEDGE_SHOW_MEMBER_LIST_ADD(0x5C),
	PLEDGE_SHOW_MEMBER_LIST_DELETE(0x5D),
	MAGIC_LIST(0x5E),
	SKILL_LIST(0x5F),
	VEHICLE_INFO(0x60),
	FINISH_ROTATING(0x61),
	SYSTEM_MESSAGE(0x62),
	START_PLEDGE_WAR(0x63),
	REPLY_START_PLEDGE_WAR(0x64),
	STOP_PLEDGE_WAR(0x65),
	REPLY_STOP_PLEDGE_WAR(0x66),
	SURRENDER_PLEDGE_WAR(0x67),
	REPLY_SURRENDER_PLEDGE_WAR(0x68),
	SET_PLEDGE_CREST(0x69),
	PLEDGE_CREST(0x6A),
	SETUP_GAUGE(0x6B),
	VEHICLE_DEPARTURE(0x6C),
	VEHICLE_CHECK_LOCATION(0x6D),
	GET_ON_VEHICLE(0x6E),
	GET_OFF_VEHICLE(0x6F),
	TRADE_REQUEST(0x70),
	RESTART_RESPONSE(0x71),
	MOVE_TO_PAWN(0x72),
	SSQ_INFO(0x73),
	GAME_GUARD_QUERY(0x74),
	L2_FRIEND_LIST(0x75),
	L2_FRIEND(0x76),
	L2_FRIEND_STATUS(0x77),
	L2_FRIEND_SAY(0x78),
	VALIDATE_LOCATION(0x79),
	START_ROTATING(0x7A),
	SHOW_BOARD(0x7B),
	CHOOSE_INVENTORY_ITEM(0x7C),
	DUMMY(0x7D),
	MOVE_TO_LOCATION_IN_VEHICLE(0x7E),
	STOP_MOVE_IN_VEHICLE(0x7F),
	VALIDATE_LOCATION_IN_VEHICLE(0x80),
	TRADE_UPDATE(0x81),
	TRADE_PRESS_OTHER_OK(0x82),
	FRIEND_ADD_REQUEST(0x83),
	LOG_OUT_OK(0x84),
	ABNORMAL_STATUS_UPDATE(0x85),
	QUEST_LIST(0x86),
	ENCHANT_RESULT(0x87),
	PLEDGE_SHOW_MEMBER_LIST_DELETE_ALL(0x88),
	PLEDGE_INFO(0x89),
	PLEDGE_EXTENDED_INFO(0x8A),
	SURRENDER_PERSONALLY(0x8B),
	RIDE(0x8C),
	GIVE_NICK_NAME_DONE(0x8D),
	PLEDGE_SHOW_INFO_UPDATE(0x8E),
	CLIENT_ACTION(0x8F),
	ACQUIRE_SKILL_LIST(0x90),
	ACQUIRE_SKILL_INFO(0x91),
	SERVER_OBJECT_INFO(0x92),
	GM_HIDE(0x93),
	ACQUIRE_SKILL_DONE(0x94),
	GM_VIEW_CHARACTER_INFO(0x95),
	GM_VIEW_PLEDGE_INFO(0x96),
	GM_VIEW_SKILL_INFO(0x97),
	GM_VIEW_MAGIC_INFO(0x98),
	GM_VIEW_QUEST_INFO(0x99),
	GM_VIEW_ITEM_LIST(0x9A),
	GM_VIEW_WAREHOUSE_WITHDRAW_LIST(0x9B),
	LIST_PARTY_WAITING(0x9C),
	PARTY_ROOM_INFO(0x9D),
	PLAY_SOUND(0x9E),
	STATIC_OBJECT(0x9F),
	PRIVATE_STORE_SELL_MANAGE_LIST(0xA0),
	PRIVATE_STORE_SELL_LIST(0xA1),
	PRIVATE_STORE_SELL_MSG(0xA2),
	SHOW_MINIMAP(0xA3),
	REVIVE_REQUEST(0xA4),
	ABNORMAL_VISUAL_EFFECT(0xA5),
	TUTORIAL_SHOW_HTML(0xA6),
	SHOW_TUTORIAL_MARK(0xA7),
	TUTORIAL_ENABLE_CLIENT_EVENT(0xA8),
	TUTORIAL_CLOSE_HTML(0xA9),
	SHOW_RADAR(0xAA),
	WITHDRAW_ALLIANCE(0xAB),
	OUST_ALLIANCE_MEMBER_PLEDGE(0xAC),
	DISMISS_ALLIANCE(0xAD),
	SET_ALLIANCE_CREST(0xAE),
	ALLIANCE_CREST(0xAF),
	SERVER_CLOSE_SOCKET(0xB0),
	PET_STATUS_SHOW(0xB1),
	PET_INFO(0xB2),
	PET_ITEM_LIST(0xB3),
	PET_INVENTORY_UPDATE(0xB4),
	ALLIANCE_INFO(0xB5),
	PET_STATUS_UPDATE(0xB6),
	PET_DELETE(0xB7),
	DELETE_RADAR(0xB8),
	MY_TARGET_SELECTED(0xB9),
	PARTY_MEMBER_POSITION(0xBA),
	ASK_JOIN_ALLIANCE(0xBB),
	JOIN_ALLIANCE(0xBC),
	PRIVATE_STORE_BUY_MANAGE_LIST(0xBD),
	PRIVATE_STORE_BUY_LIST(0xBE),
	PRIVATE_STORE_BUY_MSG(0xBF),
	VEHICLE_START(0xC0),
	REQUEST_TIME_CHECK(0xC1),
	START_ALLIANCE_WAR(0xC2),
	REPLY_START_ALLIANCE_WAR(0xC3),
	STOP_ALLIANCE_WAR(0xC4),
	REPLY_STOP_ALLIANCE_WAR(0xC5),
	SURRENDER_ALLIANCE_WAR(0xC6),
	SKILL_COOL_TIME(0xC7),
	PACKAGE_TO_LIST(0xC8),
	CASTLE_SIEGE_INFO(0xC9),
	CASTLE_SIEGE_ATTACKER_LIST(0xCA),
	CASTLE_SIEGE_DEFENDER_LIST(0xCB),
	NICK_NAME_CHANGED(0xCC),
	PLEDGE_STATUS_CHANGED(0xCD),
	RELATION_CHANGED(0xCE),
	EVENT_TRIGGER(0xCF),
	MULTI_SELL_LIST(0xD0),
	SET_SUMMON_REMAIN_TIME(0xD1),
	PACKAGE_SENDABLE_LIST(0xD2),
	EARTHQUAKE(0xD3),
	FLY_TO_LOCATION(0xD4),
	BLOCK_LIST(0xD5),
	SPECIAL_CAMERA(0xD6),
	NORMAL_CAMERA(0xD7),
	SKILL_REMAIN_SEC(0xD8),
	NET_PING(0xD9),
	DICE(0xDA),
	SNOOP(0xDB),
	RECIPE_BOOK_ITEM_LIST(0xDC),
	RECIPE_ITEM_MAKE_INFO(0xDD),
	RECIPE_SHOP_MANAGE_LIST(0xDE),
	RECIPE_SHOP_SELL_LIST(0xDF),
	RECIPE_SHOP_ITEM_INFO(0xE0),
	RECIPE_SHOP_MSG(0xE1),
	SHOW_CALC(0xE2),
	MON_RACE_INFO(0xE3),
	HENNA_ITEM_INFO(0xE4),
	HENNA_INFO(0xE5),
	HENNA_UNEQUIP_LIST(0xE6),
	HENNA_UNEQUIP_INFO(0xE7),
	MACRO_LIST(0xE8),
	BUY_LIST_SEED(0xE9),
	SHOW_TOWN_MAP(0xEA),
	OBSERVER_START(0xEB),
	OBSERVER_END(0xEC),
	CHAIR_SIT(0xED),
	HENNA_EQUIP_LIST(0xEE),
	SELL_LIST_PROCURE(0xEF),
	GM_HENNA_INFO(0xF0),
	RADAR_CONTROL(0xF1),
	CLIENT_SET_TIME(0xF2),
	CONFIRM_DLG(0xF3),
	PARTY_SPELLED(0xF4),
	SHOP_PREVIEW_LIST(0xF5),
	SHOP_PREVIEW_INFO(0xF6),
	CAMERA_MODE(0xF7),
	SHOW_XMAS_SEAL(0xF8),
	ETC_STATUS_UPDATE(0xF9),
	SHORT_BUFF_STATUS_UPDATE(0xFA),
	SSQ_STATUS(0xFB),
	PETITION_VOTE(0xFC),
	AGIT_DECO_INFO(0xFD),
	// ExPackets
	EX_DUMMY(0xFE, 0x00),
	EX_REGEN_MAX(0xFE, 0x01),
	EX_EVENT_MATCH_USER_INFO(0xFE, 0x02),
	EX_COLOSSEUM_FENCE_INFO(0xFE, 0x03),
	EX_EVENT_MATCH_SPELLED_INFO(0xFE, 0x04),
	EX_EVENT_MATCH_FIRECRACKER(0xFE, 0x05),
	EX_EVENT_MATCH_TEAM_UNLOCKED(0xFE, 0x06),
	EX_EVENT_MATCH_GM_TEST(0xFE, 0x07),
	EX_PARTY_ROOM_MEMBER(0xFE, 0x08),
	EX_CLOSE_PARTY_ROOM(0xFE, 0x09),
	EX_MANAGE_PARTY_ROOM_MEMBER(0xFE, 0x0A),
	EX_EVENT_MATCH_LOCK_RESULT(0xFE, 0x0B),
	EX_AUTO_SOULSHOT(0xFE, 0x0C),
	EX_EVENT_MATCH_LIST(0xFE, 0x0D),
	EX_EVENT_MATCH_OBSERVER(0xFE, 0x0E),
	EX_EVENT_MATCH_MESSAGE(0xFE, 0x0F),
	EX_EVENT_MATCH_SCORE(0xFE, 0x10),
	EX_SERVER_PRIMITIVE(0xFE, 0x11),
	EX_OPEN_MPCC(0xFE, 0x12),
	EX_CLOSE_MPCC(0xFE, 0x13),
	EX_SHOW_CASTLE_INFO(0xFE, 0x14),
	EX_SHOW_FORTRESS_INFO(0xFE, 0x15),
	EX_SHOW_AGIT_INFO(0xFE, 0x16),
	EX_SHOW_FORTRESS_SIEGE_INFO(0xFE, 0x17),
	EX_PARTY_PET_WINDOW_ADD(0xFE, 0x18),
	EX_PARTY_PET_WINDOW_UPDATE(0xFE, 0x19),
	EX_ASK_JOIN_MPCC(0xFE, 0x1A),
	EX_PLEDGE_EMBLEM(0xFE, 0x1B),
	EX_EVENT_MATCH_TEAM_INFO(0xFE, 0x1C),
	EX_EVENT_MATCH_CREATE(0xFE, 0x1D),
	EX_FISHING_START(0xFE, 0x1E),
	EX_FISHING_END(0xFE, 0x1F),
	EX_SHOW_QUEST_INFO(0xFE, 0x20),
	EX_SHOW_QUEST_MARK(0xFE, 0x21),
	EX_SEND_MANOR_LIST(0xFE, 0x22),
	EX_SHOW_SEED_INFO(0xFE, 0x23),
	EX_SHOW_CROP_INFO(0xFE, 0x24),
	EX_SHOW_MANOR_DEFAULT_INFO(0xFE, 0x25),
	EX_SHOW_SEED_SETTING(0xFE, 0x26),
	EX_FISHING_START_COMBAT(0xFE, 0x27),
	EX_FISHING_HP_REGEN(0xFE, 0x28),
	EX_ENCHANT_SKILL_LIST(0xFE, 0x29),
	EX_ENCHANT_SKILL_INFO(0xFE, 0x2A),
	EX_SHOW_CROP_SETTING(0xFE, 0x2B),
	EX_SHOW_SELL_CROP_LIST(0xFE, 0x2C),
	EX_OLYMPIAD_MATCH_END(0xFE, 0x2D),
	EX_MAIL_ARRIVED(0xFE, 0x2E),
	EX_STORAGE_MAX_COUNT(0xFE, 0x2F),
	EX_EVENT_MATCH_MANAGE(0xFE, 0x30),
	EX_MULTI_PARTY_COMMAND_CHANNEL_INFO(0xFE, 0x31),
	EX_PC_CAFE_POINT_INFO(0xFE, 0x32),
	EX_SET_COMPASS_ZONE_CODE(0xFE, 0x33),
	EX_GET_BOSS_RECORD(0xFE, 0x34),
	EX_ASK_JOIN_PARTY_ROOM(0xFE, 0x35),
	EX_LIST_PARTY_MATCHING_WAITING_ROOM(0xFE, 0x36),
	EX_SET_MPCC_ROUTING(0xFE, 0x37),
	EX_SHOW_ADVENTURER_GUIDE_BOOK(0xFE, 0x38),
	EX_SHOW_SCREEN_MESSAGE(0xFE, 0x39),
	PLEDGE_SKILL_LIST(0xFE, 0x3A),
	PLEDGE_SKILL_LIST_ADD(0xFE, 0x3B),
	PLEDGE_POWER_GRADE_LIST(0xFE, 0x3C),
	PLEDGE_RECEIVE_POWER_INFO(0xFE, 0x3D),
	PLEDGE_RECEIVE_MEMBER_INFO(0xFE, 0x3E),
	PLEDGE_RECEIVE_WAR_LIST(0xFE, 0x3F),
	PLEDGE_RECEIVE_SUB_PLEDGE_CREATED(0xFE, 0x40),
	EX_RED_SKY(0xFE, 0x41),
	PLEDGE_RECEIVE_UPDATE_POWER(0xFE, 0x42),
	FLY_SELF_DESTINATION(0xFE, 0x43),
	SHOW_PC_CAFE_COUPON_SHOW_UI(0xFE, 0x44),
	EX_SEARCH_ORC(0xFE, 0x45),
	EX_CURSED_WEAPON_LIST(0xFE, 0x46),
	EX_CURSED_WEAPON_LOCATION(0xFE, 0x47),
	EX_RESTART_CLIENT(0xFE, 0x48),
	EX_REQUEST_HACK_SHIELD(0xFE, 0x49),
	EX_USE_SHARED_GROUP_ITEM(0xFE, 0x4A),
	EX_MPCC_SHOW_PARTY_MEMBER_INFO(0xFE, 0x4B),
	EX_DUEL_ASK_START(0xFE, 0x4C),
	EX_DUEL_READY(0xFE, 0x4D),
	EX_DUEL_START(0xFE, 0x4E),
	EX_DUEL_END(0xFE, 0x4F),
	EX_DUEL_UPDATE_USER_INFO(0xFE, 0x50),
	EX_SHOW_VARIATION_MAKE_WINDOW(0xFE, 0x51),
	EX_SHOW_VARIATION_CANCEL_WINDOW(0xFE, 0x52),
	EX_PUT_ITEM_RESULT_FOR_VARIATION_MAKE(0xFE, 0x53),
	EX_PUT_INTENSIVE_RESULT_FOR_VARIATION_MAKE(0xFE, 0x54),
	EX_PUT_COMMISSION_RESULT_FOR_VARIATION_MAKE(0xFE, 0x55),
	EX_VARIATION_RESULT(0xFE, 0x56),
	EX_PUT_ITEM_RESULT_FOR_VARIATION_CANCEL(0xFE, 0x57),
	EX_VARIATION_CANCEL_RESULT(0xFE, 0x58),
	EX_DUEL_ENEMY_RELATION(0xFE, 0x59),
	EX_PLAY_ANIMATION(0xFE, 0x5A),
	EX_MPCC_PARTY_INFO_UPDATE(0xFE, 0x5B),
	EX_PLAY_SCENE(0xFE, 0x5C),
	EX_SPAWN_EMITTER(0xFE, 0x5D),
	EX_ENCHANT_SKILL_INFO_DETAIL(0xFE, 0x5E),
	EX_BASIC_ACTION_LIST(0xFE, 0x5F),
	EX_AIRSHIP_INFO(0xFE, 0x60),
	EX_ATTRIBUTE_ENCHANT_RESULT(0xFE, 0x61),
	EX_CHOOSE_INVENTORY_ATTRIBUTE_ITEM(0xFE, 0x62),
	EX_GET_ON_AIRSHIP(0xFE, 0x63),
	EX_GET_OFF_AIRSHIP(0xFE, 0x64),
	EX_MOVE_TO_LOCATION_AIRSHIP(0xFE, 0x65),
	EX_STOP_MOVE_AIRSHIP(0xFE, 0x66),
	EX_SHOW_TRACE(0xFE, 0x67),
	EX_ITEM_AUCTION_INFO(0xFE, 0x68),
	EX_NEED_TO_CHANGE_NAME(0xFE, 0x69),
	EX_PARTY_PET_WINDOW_DELETE(0xFE, 0x6A),
	EX_TUTORIAL_LIST(0xFE, 0x6B),
	EX_RP_ITEM_LINK(0xFE, 0x6C),
	EX_MOVE_TO_LOCATION_IN_AIRSHIP(0xFE, 0x6D),
	EX_STOP_MOVE_IN_AIRSHIP(0xFE, 0x6E),
	EX_VALIDATE_LOCATION_IN_AIRSHIP(0xFE, 0x6F),
	EX_UI_SETTING(0xFE, 0x70),
	EX_MOVE_TO_TARGET_IN_AIRSHIP(0xFE, 0x71),
	EX_ATTACK_IN_AIRSHIP(0xFE, 0x72),
	EX_MAGIC_SKILL_USE_IN_AIRSHIP(0xFE, 0x73),
	EX_SHOW_BASE_ATTRIBUTE_CANCEL_WINDOW(0xFE, 0x74),
	EX_BASE_ATTRIBUTE_CANCEL_RESULT(0xFE, 0x75),
	EX_SUB_PLEDGE_SKILL_ADD(0xFE, 0x76),
	EX_RESPONSE_FREE_SERVER(0xFE, 0x77),
	EX_SHOW_PROCURE_CROP_DETAIL(0xFE, 0x78),
	EX_HERO_LIST(0xFE, 0x79),
	EX_OLYMPIAD_USER_INFO(0xFE, 0x7A),
	EX_OLYMPIAD_SPELLED_INFO(0xFE, 0x7B),
	EX_OLYMPIAD_MODE(0xFE, 0x7C),
	EX_SHOW_FORTRESS_MAP_INFO(0xFE, 0x7D),
	EX_PVP_MATCH_RECORD(0xFE, 0x7E),
	EX_PVP_MATCH_USER_DIE(0xFE, 0x7F),
	EX_PRIVATE_STORE_PACKAGE_MSG(0xFE, 0x80),
	EX_PUT_ENCHANT_TARGET_ITEM_RESULT(0xFE, 0x81),
	EX_PUT_ENCHANT_SUPPORT_ITEM_RESULT(0xFE, 0x82),
	EX_REQUEST_CHANGE_NICKNAME_COLOR(0xFE, 0x83),
	EX_GET_BOOKMARK_INFO(0xFE, 0x84),
	EX_NOTIFY_PREMIUM_ITEM(0xFE, 0x85),
	EX_GET_PREMIUM_ITEM_LIST(0xFE, 0x86),
	EX_PERIODIC_ITEM_LIST(0xFE, 0x87),
	EX_JUMP_TO_LOCATION(0xFE, 0x88),
	EX_PVP_MATCH_CC_RECORD(0xFE, 0x89),
	EX_PVP_MATCH_CC_MY_RECORD(0xFE, 0x8A),
	EX_PVP_MATCH_CC_RETIRE(0xFE, 0x8B),
	EX_SHOW_TERRITORY(0xFE, 0x8C),
	EX_NPC_QUEST_HTML_MESSAGE(0xFE, 0x8D),
	EX_SEND_UI_EVENT(0xFE, 0x8E),
	EX_NOTIFY_BIRTHDAY(0xFE, 0x8F),
	EX_SHOW_DOMINION_REGISTRY(0xFE, 0x90),
	EX_REPLY_REGISTER_DOMINION(0xFE, 0x91),
	EX_REPLY_DOMINION_INFO(0xFE, 0x92),
	EX_SHOW_OWNTHING_POS(0xFE, 0x93),
	EX_CLEFT_LIST(0xFE, 0x94),
	EX_CLEFT_STATE(0xFE, 0x95),
	EX_DOMINION_CHANNEL_SET(0xFE, 0x96),
	EX_BLOCK_UP_SET_LIST(0xFE, 0x97),
	EX_BLOCK_UP_SET_STATE(0xFE, 0x98),
	EX_START_SCENE_PLAYER(0xFE, 0x99),
	EX_AIRSHIP_TELEPORT_LIST(0xFE, 0x9A),
	EX_MPCC_ROOM_INFO(0xFE, 0x9B),
	EX_LIST_MPCC_WAITING(0xFE, 0x9C),
	EX_DISSMISS_MPCC_ROOM(0xFE, 0x9D),
	EX_MANAGE_MPCC_ROOM_MEMBER(0xFE, 0x9E),
	EX_MPCC_ROOM_MEMBER(0xFE, 0x9F),
	EX_VITALITY_POINT_INFO(0xFE, 0xA0),
	EX_SHOW_SEED_MAP_INFO(0xFE, 0xA1),
	EX_MPCC_PARTYMASTER_LIST(0xFE, 0xA2),
	EX_DOMINION_WAR_START(0xFE, 0xA3),
	EX_DOMINION_WAR_END(0xFE, 0xA4),
	EX_SHOW_LINES(0xFE, 0xA5),
	EX_PARTY_MEMBER_RENAMED(0xFE, 0xA6),
	EX_ENCHANT_SKILL_RESULT(0xFE, 0xA7),
	EX_REFUND_LIST(0xFE, 0xA8),
	EX_NOTICE_POST_ARRIVED(0xFE, 0xA9),
	EX_SHOW_RECEIVED_POST_LIST(0xFE, 0xAA),
	EX_REPLY_RECEIVED_POST(0xFE, 0xAB),
	EX_SHOW_SENT_POST_LIST(0xFE, 0xAC),
	EX_REPLY_SENT_POST(0xFE, 0xAD),
	EX_RESPONSE_SHOW_STEP_ONE(0xFE, 0xAE),
	EX_RESPONSE_SHOW_STEP_TWO(0xFE, 0xAF),
	EX_RESPONSE_SHOW_CONTENTS(0xFE, 0xB0),
	EX_SHOW_PETITION_HTML(0xFE, 0xB1),
	EX_REPLY_POST_ITEM_LIST(0xFE, 0xB2),
	EX_CHANGE_POST_STATE(0xFE, 0xB3),
	EX_NOTICE_POST_SENT(0xFE, 0xB4),
	EX_INITIALIZE_SEED(0xFE, 0xB5),
	EX_RAID_RESERVE_RESULT(0xFE, 0xB6),
	EX_BUY_SELL_LIST(0xFE, 0xB7),
	EX_CLOSE_RAID_SOCKET(0xFE, 0xB8),
	EX_PRIVATE_MARKET_LIST(0xFE, 0xB9),
	EX_RAID_CHARACTER_SELECTED(0xFE, 0xBA),
	EX_ASK_COUPLE_ACTION(0xFE, 0xBB),
	EX_BR_BROADCAST_EVENT_STATE(0xFE, 0xBC),
	EX_BR_LOAD_EVENT_TOP_RANKERS(0xFE, 0xBD),
	EX_CHANGE_NPC_STATE(0xFE, 0xBE),
	EX_ASK_MODIFY_PARTY_LOOTING(0xFE, 0xBF),
	EX_SET_PARTY_LOOTING(0xFE, 0xC0),
	EX_ROTATION(0xFE, 0xC1),
	EX_CHANGE_CLIENT_EFFECT_INFO(0xFE, 0xC2),
	EX_MEMBERSHIP_INFO(0xFE, 0xC3),
	EX_REPLY_HAND_OVER_PARTY_MASTER(0xFE, 0xC4),
	EX_QUEST_NPC_LOG_LIST(0xFE, 0xC5),
	EX_QUEST_ITEM_LIST(0xFE, 0xC6),
	EX_GM_VIEW_QUEST_ITEM_LIST(0xFE, 0xC7),
	EX_RESTART_RESPONSE(0xFE, 0xC8),
	EX_VOTE_SYSTEM_INFO(0xFE, 0xC9),
	EX_SHUTTLE_INFO(0xFE, 0xCA),
	EX_SUTTLE_GET_ON(0xFE, 0xCB),
	EX_SUTTLE_GET_OFF(0xFE, 0xCC),
	EX_SUTTLE_MOVE(0xFE, 0xCD),
	EX_MOVE_TO_LOCATION_IN_SUTTLE(0xFE, 0xCE),
	EX_STOP_MOVE_IN_SHUTTLE(0xFE, 0xCF),
	EX_VALIDATE_LOCATION_IN_SHUTTLE(0xFE, 0xD0),
	EX_AGIT_AUCTION_CMD(0xFE, 0xD1),
	EX_CONFIRM_ADDING_POST_FRIEND(0xFE, 0xD2),
	EX_RECEIVE_SHOW_POST_FRIEND(0xFE, 0xD3),
	EX_RECEIVE_OLYMPIAD(0xFE, 0xD4),
	EX_BR_GAME_POINT(0xFE, 0xD5),
	EX_BR_PRODUCT_LIST(0xFE, 0xD6),
	EX_BR_PRODUCT_INFO(0xFE, 0xD7),
	EX_BR_BUY_PRODUCT(0xFE, 0xD8),
	EX_BR_PREMIUM_STATE(0xFE, 0xD9),
	EX_BR_EXTRA_USER_INFO(0xFE, 0xDA),
	EX_BR_BUFF_EVENT_STATE(0xFE, 0xDB),
	EX_BR_RECENT_PRODUCT_LIST(0xFE, 0xDC),
	EX_BR_MINIGAME_LOAD_SCORES(0xFE, 0xDD),
	EX_BR_AGATHION_ENERGY_INFO(0xFE, 0xDE),
	EX_NAVIT_ADVENT_POINT_INFO(0xFE, 0xDF),
	EX_NAVIT_ADVENT_EFFECT(0xFE, 0xE0),
	EX_NAVIT_ADVENT_TIME_CHANGE(0xFE, 0xE1),
	EX_GOODS_INVENTORY_CHANGED_NOTIFY(0xFE, 0xE2),
	EX_GOODS_INVENTORY_INFO(0xFE, 0xE3),
	EX_GOODS_INVENTORY_RESULT(0xFE, 0xE4),
	EX_2ND_PASSWORD_CHECK(0xFE, 0xE5),
	EX_2ND_PASSWORD_VERIFY(0xFE, 0xE6),
	EX_2ND_PASSWORD_ACK(0xFE, 0xE7),
	EX_SAY2_FAIL(0xFE, 0xE8);
	
	private final int _id1;
	private final int _id2;
	
	OutgoingPackets(int id1)
	{
		this(id1, -1);
	}
	
	OutgoingPackets(int id1, int id2)
	{
		_id1 = id1;
		_id2 = id2;
	}
	
	public int getId1()
	{
		return _id1;
	}
	
	public int getId2()
	{
		return _id2;
	}
	
	public void writeId(PacketWriter packet)
	{
		if (Config.DEBUG_OUTGOING_PACKETS)
		{
			final String name = packet.getClass().getSimpleName();
			if (!Config.ALT_DEV_EXCLUDED_PACKETS.contains(name))
			{
				PacketLogger.info((_id2 > 0 ? "[S EX] " : "[S] ") + name);
			}
		}
		
		packet.writeC(_id1);
		if (_id2 > 0)
		{
			packet.writeH(_id2);
		}
	}
	
	public static OutgoingPackets getPacket(int id1, int id2)
	{
		for (OutgoingPackets packet : values())
		{
			if ((packet.getId1() == id1) && (packet.getId2() == id2))
			{
				return packet;
			}
		}
		return null;
	}
}
