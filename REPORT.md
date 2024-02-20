# Report For Project

This is a report of the calculator example done by group 8.

The group chose to calculate the average time it took to calculate the set of operations on
both single thread and multithread server. The operations are as follows:

A, Addition
S, Subtraction
M, Multiplication
D, Division


Here are the following results the group got by simulating 10 different clients for
both single and multithreading server:

Single-threaded:

Average time: 44ms

Multi-threaded:

Average time: 40ms

The difference between these times are insanely minimal with a difference of ...
But multithreading wins the race by a small margin. This is because on a multi-threaded 
server, the job of calculating each operation is done simultaneously on all threads.

# Video for the program

Video will be handed in as a seperate file.