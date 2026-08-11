#!/bin/bash

set -e

MIGRATION_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

docker exec -i postgres psql -U postgres -d future-retail -f /dev/stdin < "$MIGRATION_DIR/01-products.sql" && \
echo "All migrations completed successfully!"
