import lamport.Rename;

public abstract class AutoReleaseThread extends Thread {

	private Rename m_rename;
	private String m_name;

	public AutoReleaseThread(Rename rename, int n) {
		super();
		m_rename = rename;
		m_name = Integer.toString(n);
	}

	public void run() {

		while (true) {

			Rename.Result res = m_rename.reserveId();

			while (res == null) {

				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				res = m_rename.reserveId();
			}

			System.out.printf("Thread #%s renamed to %d\n", m_name, res.m_id);

			onReserve(res);

			try {
				Thread.sleep((long) (Math.random() * 15));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			m_rename.releaseId(res.m_down, res.m_right);

			onRelease(res);
		}
	}

	public abstract void onReserve(Rename.Result result);

	public abstract void onRelease(Rename.Result result);
}
