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

static long lolify(int fd, long file_size, int buffer_size);

static int is_uint_string(const char *s_int);
static int parse_multiplier(const char *s_multiplier);
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
"-o <file>  Write output to <file>. If not set, will dump to stdout\n"
"-u <unit>  Desired size unit (default b); b | k[b] | m[b] | g[b]\n"
"-s <bytes  Number of units to be written (bytes by default) (>1)\n"
"-b <size>  Buffer size in bytes (>1)\n", cmd);

	return 0;
}

int main(int argc, char *argv[]) {
	int fd;
	const char *file_name;
	long file_size;
	int buffer_size, multiplier;

	long bytes_written;

	char flag;
	extern int optind, optopt;

	const char *cmd = argv[0];

	file_name = NULL;
	file_size = 0;

	buffer_size = DEFAULT_BUFFER_SIZE;
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
						fprintf(stderr, "Size must be greater than 0\n");
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

	if (file_size < 1) {
		fprintf(stderr, "Size must be specified with the -s parameter\n");
		fprintf(stderr, "\nRun '%s -h' for more information\n", cmd);
		return EINVAL;
	}

	file_size *= multiplier;

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
	if (bytes_written < 0) {
		perror("Unable to lolify");
		return -((int) bytes_written);
	}

	return 0;
}

static long lolify(int fd, long file_size, int buffer_size) {
	int res, written, to_write, offset;
	long total_written;
	char *buffer;

	res = 0;

	buffer = generate_buffer(buffer_size);
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
	return -errno;
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

static char *generate_buffer(size_t size) {
	char *buf;
	int i;

	buf = malloc(size);
	if (buf == NULL) {
		return NULL;
	}

	for (i = 0; i < size; ++i) {
		buf[i] = (i % 2 == 0) ? 'l' : 'o';
	}

	return buf;
}
