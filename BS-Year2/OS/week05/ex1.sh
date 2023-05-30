#!/bin/bash

n=$1

gcc ./publisher.c -o publisher
gcc ./subscriber.c -o subscriber

i=1

while [[ $i -le n ]] ; do
    gnome-terminal gnome-terminal -- ./subscriber $n $i
    ((i += 1))
done

gnome-terminal gnome-terminal -- ./publisher $n
