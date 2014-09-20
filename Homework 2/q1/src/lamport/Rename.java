package lamport;

import java.util.HashMap;

import lamport.Splitter.Direction;

public class Rename {

	private int m_range = 0;

	private Splitter[][] m_splitters;
	private HashMap<Integer, Splitter> m_splitterMap = new HashMap<Integer, Splitter>();

	public Rename(int m) {
		m_range = m;
		m_splitters = new Splitter[m_range][m_range];

		for (int x = 0; x < m_range; x++) {
			for (int y = 0; y < m_range; y++) {
				m_splitters[x][y] = new Splitter();
			}
		}
	}

	public int reserveId() {

		boolean search = true;
		int down = 0, right = 0, id = -1;

		while (search) {

			if(right >= m_range || down >= m_range){
				return -1;
			}
			
			Splitter splitter = m_splitters[right][down];
			Direction direction = splitter.getDirection(Thread.currentThread()
					.getId());

			switch (direction) {
			case DOWN: {
				down++;
				break;
			}
			case RIGHT: {
				right++;
				break;
			}
			case STOP: {
				search = false;
				id = (down + right - (down * (down - 1) / 2));
				m_splitterMap.put(id, splitter);
				break;
			}
			}
		}

		return id;
	}

	public void releaseId(int id) {
		if (m_splitterMap.containsKey(id)) {
			m_splitterMap.remove(id).release();
		}
	}
}
