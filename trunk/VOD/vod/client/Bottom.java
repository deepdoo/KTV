package vod.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class Bottom extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 824011308204878144L;
	private JButton switchAudio;
	private JButton leftAudio;
	private JButton balanceAudio;
	private JButton rightAudio;
	private JButton next;
	private JButton pause;
	private JButton mute;
	private JSlider volume;

	public Bottom() {
		super(new GridLayout(1, 1, 20, 20));

		Font bf = new Font("System", Font.BOLD, 30);
		this.switchAudio = new JButton("原/伴");
		this.switchAudio.setFont(bf);
		this.leftAudio = new JButton("左声");
		this.leftAudio.setFont(bf);
		this.balanceAudio = new JButton("平衡");
		this.balanceAudio.setFont(bf);
		this.rightAudio = new JButton("右声 ");
		this.rightAudio.setFont(bf);
		this.next = new JButton("切歌");
		this.next.setFont(bf);
		this.pause = new JButton("暂停");
		this.pause.setFont(bf);
		this.volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		this.mute = new JButton("静音");
		this.mute.setFont(bf);
		this.add(this.switchAudio);
		this.add(this.leftAudio);
		this.add(this.balanceAudio);
		this.add(this.rightAudio);
		this.add(this.next);
		this.add(this.pause);
		this.add(this.volume);
		this.add(this.mute);

	}

	public void addListner(ActionListener switchAudioi, ActionListener balance, ActionListener next,
			ActionListener pause, ChangeListener volume, ActionListener mute) {
		this.switchAudio.addActionListener(switchAudioi);
		this.leftAudio.addActionListener(balance);
		this.rightAudio.addActionListener(balance);
		this.balanceAudio.addActionListener(balance);
		this.next.addActionListener(next);
		this.pause.addActionListener(pause);
		this.volume.addChangeListener(volume);
		this.mute.addActionListener(mute);
	}

}
