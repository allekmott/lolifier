/**
 * @file	lolifier.c
 * @author	Allek Mott <allekmott@gmail.com>
 * @date	17 September 2018
 * @brief	Entry point for lolifier tool
 */

#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#define DEFAULT_BUFFER_SIZE 1024

static size_t lolify(int fd, size_t file_size, size_t buffer_size);

static size_t parse_uint(const char *s_int);
static float parse_float(const char *s_float);
static unsigned int parse_multiplier(const char *s_multiplier);
static char *generate_buffer(size_t size);

static const int DEFAULT_FILE_FLAGS =
		O_WRONLY	/* write only */
		| O_CREAT	/* create if doesn't exist */
		| O_TRUNC;	/* erase existing contents upon open */

static const mode_t DEFAULT_FILE_MODE =
		S_IRUSR		/* read for user */
		| S_IWUSR	/* write for user */
		| S_IRGRP	/* read for group */
		| S_IWGRP	/* write for group */
		| S_IROTH;	/* read for other users */

static int usage(const char *cmd) {
	printf(
"Usage: %s [options]\n" \
"\n"
"Options:\n"
"-------\n"
"-o <file>   Write output to <file>. If not set, will dump to stdout\n"
"-u <unit>   Desired size unit (default b); b | k[b] | m[b] | g[b]\n"
"-s <bytes>  Number of units to be written (bytes by default) (>1)\n"
"-b <size>   Buffer size in bytes (>1)\n", cmd);

	return 0;
}

int main(int argc, char *argv[]) {
	int fd;
	const char *file_name;

	size_t buffer_size;
	int multiplier;
	float n_units;
	size_t file_size;
	size_t bytes_written;

	char flag;
	extern int optind, optopt;

	const char *cmd = argv[0];

	file_name = NULL;
	n_units = -1.0f;

	buffer_size = DEFAULT_BUFFER_SIZE;
	multiplier = 1;

	while ((flag = getopt(argc, argv, "o:u:s:b:h")) != -1) {
		switch (flag) {
			case 'o':
				file_name = optarg;
				break;
			case 'u':
				multiplier = parse_multiplier(optarg);
				if (multiplier == 0) {
					fprintf(stderr, "Invalid unit: %s\n", optarg);
					return EINVAL;
				}
				break;
			case 's':
				n_units = parse_float(optarg);
				if (n_units <= 0.0f) {
					fprintf(stderr, "Invalid size: %s\n", optarg);
					return EINVAL;
				}
				break;
			case 'b':
				buffer_size = parse_uint(optarg);
				if (buffer_size < 2) {
					fprintf(stderr, "Invalid buffer size: %s\n", optarg);
					return EINVAL;
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

	if (n_units == -1.0f) {
		fprintf(stderr, "Size must be specified with the -s parameter\n");
		fprintf(stderr, "\nRun '%s -h' for more information\n", cmd);
		return EINVAL;
	}

	file_size = n_units * 1e6;
	file_size *= multiplier;
	file_size /= 1e6;

	if (file_size == 0) {
		fprintf(stderr, "Size must be larger than 0 bytes\n");
		return EINVAL;
	}

	if (file_name == NULL) {
		/* write to stdout by default */
		fd = fileno(stdout);
	} else {
		printf("To write %lu bytes to %s\n", file_size, file_name);

		fd = open(file_name, DEFAULT_FILE_FLAGS, DEFAULT_FILE_MODE);
		if (fd < 0) {
			perror("Unable to open file");
			return errno;
		}
	}

	bytes_written = lolify(fd, file_size, buffer_size);
	if (bytes_written == 0) {
		perror("Unable to lolify");
		return errno;
	}

	return 0;
}

static size_t lolify(int fd, size_t file_size, size_t buffer_size) {
	int res;
	size_t total_written, written, to_write, offset;
	char *buffer;

	res = 0;

	buffer = generate_buffer(buffer_size);
	if (buffer == NULL) {
		goto exit_error;
	}

	total_written = written = 0;
	while (total_written < file_size) {
		offset = (total_written % 2 == 0) ? 0 : 1;
		to_write = file_size - total_written;

		if (to_write > buffer_size) {
			to_write = buffer_size - offset;
		}

		written = write(fd, (buffer + offset), to_write);
		if (written < 0) {
			goto exit_error;
		}

		total_written += written;
	}

	close(fd);
	return total_written;

exit_error:
	close(fd);
	return 0;
}

static size_t parse_uint(const char *s_int) {
	int n_int;

	n_int = strtoul(s_int, NULL, 0);
	if (errno == EINVAL) {
		return -1;
	}

	return n_int;
}

static float parse_float(const char *s_float) {
	float n_float;

	n_float = strtof(s_float, NULL);
	if (errno == EINVAL) {
		return 0.0f;
	}

	return n_float;
}

static unsigned int parse_multiplier(const char *s_multiplier) {
	switch (s_multiplier[0]) {
		case 'b': return 1;
		case 'k': return 1024;
		case 'm': return 1048576;
		case 'g': return 1073741824;
		default: return 0;
	}
}

static char *generate_buffer(size_t size) {
	char *buffer;
	unsigned int i;

	buffer = calloc(size, sizeof(char));
	if (buffer == NULL) {
		return NULL;
	}

	for (i = 0; i < size; ++i) {
		buffer[i] = (i % 2 == 0) ? 'l' : 'o';
	}

	return buffer;
}
