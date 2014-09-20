package q3;

public class CountedThread extends Thread {
	
	private int m_id;
	
	public CountedThread(){
		super();
	}
	
	public CountedThread(Runnable run, int id){
		super(run);
		m_id = id;
	}
	
	@Override
	public long getId(){
		return m_id;
	}
}
