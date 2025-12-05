#!/bin/sh
set -e

ENV_FILE="/app/.env"

# Load environment variables from .env file
if [ -f "$ENV_FILE" ]; then
    export $(grep -v '^#' "$ENV_FILE" | xargs)
fi

# Default APP_ENV to production if not set
APP_ENV=${APP_ENV:-production}

echo "Starting ThingsBoard API (ENV: $APP_ENV)"

# Generate JWT_SECRET if not set or is placeholder
if [ -f "$ENV_FILE" ]; then
    CURRENT_SECRET=$(grep "^JWT_SECRET=" "$ENV_FILE" | cut -d'=' -f2)

    if [ -z "$CURRENT_SECRET" ] || [ "$CURRENT_SECRET" = "CHANGE_ME_GENERATE_SECURE_SECRET" ]; then
        NEW_SECRET=$(openssl rand -base64 32)
        sed -i "s|^JWT_SECRET=.*|JWT_SECRET=${NEW_SECRET}|" "$ENV_FILE"
        echo "JWT_SECRET generated"
    fi
fi

# Run database migrations (all environments)
echo "Running database migrations..."
java -cp "/app/BOOT-INF/lib/*:/app/BOOT-INF/classes" \
    $JAVA_OPTS \
    org.thingsboard.server.install.ThingsboardInstallApplication \
    --install.upgrade=true \
    --install.upgrade.cluster=true || true
echo "Migrations completed"

# Run seeders (non-production only)
if [ "$APP_ENV" != "production" ]; then
    echo "Running database seeders (non-production)..."
    java -cp "/app/BOOT-INF/lib/*:/app/BOOT-INF/classes" \
        $JAVA_OPTS \
        org.thingsboard.server.install.ThingsboardInstallApplication \
        --install.data.demo=true || true
    echo "Seeders completed"
fi

echo "Starting application..."
exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher "$@"
