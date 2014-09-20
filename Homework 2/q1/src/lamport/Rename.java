package lamport;

import java.util.concurrent.ConcurrentHashMap;

import lamport.Splitter.Direction;

public class Rename {

	private int m_range = 0;

	private Splitter[][] m_splitters;
	private ConcurrentHashMap<Integer, Splitter> m_splitterMap = new ConcurrentHashMap<Integer, Splitter>();

	public class Result {
		public int m_down;
		public int m_right;
		public int m_id;

		public Result(int down, int right, int id) {
			m_down = down;
			m_right = right;
			m_id = id;
		}
	}

	public Rename(int m) {
		m_range = m;
		m_splitters = new Splitter[m_range][m_range];

		int maxX = m_range;

		for (int y = 0; y < m_range; y++) {
			for (int x = 0; x < maxX; x++) {
				m_splitters[x][y] = new Splitter();
			}
			maxX--;
		}
	}

	public Result reserveId() {

		boolean search = true;
		int down = 0, right = 0, id = -1;

		while (search) {

			Splitter splitter = null;

			try {
				splitter = m_splitters[right][down];
			} catch (Exception e) {
				return null;
			}

			if (splitter == null) {
				return null;
			}
			
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
				// Arithmetic series
				if (down < right | down == right) {
					int _right = right + 1;
					id = (_right + down) * (_right + 1 + down) / 2 - down;
				} else {
					id = (down + right) * (down + right + 1) / 2 + right;
				}
				
				// id = m_range + right - (down * (down - 1) / 2);
				
				m_splitterMap.put(id, splitter);
				search = false;
				
				break;
			}
			}
		}

		return new Result(down, right, id);
	}

	public void releaseId(int id) {
		if (m_splitterMap.containsKey(id)) {
			m_splitterMap.remove(id).release();
		}
	}
}
