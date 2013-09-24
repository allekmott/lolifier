#!/bin/sh

# build.sh
# Created: 23 September 2013
# Author: Allek Mott

# if that bin shiz dont be doin that exist thing, make it
if [ ! -d "bin" ]; then
	echo "Folder 'bin' doesn't exist, creating"
	mkdir bin
fi

cd src
echo "Compiling source"
javac -d ../bin com/loop404/lolifier/*.java