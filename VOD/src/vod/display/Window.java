package vod.display;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import vod.resources.Resource;

public class Window extends JFrame {

	private GraphicsDevice gd;

	private Screen screen = new Screen();
	
	private static Window window = new Window();

	public static Window getInstance() {
		return window;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -624613098893025036L;

	private Window() {
		super();
		this.getContentPane().add(screen);
//		Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		this.setMinimumSize(fullScreenSize);
//		this.setMaximumSize(fullScreenSize);

		 this.setUndecorated(true);

		// set play device
		GraphicsDevice[] gds = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getScreenDevices();

		if (gds.length < Integer.parseInt(Resource.SCREEN_DEVICE)) {
			gd = gds[gds.length - 1];
		} else {
			gd = gds[Integer.parseInt(Resource.SCREEN_DEVICE) - 1];
		}

		if (gd == null) {
			gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice();
		}
		
//		if (this.gd.isFullScreenSupported()) {
//			this.gd.setFullScreenWindow(this);
//		}
		Rectangle rectangle = gd.getDefaultConfiguration().getBounds();
		this.setBounds(rectangle);
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// Screen Color
		this.screen.setBackground(Color.decode(Resource.SCREEN_COLOR));
		
		this.setAlwaysOnTop(true);
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}

	public String getHandleId() {
		return String.valueOf(this.screen.getWid());
	}
}