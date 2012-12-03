package vod.display;

import java.awt.Canvas;
import java.awt.peer.ComponentPeer;

public class Screen extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4888337464308130137L;

	public Screen() {
		super();
	}

	@SuppressWarnings("deprecation")
	public long getWid() {
		long wid = -1;
		try {
			Class<?> cl = Class.forName("sun.awt.windows.WComponentPeer");
			java.lang.reflect.Field f = cl.getDeclaredField("hwnd");
			f.setAccessible(true);
			ComponentPeer peer = getPeer();
			wid = f.getLong(peer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wid;
	}

}
