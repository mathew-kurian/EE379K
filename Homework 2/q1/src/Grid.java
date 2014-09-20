import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Grid extends JFrame {

	private static int PADDING = 10;
	private static int DIMENSION = 50;

	private int m_range;
	private JPanel[][] m_grids;

	public Grid(int n) {
		m_range = n;
		m_grids = new JPanel[m_range][m_range];
	}

	public void createAndShowGui() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Rename Grid");
		getContentPane().setBackground(new Color(0x333333));

		int maxX = m_range;
		int posX = PADDING;
		int posY = PADDING;
		int side = (m_range + 1) * (PADDING + DIMENSION) + PADDING;

		for (int y = 0; y < m_range; y++) {
			for (int x = 0; x < maxX; x++) {
				JPanel grid = new JPanel();
				grid.setBackground(new Color(0x555555));
				grid.setBorder(BorderFactory.createLineBorder(new Color(
						0x565656)));
				grid.setBounds(posX, posY, DIMENSION, DIMENSION);
				this.add(grid);
				m_grids[x][y] = grid;

				posX += PADDING + DIMENSION;
			}
			maxX--;
			posX = PADDING;
			posY += DIMENSION + PADDING;
		}

		setSize(side, side);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void dirtyGrid(int down, int right) {
		JPanel panel = m_grids[down][right];
		panel.setBackground(Color.YELLOW);
		panel.repaint();
		repaint();
	}

	public void cleanGrid(int down, int right) {
		JPanel panel = m_grids[down][right];
		panel.setBackground(new Color(0x565656));
		panel.repaint();
		repaint();
	}
}
