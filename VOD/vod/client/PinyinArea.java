package vod.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PinyinArea extends JPanel {

	private JButton[] bs = new JButton[27];
	/**
	 * 
	 */
	private static final long serialVersionUID = 8779724242218926787L;

	public PinyinArea() {
		super(new GridLayout(7, 4));

		Font bf = new Font("System", Font.BOLD, 46);
		JButton b = null;
		for (char i = 0; i < bs.length -1; i++) {
			b = new JButton(String.valueOf((char)('A' + i)));
			bs[i] = b;
			b.setFont(bf);
			this.add(b);
		}
		b = new JButton("←");
		bs[bs.length-1] = b;
		b.setFont(bf);
		this.add(b);
	}

	public void action(final Client f) {
		for (JButton b : bs) {
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton source = (JButton) e.getSource();
					if ("←".equals(source.getText())) {
						f.setTitle(f.getTitle().length() > 0 ? f.getTitle()
								.substring(0, f.getTitle().length() - 1) : "");
					} else {
						f.setTitle(f.getTitle() + source.getText());
					}
					f.getStars(f.getTitle());
					f.getSongs(f.getTitle());
				}
			});
		}
	}
}
