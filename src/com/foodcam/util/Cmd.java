package com.foodcam.util;

/**
 * 공개키를 포함한 Json 사용 간 통신 규약
 * @author root
 *
 */
public abstract class Cmd {
	private static final String COMMAND_KEY = "[COMMAND_SERVER_FOOD_MATCHER]";
	public static final String SERVER_KEY = COMMAND_KEY + "[SERVER_KEY_SERVER_FOOD_MATCHER]";
	public static final String SERVER_IS_BUSY = COMMAND_KEY + "[SERVER_IS_BUSY]";
	public static final String SERVER_IS_STABLE = COMMAND_KEY + "[SERVER_IS_STABLE]";

	public static final String REQUEST_PIC = COMMAND_KEY + "[REQUEST_PIC]";
	public static final String BAD_ACCESS = COMMAND_KEY + "[BAD_ACCESS]";
}