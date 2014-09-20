import javax.swing.SwingUtilities;

import lamport.Rename;
import lamport.Rename.Result;

public class Driver {

	/**
	 * Issues: Not calling "DOWN"
	 */
	public static void main(String[] args) {
		
		final Rename rename = new Rename(5);
		final Grid grid = new Grid(5);
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				grid.createAndShowGui();
				
				for(int i = 0; i < 10; i++){
					new AutoReleaseThread(rename, i){
						@Override
						public void onReserve(Result result) {
							grid.dirtyGrid(result.m_down, result.m_right);
							
						}
						@Override
						public void onRelease(Result result) {
							grid.cleanGrid(result.m_down, result.m_right);
							
						}
					}.start();
				}
			}
		});
		
	}

}
