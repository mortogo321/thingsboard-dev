# ThingsBoard Web UI - Multi-stage Dockerfile
# Supports: development (hot reload) and production
# Build context: root directory

# Node 22: highest major officially supported by Angular 18.
# Pinned: unpinned lts/alpine-slim tags float and break reproducible builds.
ARG NODE_IMAGE=node:22-alpine
ARG NGINX_IMAGE=nginx:1.30.5-alpine-slim

# ============================================
# Stage 1: Dependencies
# ============================================
FROM ${NODE_IMAGE} AS deps

WORKDIR /app

COPY web/package.json web/yarn.lock ./

# git required: several deps are git URLs (echarts/flot/ngx-flowchart forks)
RUN apk add --no-cache git && yarn install --frozen-lockfile && yarn cache clean

# ============================================
# Stage 2: Builder
# ============================================
FROM ${NODE_IMAGE} AS builder

WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules
COPY web/ .

# Web config is bake-time via src/environments/* (no runtime .env is read)
RUN yarn build:prod && rm -rf node_modules

# ============================================
# Stage 3: Development Runtime
# ============================================
FROM ${NODE_IMAGE} AS development

WORKDIR /app

# git required: entrypoint runs yarn install, several deps are git URLs
RUN apk add --no-cache git

COPY web/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Source code mounted via docker-compose volumes

EXPOSE 4200

ENTRYPOINT ["/entrypoint.sh"]
CMD ["yarn", "start", "--host", "0.0.0.0", "--poll", "2000"]

# ============================================
# Stage 4: Production Runtime
# ============================================
FROM ${NGINX_IMAGE} AS production

RUN rm -rf /etc/nginx/conf.d/*

COPY docker/nginx/nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist/thingsboard /usr/share/nginx/html

RUN chown -R nginx:nginx /usr/share/nginx/html \
    && chown -R nginx:nginx /var/cache/nginx \
    && chown -R nginx:nginx /var/log/nginx \
    && touch /var/run/nginx.pid \
    && chown -R nginx:nginx /var/run/nginx.pid \
    && chmod -R 755 /usr/share/nginx/html

RUN rm -rf /usr/share/nginx/html/*.map 2>/dev/null || true

USER nginx

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -q --spider http://localhost:80/health || exit 1

CMD ["nginx", "-g", "daemon off;"]
