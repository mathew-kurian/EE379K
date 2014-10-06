#include <omp.h>

#include <fstream>
#include <iostream>
#include <string>
#include <sstream>

#define DESTROY(x)	if(x) delete x;

struct matrix {
	
	int ** rows = NULL;
	int m = 0;
	int n = 0;

public: 
	
	matrix(int ** _rows, int m, int n) :
		rows(_rows),
		m(m),
		n(n)
	{
	}

	~matrix(){

		if (m > 0 && n > 0 && rows)
		{
			for (int mm = 0; mm < m; mm++){\
				if (rows[mm]){
					delete rows[mm];
				}
			}

			delete rows;
		}
	}

	static matrix * fromfile(char * file)
	{
		using namespace std;

		ifstream input(file);
		string line;
		int m = 0, n = 0, i = -1;
		bool dim = true;
		int ** rows;

		for (line; getline(input, line);)
		{
			stringstream lineStream(line);
			string token;

			// read size
			if (dim)
			{
				lineStream >> m;
				lineStream >> n;

				if (m <= 0 || n <= 0)
				{
					return NULL;
				}

				rows = new int*[m];
				dim = false;

				memset(rows, 0, m);

				continue;
			}
			else
			{
				if (++i > m)
				{
					return NULL;
				}

				int * row = new int[n];

				memset(row, 0, n);

				for (int x = 0; x < n; x++){
					lineStream >> row[x];
				}

				rows[i] = row;
			}
		}

		return new matrix(rows, m, n);
	}

	void print()
	{
		using namespace std;

		int * row;

		for (int mm = 0; mm < m; mm++){

			row = rows[mm];
			
			for (int nn = 0; nn < n; ++nn){
				cout << row[nn] << ' ';
			}

			cout << '\n';
		}
	}
};

int main(int argc, char* argv[])
{
	using namespace std;

	// TODO
	// 1. Read from arguments with error check
	// 2. Comment out std:getchar before submission

	int retcode;
	int ** solrows = NULL;
	int n, m, maxops;
	int threads;
	string lasterror;
	matrix * mtx1 = NULL, *mtx2 = NULL, *sol = NULL;

	/*
	if (argc < 3){
		lasterror = "Error: Arguments length < 3";
		goto failure;
	}

	threads = atoi(argv[2]");;

	if(threads < 1){
		lasterror = "Error: Thread count less than 1";
		goto failure;
	}

	mtx1 = matrix::fromfile(argv[0]);
	mtx2 = matrix::fromfile(argv[1]);
	*/

	threads = atoi("10");
	mtx1 = matrix::fromfile("input1.txt");
	mtx2 = matrix::fromfile("input2.txt");

	if (!mtx1 || !mtx2){
		lasterror = "Error: Read error";
		goto failure;
	}

	if ((mtx1->m != mtx2->n) || (mtx1->n != mtx2->m)){
		lasterror = "Error: Matrix cannot be multiplied";
		goto failure;
	}

	m = mtx1->m;
	n = mtx1->n;
	maxops = m * m;

	solrows = new int *[m];
	memset(solrows, 0, m);

	for (int x = 0; x < m; x++){
		int * solrow = new int[m];
		memset(solrow, 0, m);
		solrows[x] = solrow;
	}

	sol = new matrix(solrows, m, m);

	omp_set_num_threads(threads);

#pragma omp parallel num_threads(threads)
	{	
		int op = omp_get_thread_num();

		while (op < maxops){

			int cprod = 0;
			int * row = mtx1->rows[op / m];
			int n = mtx1->n;

			for (int i = 0; i < n; i++){
				cprod += row[i] * mtx2->rows[i][op % m];
			}

			solrows[op / m][op % m] = cprod;

			op += threads;
		}
	}
	
	sol->print();
	
	goto ok;
	
// Exits

failure:

	retcode = EXIT_FAILURE;
	cout << lasterror;
	goto finish;

ok:

	// set exit code
	retcode = EXIT_SUCCESS;

finish: 

	DESTROY(mtx1);
	DESTROY(mtx2);
	DESTROY(sol);

	// Important !!!!!!!!!
	// Comment out the next line on submission
	std::getchar();

	return retcode;
}
