#!/bin/bash

chmod a-x _ex3.txt
ls -lah _ex3.txt >> ex3.txt

chmod uo+rwx _ex3.txt
ls -lah _ex3.txt >> ex3.txt

chmod -R g=u _ex3.txt
ls -lah _ex3.txt >> ex3.txt

echo >> ex3.txt
echo "1. 660 -- 110 => Owner has read & write permissions" >> ex3.txt
echo "       |- 110 => Group has read & write permissions" >> ex3.txt
echo "       |- 000 => Other does not have any permissions" >> ex3.txt

echo "2. 775 -- 111 => Owner has read & write & execute permissions" >> ex3.txt
echo "       |- 111 => Group has read & write & execute permissions" >> ex3.txt
echo "       |- 101 => Other have read & execute permissions" >> ex3.txt

echo "3. 777 -- 111 => Owner has read & write & execute permissions" >> ex3.txt
echo "       |- 111 => Group has read & write & execute permissions" >> ex3.txt
echo "       |- 111 => Other have read & write & execute permissions" >> ex3.txt

