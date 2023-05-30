#!/bin/bash

fallocate -l 50M lofs.img

PATHHHH=$(sudo losetup --find --show lofs.img)

sudo mkfs.ext4 "$PATHHHH"

mkdir ./lofsdisk

sudo mount "$PATHHHH" ./lofsdisk

sudo chmod o+w ./lofsdisk
