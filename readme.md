Project done by Kaushik Natarajan (kxn180028)


This is a java file implementation of the HS Algorithm for leader election.

The main class being used is threadings. 
It first accepts the number of processes n and creates an array of threads. 
There are n threads in the system apart from the master thread on which the program runs. All these n threads are synchronized. 

The thread for each preocess is implemented by the threads class. 

The execute class contains the process for finding the leader of the ring. 

Packages used : 

java.util
java.Math

Assumption : No two processes are identical and all uid's are unique

Steps for running : 

1. Compile the main file by executing threadings.java
2. Run the threadings class that the compilation produced. 
3. Give the number of processes and their uids as input.