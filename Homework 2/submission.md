Question 1
--------------

LOL

Question 2
--------------

If a read occurs concurrently with a write, the Bakery algorithm should still work properly. The only time that there is a read and write is when threads are looking for the maximum label and assigning themselves with one plus that. During such a case it does not matter if the read is assigned the old value or new value since one will be added to it anyways. More so, if there are two labels of the same order, the Bakery algorithm compensates by letting the thread of higher lexographic order to continue. As a result, the Bakery algorithm will always work for any order order of label even if there are duplicates. 

If a read occurs concurrently with a write, the Bakery algorithm would fail. Consider the case of two threads A and B. An example of this is as follows:

Thread A                Thread B
--------                --------
Enter CS                Waiting in while-loop           label[A] = 1; label[B] = 2;
Exits CS                OS halts                        label[A] = 0; label[B] = 2;
Lock called                                             label[A] = 1; label[B] = 2;
Update label[A] = 3     Reads wrong label; Enter CS;    label[A] = -1;
Enters CS               Enters CS                       label[A] = 3; label[B] = 2;

Question 3
---------------

The chart in this directory. 

