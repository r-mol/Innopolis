==========================
Least Square Approximation
==========================

Write a C++ Program to compose a least square approximation for agiven data set.
We have:

Y=A \cdot X
 

For each sample i of your data you have (n+1) real constants:

.. math:: 
     <img src="https://render.githubusercontent.com/render/math?math=a_{i0}, a_{i1}, a_{i}_{2}, \ldots, a_{i}_{n}\right)$, where ai0 = 1. 



.. math:: 
  \y_{i}=a_{i 0} \cdot x_{i 0}+a_{i 1} \cdot x_{i 1}+a_{i 2} \cdot x_{i 2}+\ldots+a_{i n} \cdot x_{i n}


Input
*****

First line contains space-separated integers, n (number of features you have) and m (number of samples in the dataset). 
Each of the later m lines have (n+1) space-separated values. First n values represents your data features
.. math:: 
  $\left(a_{i}_{1}, a_{i}_{2}, \ldots, a_{i}_{n}\right)$
 
and the last value is the value of yi. 

Constraints
***********
.. code:: text

      1 <= n <=10


      6 <= m <= 100


      0 <= i < m


      0 <= xij <= 1


      0 <= yi <= 10^6
 
Output
******

The output contains:

1. the matrix A itself after the line "A:"

2. the matrix b after the line "b:"

3. the matrix (A_T*A) after the line "A_T*A:"

4. the matrix(A_T*A)^-1 after the line "(A_T*A)_-1:"

5. the matrix(A_T*A)^-1*A_T after the line "(A_T*A)_-1*A_T:"

6. the answer (matrix (A_T*A)^-1*A_T*b) after the line "x:"

Dont't forget to set the floatfield format flag for the str stream to fixed and to set the precision to 2 decimals. Something like this:

.. code:: text

     output << std::fixed << std::setprecision(2);
     
Example
*********
input.txt

.. code:: text

      2 6
      0.62 0.21 127.79
      0.34 0.07 97.18
      0.85 0.16 194.65
      0.99 0.41 162.6
      0.27 0.37 141.77
      0.18 0.89 109.85
 
output.txt

.. code:: text

     A:
     1.00 0.62 0.21 
     1.00 0.34 0.07 
     1.00 0.85 0.16 
     1.00 0.99 0.41 
     1.00 0.27 0.37 
     1.00 0.18 0.89

     b:
     127.79 
     97.18 
     194.65 
     162.60 
     141.77 
     109.85

     A_T*A:
     6.00 3.25 2.11 
     3.25 2.31 0.96 
     2.11 0.96 1.17

     (A_T*A)_-1:
     1.49 -1.49 -1.47 
     -1.49 2.15 0.93 
     -1.47 0.93 2.73 

     (A_T*A)_-1*A_T:
     0.26 0.88 -0.01 -0.59 0.54 -0.08 
     0.04 -0.70 0.48 1.02 -0.57 -0.27 
     -0.31 -0.96 -0.24 0.58 -0.20 1.13

     x:
     88.75 
     85.92 
     10.46 
