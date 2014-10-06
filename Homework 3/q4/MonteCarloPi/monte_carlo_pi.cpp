#include <omp.h>

#include <iostream>
#include <cstdlib>
#include <stdlib.h>
#include <time.h>

int inline imax(int x, int y){
	return x > y ? x : y;
}

double MonteCarloPi(int s)
{
	int valid = 0;

	#pragma omp parallel
	{
		int threads = omp_get_num_threads();
		int samples = s / threads;

		srand(int(time(NULL)) ^ threads);
		
		for (int i = 0; i < samples; ++i)
		{
			double x = ((double)rand()) / ((double)RAND_MAX);
			double y = ((double)rand()) / ((double)RAND_MAX);

			if ((x * x + y * y) <= 1.0){
				++valid;
			}
		}
	}

	return (((double)valid) / ((double)s)) * 4.0;
}

int main(int argc, char* argv[])
{
	printf("%f", MonteCarloPi(100000));

	std::getchar();

	return EXIT_SUCCESS;
}