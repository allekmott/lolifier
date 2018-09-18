
CC=gcc
CFLAGS=-Wall

EXE=lolifier

.PHONY: all
all:
	$(CC) -o $(EXE) lolifier.c

.PHONY: clean
clean:
	rm -f $(EXE)
	rm -f *.o
	rm -f *.d
	rm -f lol.txt

