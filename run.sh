#!/bin/sh

# run.sh
# Created: 23 September 2013
# Author: Allek Mott

clear

cd bin

if [ $# -gt 0 ]; then
	java com.loop404.lolifier.Lolifier $1
fi

java com.loop404.lolifier.Lolifier