#!/usr/bin/env bash

# TODO: extract using id
# INFO=$(id "$(whoami)")
# USER_ID=$(cat $INFO | cut -d' ' -f1)
# GROUP_ID=$(cat $INFO | cut -d'=' -f2 | cut -d'(' -f1)

# These can not be changed
export NIX_USERNAME=yoda
export NIX_GROUPNAME=users

# These can be changed
export NIX_USER_UID=1000
export NIX_USER_GID=1000
