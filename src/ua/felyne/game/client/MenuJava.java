package ua.felyne.game.client;

import ua.felyne.game.shared.SpriteSheet;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class MenuJava extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JButton create;
	private final JButton join;
	private final JComboBox<Integer> players;
	
	public MenuJava() {
		super("Game");
		Container c = getContentPane();
		GridLayout layout = new GridLayout();
		layout.setColumns( 1 );
		layout.setRows( 3 );
		setPreferredSize(new java.awt.Dimension(SpriteSheet.SCREEN_WIDTH, SpriteSheet.SCREEN_HEIGHT));
		setSize(SpriteSheet.SCREEN_WIDTH, SpriteSheet.SCREEN_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		
		create = new JButton("Create");
		join = new JButton("Join");
		players = new JComboBox<Integer>(new Integer[] {1, 2, 3, 4});
		
		c.setLayout(layout);
		c.add(create);
		c.add(join);
		c.add(players);
		
		addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	
	public void addListener(ActionListener al) {

		create.addActionListener(al);
		join.addActionListener(al);
	}
	
	public JButton getCreate() {
		return create;
	}
	
	public JButton getJoin() {
		return join;
	}
	
	public int getPlayers() {
		return (Integer)players.getSelectedItem();
	}
}
