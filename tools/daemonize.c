/* -*- mode: c; coding: utf-8; fill-column: 80; -*-
 *
 *,----------------------------------------------------------------------
 *| daemonize.c
 *| Created by Balakrishnan Chandrasekaran on 2015-01-29 02:13 -0500.
 *'----------------------------------------------------------------------
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
  if (argc != 2) {
    const char *prog = argv[0];
    fprintf(stderr, "Usage: %s <launcher-script>\n", prog);
    exit(1);
  }
  const char *launcher = argv[1];

  if (fork() == 0) {
    if (setsid() == -1) {
      perror("setsid");
      exit(1);
    }
    
    if (chdir("/") == -1) {
      perror("chdir");
      exit(1);
    }
    
    if (fork() == 0) {
      if (execl("/", launcher, NULL) == -1) {
        perror("'execl' failed!");
        exit(1);
      }
      
      exit(0);
    }
    
    exit(0);
  }
  
  exit(0);
}
