package lamport;

import java.util.concurrent.ConcurrentHashMap;

import lamport.Splitter.Direction;

public class Rename {

	private int m_range = 0;

	private Splitter[][] m_splitters;

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
				splitter = m_splitters[down][right];
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
				id = getId(down, right, m_range);
				search = false;
				
				break;
			}
			}
		}

		return new Result(down, right, id);
	}
	public static int getId(int down, int right, int range){
		
		// return range + right - (down * (down - 1) / 2);
		
//		if (down < right | down == right) {
//			right++;
//			// Arithmetic series
//			return (right + down) * (right + 1 + down) / 2 - down;
//		} else {
//			// Arithmetic series
//			return (down + right) * (down + right + 1) / 2 + right;
//		}
		
		int y = down * (down + 1) / 2 + 1;
		int x = 0;
		
		if(right > 0){
			int dx = down + 1;
			int ex = dx + right;
			int sx = dx * (dx + 1) / 2;
			x = ex * (ex + 1) / 2 - sx;
		}
		
		return x + y;
	}

	public void releaseId(int down, int right) {
		m_splitters[down][right].release();
	}
}
