package vod.client;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vod.entities.Song;
import vod.entities.Star;
import vod.exe.Processer;
import vod.resources.Resource;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3661704460740345644L;

	private Processer p;

	// ----DATA
	private List<Star> starList;
	private List<Song> songList;
	private String[] starsV = new String[] {};
	private String[] songsV = new String[] {};
	private String[] selectedV = new String[] {};
	// ----DATA

	private Result selected = new Result();
	private Result star = new Result();
	private Result song = new Result();

	private JPanel top = new JPanel(new GridLayout());

	private PinyinArea pa = new PinyinArea();

	private boolean action1 = true;
	private boolean action2 = true;

	private void init() {

		//
		this.pa.action(this);
		this.star.setListData(starsV);
		this.song.setListData(songsV);
		this.selected.setListData(selectedV);

		this.star.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (action1) {
					action1 = false;
					if (star.getSelectedIndex() < 0)
						return;
					songList = starList.get(star.getSelectedIndex()).getSongs();
					songsV = new String[songList.size()];

					for (int i = 0; i < songsV.length; i++) {
						songsV[i] = songList.get(i).getName();
					}
					song.setListData(songsV);

				} else {
					action1 = true;
				}
			}
		});

		this.song.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (action2) {
					action2 = false;
					if (song.getSelectedIndex() > -1) {
						p.addToStore(songList.get(song.getSelectedIndex()));
						refreshSelectedList();
					}
				} else {
					action2 = true;
				}
			}
		});
	}

	public void refreshSelectedList() {
		selectedV = new String[p.getSelectedList().size()];
		for (int i = 0; i < p.getSelectedList().size(); i++) {
			selectedV[i] = p.getSelectedList().get(i).getName() + " "
					+ p.getSelectedList().get(i).getStar();
		}
		selected.setListData(selectedV);
	}

	public Client(Processer p) {
		super();
		this.p = p;
		JPanel panel = new JPanel(new BorderLayout());

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setDividerLocation(600);
		split.add(top);
		Bottom bottom = new Bottom();
		split.add(bottom);
		this.addListener(bottom);

		// Scroll
		JPanel leftPanel = new JPanel(new GridLayout(2, 1, 15, 0));
		JScrollPane rightScroll = new JScrollPane();
		top.add(leftPanel);
		top.add(pa);
		top.add(rightScroll);

		leftPanel.add(star);
		leftPanel.add(song);

		rightScroll.getViewport().add(this.selected);

		panel.add(split);

		// Set Device
		if ("0".compareTo(Resource.CONSOLE_DEVICE) < 0) {
			GraphicsDevice[] gds = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getScreenDevices();

			GraphicsDevice gd = null;
			if (gds.length < Integer.parseInt(Resource.CONSOLE_DEVICE)) {
				gd = gds[gds.length - 1];
			} else {
				gd = gds[Integer.parseInt(Resource.CONSOLE_DEVICE) - 1];
			}
			
			this.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
		}
		
		this.setSize(1024, 768);
		
		this.getContentPane().add(panel);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.init();
	}

	private void addListener(Bottom b) {
		b.addListner(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.audio();
			}
		}, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = ((JButton)e.getSource()).getText();
				if (text.startsWith("左")) {
					p.balance("-1");
				}
				if (text.startsWith("右")) {
					p.balance("1");
				}
				if (text.startsWith("平")) {
					p.balance("0");
				}
			}
		}, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.next();
			}
		}, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.pause();
			}
		}, new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				p.volume(((JSlider) e.getSource()).getValue());
			}
		}, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.mute();

			}
		});
	}

	public void getStars(String pinyin) {
		this.starList = p.findStarsByPinyin(pinyin);
		this.starsV = new String[starList.size()];

		for (int i = 0; i < starsV.length; i++) {
			starsV[i] = this.starList.get(i).getName();
		}
		this.star.setListData(starsV);
	}

	public void getSongs(String pinyin) {
		this.songList = p.findSongsByPinyin(pinyin);
		this.songsV = new String[songList.size()];

		for (int i = 0; i < songsV.length; i++) {
			songsV[i] = this.songList.get(i).getName();
		}
		this.song.setListData(songsV);
	}
}
