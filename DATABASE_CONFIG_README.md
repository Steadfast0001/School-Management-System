# Database Configuration Guide

## Overview
The application now supports configurable database credentials through environment variables or properties files, eliminating hardcoded credentials for better security and flexibility.

## Configuration Methods

### 1. Environment Variables (Recommended for Production)

Set the following environment variables:

```bash
# Windows Command Prompt
set DB_URL=jdbc:postgresql://your-server:5432/your-database
set DB_USER=your_username
set DB_PASSWORD=your_secure_password

# Windows PowerShell
$env:DB_URL="jdbc:postgresql://your-server:5432/your-database"
$env:DB_USER="your_username"
$env:DB_PASSWORD="your_secure_password"

# Linux/Mac
export DB_URL=jdbc:postgresql://your-server:5432/your-database
export DB_USER=your_username
export DB_PASSWORD=your_secure_password
```

### 2. Properties File

Create or modify `config/database.properties`:

```properties
db.url=jdbc:postgresql://your-server:5432/your-database
db.user=your_username
db.password=your_secure_password
```

### 3. Custom Properties File Location

Set the `DB_CONFIG_FILE` environment variable to specify a custom properties file path:

```bash
export DB_CONFIG_FILE=/path/to/your/database.properties
```

## Configuration Priority

The system loads configuration in this order (first found wins):

1. **Environment Variables** (highest priority)
2. **Properties File** (`config/database.properties` or custom path)
3. **Default Values** (fallback for development)

## Security Best Practices

### Production Deployment

1. **Never commit credentials** to version control
2. **Use environment variables** for production deployments
3. **Use strong, unique passwords**
4. **Restrict database user permissions** to minimum required
5. **Use SSL/TLS** for database connections when possible

### Environment Variable Setup

For production servers, set environment variables in your deployment script or container configuration:

```bash
# Docker example
docker run -e DB_URL="jdbc:postgresql://db:5432/prod_db" \
           -e DB_USER="app_user" \
           -e DB_PASSWORD="secure_password" \
           your-app:latest

# Systemd service example
Environment=DB_URL=jdbc:postgresql://localhost:5432/prod_db
Environment=DB_USER=app_user
Environment=DB_PASSWORD=secure_password
```

## Development Setup

For local development, you can:

1. Use the default values (already configured)
2. Create a local `config/database.properties` file
3. Set local environment variables

## Troubleshooting

### Connection Issues

1. **Check environment variables**: `echo $DB_URL`
2. **Verify properties file**: Ensure `config/database.properties` exists and is readable
3. **Check database server**: Ensure PostgreSQL is running and accessible
4. **Validate credentials**: Test connection manually with psql

### Configuration Loading

The application will log which configuration source is being used:
- "Database configuration loaded from: [file path]"
- "Database configuration loaded from environment variables"

## Files Created

- `config/database.properties` - Sample properties file
- `.env.example` - Environment variable template
- Updated `DBConnection.java` - New configuration-aware connection manager