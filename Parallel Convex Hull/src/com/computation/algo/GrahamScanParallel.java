package com.computation.algo;

import com.computation.common.ConvexHull;

public class GrahamScanParallel extends ConvexHull{

	public GrahamScanParallel(int points, int width, int height, int threads,
			boolean debug) {
		super(points, width, height, threads, debug);
	}

	@Override
	protected void findHull() 
	{
		
	}
}
