package vod.client;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ListSelectionListener;

public class Result extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8461964883766756225L;
	private JList list;

	public Result() {
		super();
		this.setLayout(new ScrollPaneLayout());
		list = new JList();

		Font f = new Font("System", Font.PLAIN, 30);
		list.setFont(f);
		this.getViewport().add(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void setListData(String[] data) {
		this.list.setListData(data);
	}

	public void addListSelectionListener(ListSelectionListener e) {
		this.list.addListSelectionListener(e);
	}
	
	public int getSelectedIndex() {
		return this.list.getSelectedIndex();
	}
}
