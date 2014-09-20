import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Bathroom extends JFrame {

	// Note: Crap code but does the job

	private JLabel m_maleSignal;
	private JLabel m_maleCount;
	private JLabel m_femaleSignal;
	private JLabel m_femaleCount;

	private volatile int m_male = 0;
	private volatile int m_female = 0;

	public Bathroom() {
		m_maleSignal = new JLabel("MALE", JLabel.CENTER);
		m_maleCount = new JLabel(Integer.toString(m_male), JLabel.CENTER);
		m_femaleSignal = new JLabel("FEMALE", JLabel.CENTER);
		m_femaleCount = new JLabel(Integer.toString(m_female), JLabel.CENTER);
	}

	public void createAndShowGUI() {

		// Set up male
		JPanel left = new JPanel();

		left.setBackground(new Color(0x222222));
		left.setLayout(null);
		left.setSize(new Dimension(440, 240));

		m_maleSignal.setBounds(20, 20, 200, 200);
		m_maleSignal.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		m_maleSignal.setBackground(new Color(0x555555));
		m_maleSignal.setForeground(Color.WHITE);
		m_maleSignal.setVerticalAlignment(SwingConstants.CENTER);
		m_maleSignal.setOpaque(true);

		m_maleCount.setBounds(240, 20, 200, 200);
		m_maleCount.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		m_maleCount.setBackground(new Color(0x555555));
		m_maleCount.setForeground(Color.WHITE);
		m_maleCount.setOpaque(true);

		left.add(m_maleSignal);
		left.add(m_maleCount);

		// Set up female
		JPanel right = new JPanel();

		right.setBackground(new Color(0x444444));
		right.setLayout(null);
		right.setSize(new Dimension(440, 240));

		m_femaleSignal.setBounds(20, 20, 200, 200);
		m_femaleSignal.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		m_femaleSignal.setBackground(new Color(0x555555));
		m_femaleSignal.setForeground(Color.WHITE);
		m_femaleSignal.setVerticalAlignment(SwingConstants.CENTER);
		m_femaleSignal.setOpaque(true);

		m_femaleCount.setBounds(240, 20, 200, 200);
		m_femaleCount.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		m_femaleCount.setBackground(new Color(0x555555));
		m_femaleCount.setForeground(Color.WHITE);
		m_femaleCount.setOpaque(true);

		right.add(m_femaleSignal);
		right.add(m_femaleCount);

		// Set up frame

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(920, 270);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(1, 2));

		add(left);
		add(right);

		setVisible(true);
	}

	public void onEnter(GenderThread.Gender gender) {
		if (gender == GenderThread.Gender.MALE) {
			m_maleSignal.setBackground(new Color(0xA88000));
			m_maleCount.setText(Integer.toString(++m_male));
		} else {
			m_femaleSignal.setBackground(new Color(0xA88000));
			m_femaleCount.setText(Integer.toString(++m_female));
		}
	}

	public void onLeave(GenderThread.Gender gender) {
		if (gender == GenderThread.Gender.MALE) {
			if (--m_male == 0) {
				m_maleSignal.setBackground(new Color(0x555555));
			}
			m_maleCount.setText(Integer.toString(m_male));
		} else {
			if (--m_female == 0) {
				m_femaleSignal.setBackground(new Color(0x555555));
			}
			m_femaleCount.setText(Integer.toString(m_female));
		}

	}
}
