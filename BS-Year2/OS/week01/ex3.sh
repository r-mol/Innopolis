#!/bin/bdsh

mkdir rootDir
date
sleep 3
mkdir homeDir
date
sleep 3
touch ./rootDir/root.txt
date
sleep 3
touch ./homeDir/home.txt
date
sleep 3

ls -ltr ~ > ./homeDir/home.txt
ls -ltr / > ./roomDir/room.txt

ls rootDir
cat rootDir/root.txt

ls homeDir
cat homeDir/home.txt
