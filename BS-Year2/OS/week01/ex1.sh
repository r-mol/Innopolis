#!/bin/bash

ls /usr/bin | grep gcc | sort -n -r | tail -n 5 >  ex1.txt
