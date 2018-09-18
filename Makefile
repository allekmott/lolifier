
CC=gcc
CFLAGS=-Wall -Wextra

EXE=lolifier

.PHONY: all
all:
	$(CC) $(CFLAGS) -o $(EXE) lolifier.c

.PHONY: clean
clean:
	rm -f $(EXE)
	rm -f *.o
	rm -f *.d
	rm -f lol.txt

