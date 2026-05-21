#!/bin/bash
set -e

# Configure PostgreSQL to use trust authentication
echo "Configuring PostgreSQL authentication..."
cat > /var/lib/postgresql/data/pg_hba.conf <<EOF
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   all             all                                     trust
host    all             all             0.0.0.0/0               trust
host    all             all             ::0/0                   trust
EOF

echo "PostgreSQL authentication configured successfully"

# Made with Bob
