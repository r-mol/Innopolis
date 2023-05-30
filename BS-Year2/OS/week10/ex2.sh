#!/bin/bash

link ../week01/file.txt _ex2.txt
cat _ex2.txt >> ex2.txt

eval $(stat -s ../week01/file.txt)
find ../ -inum $st_ino >> ex2.txt

find ./ -inum $st_ino -exec rm {} \;
