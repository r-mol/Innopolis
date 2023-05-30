#!/bin/bash

countPrimes () {
    printf '\nExecution time for %d numbers and %d threads\n' "$1" "$2" >> ex4.txt
    /usr/bin/time -ap -o ex4.txt ./ex4 $1 $2
}

printf "" > ex4.txt

gcc ex4.c -pthread -o ex4

countPrimes 10000000 1
countPrimes 10000000 2
countPrimes 10000000 4
countPrimes 10000000 10
countPrimes 10000000 100
