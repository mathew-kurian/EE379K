Question 1
--------------

LOL

Question 2
--------------

If a read occurs concurrently with a write, the Bakery algorithm would fail. Consider the case of two threads A and B. An example of this is as follows:

```
Thread A                Thread B
--------                --------
Enter CS                Waiting in while-loop           label[A] = 1; label[B] = 2;
Exits CS                OS halts                        label[A] = 0; label[B] = 2;
Lock called                                             label[A] = 1; label[B] = 2;
Update label[A] = 3     Reads wrong label; Enter CS;    label[A] = -1;
Enters CS               Enters CS                       label[A] = 3; label[B] = 2;
```
