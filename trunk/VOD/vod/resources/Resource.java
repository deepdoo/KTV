package vod.resources;

import java.util.ResourceBundle;

public class Resource {

	private static ResourceBundle bundle = ResourceBundle
			.getBundle("ApplicationContext");

	public final static String PLAYER_PATH = bundle.getString("PP");

	public final static String SONG_PATH = bundle.getString("SP");
	
	public final static String SCREEN_TYPE = bundle.getString("ST");

	public final static String TYPE_PATH = bundle.getString("TYPE");

	public final static String SCREEN_DEVICE = bundle.getString("SD");
	
	public final static String CONSOLE_DEVICE = bundle.getString("CD");

	public final static String SCREEN_COLOR = bundle.getString("SC");
	
	public final static String NAMING_PATTERN = bundle.getString("NamingPattern");
	
	public final static String NAMING_PREFIX = bundle.getString("NamingPrefix");
}
