#!/bin/bash

add_command()
{
cp --verbose --parents "$1" "$2"

for library in $(ldd "$1" | cut -d '>' -f 2 | awk '{print $1}')
do
        [ -f "${library}" ] && cp --verbose --parents "${library}" "$2"
done
}

echo "Roman" > ./lofsdisk/file1
echo "Molochkov" > ./lofsdisk/file2

add_command /bin/bash ./lofsdisk
add_command /bin/ls ./lofsdisk
add_command /bin/cat ./lofsdisk
add_command /bin/echo ./lofsdisk

gcc ./ex2.c -o ./lofsdisk/ex2.out

echo "List the contents of the root directory 1" > ex2.txt
sudo chroot ./lofsdisk ./ex2.out >> ex2.txt

echo "List the contents of the root directory 2" >> ex2.txt
./lofsdisk/ex2.out >> ex2.txt

rm ./lofsdisk/ex2.out
