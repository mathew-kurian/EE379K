#include <omp.h>

#include <iostream>
#include <cstdlib>
#include <cstring>
#include <stdlib.h>
#include <time.h>

int inline imax(int x, int y){
  return x > y ? x : y;
}

double MonteCarloPi(int s)
{
  int valid = 0, samples = 0, threads = 0;
  double x, y;

  #pragma omp parallel private(x, y,samples, threads) reduction(+:valid) 
  {
    threads = omp_get_num_threads();
    samples = imax(s / threads, 1);

    srand(int(time(NULL)) ^ threads);
    
    #pragma omp for
    for (int i = 0; i < samples; ++i)
    {
      x = ((double)rand()) / ((double)RAND_MAX);
      y = ((double)rand()) / ((double)RAND_MAX);

      if ((x * x + y * y) <= 1.0){
        ++valid;
      }
    }
  }

  return (((double)valid) / ((double)s)) * 4.0;
}

int main(int argc, char* argv[])
{
  using namespace std;

  if(argc < 2){
    cout << "Error: Arguments length < 1" << endl;
    return EXIT_FAILURE;
  }

  cout << MonteCarloPi(atoi(argv[1])) << endl;

  return EXIT_SUCCESS;
}