#!/bin/sh
set -e

MARKER_FILE="/app/node_modules/.deps-marker"
CURRENT_OS=$(uname -s)
LOCK_HASH=$(md5sum /app/yarn.lock 2>/dev/null | cut -d' ' -f1 || echo "none")
MARKER_VALUE="${CURRENT_OS}:${LOCK_HASH}"

install_deps() {
    echo "Installing dependencies..."
    yarn install
    echo "$MARKER_VALUE" > "$MARKER_FILE"
    echo "Dependencies installed (OS: $CURRENT_OS)"
}

NEED_INSTALL=false

if [ ! -d "/app/node_modules" ]; then
    echo "node_modules not found"
    NEED_INSTALL=true
elif [ ! -f "$MARKER_FILE" ]; then
    echo "Marker not found, reinstalling"
    NEED_INSTALL=true
elif [ "$(cat $MARKER_FILE)" != "$MARKER_VALUE" ]; then
    OLD_MARKER=$(cat $MARKER_FILE)
    echo "Dependencies changed (was: $OLD_MARKER, now: $MARKER_VALUE)"
    rm -rf /app/node_modules
    NEED_INSTALL=true
fi

if [ "$NEED_INSTALL" = true ]; then
    install_deps
else
    echo "Dependencies up to date"
fi

exec "$@"
