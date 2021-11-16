#!/usr/bin/env bash

set -euf -o pipefail
THISDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

USER=${1:-}
OUTPUT=${1:-"$THISDIR"/passwd}

id "$USER" >> "$OUTPUT"
