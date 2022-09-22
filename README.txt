----------------
Task requirement
----------------

Create an executable .jar file. You can use Java version up to 15 inclusive.
Please, be aware that if you use any Java version older than 15,
some classes which were included into JRE in previous versions (like javafx) are not available in JRE 15.
In this case the solution will fail with an exception on the first test case.

Read the input data from the file named input.txt which will be located in the current working directory.
Please, access the file by name, e.g. "input.txt" not "/home/user/input.txt" so it will be resolved
to the current working directory automatically.

As a result of execution, your .jar file should create a file containing the output data,
named output.txt in the same folder (working directory).

Performance of your solution matters! Note that the memory limit for the tests is set to 128 MB (-Xmx128M).

The solution .jar file must contain the source code.

The solution .jar file size must be less than 10 MB.
You are free to include any libraries, just make sure their usage is justified.

After you have solved the task, send it via the form in the section below (please do not publish it anywhere else).
Your submission will be automatically tested, so it is required that you strictly follow the input/output data format.
If your .jar file is not executable, or does not contain source code, it will not be accepted.

We value candidates who try to think over the solution before sending it as finished -
please, don't send a lot of similar solutions with random changes.

------------------------
Input/Output Data Format
------------------------

In order to complete the task, you will have to create an implementation of an order book.
It will be checked for correctness in the following way:
Initially, you have an empty order book. Then, updates to the book are applied,
and the book should respond to queries as described under the "Input/Output data format" section below.

Each line in the file can be one of the following:

Updates to the limit order book in the following format:
u,<price>,<size>,bid - set bid size at <price> to <size>
u,<price>,<size>,ask - set ask size at <price> to <size>

Queries in the following format:
q,best_bid - print best bid price and size
q,best_ask - print best ask price and size
q,size,<price> - print size at specified price (bid, ask or spread).

And market orders in the following format:
o,buy,<size> - removes <size> shares out of asks, most cheap ones.
o,sell,<size> - removes <size> shares out of bids, most expensive ones

In case of a buy order this is similar to going to a market
(assuming that you want to buy <size> similar items there,
and that all instances have identical quality, so price is the only factor) -
you buy <size> units at the cheapest price available.

Queries, market orders, and limit order book updates are in arbitrary sequence.
Each line in the file is either one of the three and ends with a UNIX newline character - \n.

Input values range:
Price - 1...10^9
Size - 0...10^8

Example of input file:
u,9,1,bid
u,11,5,ask
q,best_bid
u,10,2,bid
q,best_bid
o,sell,1
q,size,10
u,9,0,bid
u,11,0,ask

Example of output file (for this input file):
9,1
10,2
1

--------------------------
How to Check Your Solution
--------------------------

Before sending the solution, it is recommended to check how it works on your side. Please, follow the steps below:
Create a file input.txt with the contents described in the section Input/Output data format .
Put the input.txt into a folder with your_solution.jar file (the name of the jar file doesn't matter).
Open the command line in the same folder. Run java -Xmx128M -jar your_solution.jar.
Use your file name instead of "your_solution".
Assuming your solution works correctly, output.txt file should appear in the same folder,
with the contents identical to what is shown in the Input/Output data format section.