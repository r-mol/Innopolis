#!/bin/bash

countPrimes () {
    printf '\nExecution time for %d numbers and %d threads\n' "$1" "$2" >> ex3.txt
    /usr/bin/time -ap -o ex3.txt ./ex3 $1 $2
}

printf "" > ex3.txt

gcc ex3.c -pthread -o ex3

countPrimes 10000000 1
countPrimes 10000000 2
countPrimes 10000000 4
countPrimes 10000000 10
countPrimes 10000000 100
