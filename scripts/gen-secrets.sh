#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DEV_DIR="$ROOT_DIR/secrets/dev"
DOCKER_DIR="$ROOT_DIR/secrets/docker"

mkdir -p "$DEV_DIR" "$DOCKER_DIR"

rand_b64() {
  # 32 bytes base64 -> ~43 chars
  openssl rand -base64 32 | tr -d '\n'
}

create_secret() {
  local file="$1"
  if [ -s "$file" ] && [ "${FORCE_REGEN:-0}" != "1" ]; then
    echo "Skip (exists): $file"
  else
    rand_b64 > "$file"
    echo "Generated: $file"
  fi
}

# Dev secrets
create_secret "$DEV_DIR/mysql_password"
create_secret "$DEV_DIR/mysql_root_password"
create_secret "$DEV_DIR/jwt_secret"

# Docker secrets
create_secret "$DOCKER_DIR/mysql_password"
create_secret "$DOCKER_DIR/mysql_root_password"
create_secret "$DOCKER_DIR/jwt_secret"

echo
echo "Secrets prêts."
echo "Utilisation local IntelliJ: APP_SECRETS_DIR=./secrets/dev (par défaut via application.yml)"
echo "Utilisation docker-compose: secrets montés dans /run/secrets"