#!/usr/bin/env bash

set -euf -o pipefail
THISDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

OUTPUT=${1:-"$THISDIR"/passwd}

cat /etc/passwd > "$OUTPUT"
