#!/bin/bash

gcc ./ex3.c -o ex3
./ex3 3 &

echo "Enter a number of subscribers:"
read n

i=1
while [[ $i -le n ]] ; do
   tree .
  sleep $i
  (( i += 1 ))
done