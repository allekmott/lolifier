/**
 * @file	lolifier.c
 * @author	Allek Mott <allekmott@gmail.com>
 * @date	17 September 2018
 * @brief	Entry point for lolifier tool
 */

#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#define DEFAULT_BUFFER_SIZE = 1024

static int lolify(const char *file_name, long file_size);

static int is_uint_string(const char *s_int);
static int parse_multiplier(const char *s_multiplier);
static void fill_buffer(char *buffer, size_t n_chars);

static int usage(const char *cmd) {
	printf(
"Usage: %s [options]\n" \
"\n"
"Options:\n"
"-------\n"
"-o  <file>        Write output to <file>\n"
"-u  <unit>        Desired size unit (default b); b | k[b] | m[b] | g[b]\n"
"-s  <bytes>       Number of units to be written (bytes by default)\n"
"-b  <size>        Buffer size in bytes (>1)\n", cmd);

	return 0;
}

int main(int argc, char *argv[]) {
	const char *file_name;
	long file_size;
	int buffer_size, multiplier;

	char flag;
	extern int optind, optopt;

	const char *cmd = argv[0];

	file_name = "lol.txt";
	file_size = 1048576;	/* 1mb */

	multiplier = 1;

	while ((flag = getopt(argc, argv, "o:u:s:b:h")) != -1) {
		switch (flag) {
			case 'o':
				file_name = optarg;
				break;
			case 'u':
				multiplier = parse_multiplier(optarg);
				if (multiplier == -1) {
					fprintf(stderr, "Invalid unit: %s\n", optarg);
					return EINVAL;
				}
				break;
			case 's':
				if (!is_uint_string(optarg)) {
					fprintf(stderr, "Invalid file size: %s\n", optarg);
					return EINVAL;
				} else {
					file_size = atoi(optarg);
					if (file_size < 1) {
						fprintf(stderr, "Invalid file size: %s\n", optarg);
						return EINVAL;
					}
				}
				break;
			case 'b':
				if (!is_uint_string(optarg)) {
					fprintf(stderr, "Invalid buffer size: %s\n", optarg);
					return EINVAL;
				} else {
					buffer_size = atoi(optarg);
					if (buffer_size < 2) {
						fprintf(stderr, "Invalid buffer size: %s\n", optarg);
						return EINVAL;
					}
				}
				break;
			case 'h': return usage(cmd);
			default:
				fprintf(stderr,
						"\nRun '%s -h' to see a list of valid options\n", cmd);
				return EINVAL;
		}
	}

	argc -= optind;
	argv += optind;

	printf("To write %lu bytes to %s\n", file_size, file_name);
	return lolify(file_name, file_size);
}

static int lolify(const char *file_name, long file_size) {
	return 0;
}

static int is_uint_string(const char *s_int) {
	int i, n_chars;
	char c;

	n_chars = strlen(s_int);

	for (i = 0; i < n_chars; ++i) {
		c = s_int[i];

		if (!(c >= '0' && c <= '9')) {
			return 0;
		}
	}

	return 1;
}

static int parse_multiplier(const char *s_multiplier) {
	int n_multiplier = 1;

	switch (s_multiplier[0]) {
		case 'b': return 1;
		case 'k': return 1024;
		case 'm': return 1048576;
		case 'g': return 1073741824;
		default: return -1;
	}
}

static void fill_buffer(char *buffer, size_t n_chars) {

}
