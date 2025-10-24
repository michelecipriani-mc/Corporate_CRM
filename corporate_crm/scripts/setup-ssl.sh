#!/bin/bash
set -e

# ===============================
# CONFIGURAZIONE
# ===============================
BACKEND_KEYSTORE_DIR="src/main/resources/keystore"
BACKEND_KEYSTORE_FILE="$BACKEND_KEYSTORE_DIR/keystore.p12"
BACKEND_KEYSTORE_PASS="la_tua_password_sicura"

FRONTEND_SSL_DIR="../corporate_crm_front/ssl"
FRONTEND_CERT="$FRONTEND_SSL_DIR/localhost.crt"
FRONTEND_KEY="$FRONTEND_SSL_DIR/localhost.key"

DNAME="CN=localhost, OU=Dev, O=MyOrg, L=City, S=State, C=IT"

echo "🔧 Inizio generazione certificati SSL..."
echo "------------------------------------------"

# ===============================
# BACKEND SPRING BOOT
# ===============================
if [ ! -f "$BACKEND_KEYSTORE_FILE" ]; then
  echo "➡️  Genero keystore per Spring Boot..."
  mkdir -p "$BACKEND_KEYSTORE_DIR"

  keytool -genkeypair \
    -alias local-ssl \
    -keyalg RSA \
    -keysize 2048 \
    -storetype PKCS12 \
    -keystore "$BACKEND_KEYSTORE_FILE" \
    -validity 365 \
    -dname "$DNAME" \
    -storepass "$BACKEND_KEYSTORE_PASS" >/dev/null 2>&1

  echo "✅ Keystore Spring Boot creato: $BACKEND_KEYSTORE_FILE"
else
  echo "ℹ️ Keystore Spring Boot già presente: $BACKEND_KEYSTORE_FILE"
fi

# ===============================
# FRONTEND ANGULAR
# ===============================
if [ ! -f "$FRONTEND_CERT" ] || [ ! -f "$FRONTEND_KEY" ]; then
  echo "➡️  Genero certificato SSL per Angular..."
  mkdir -p "$FRONTEND_SSL_DIR"

  openssl req -x509 -newkey rsa:2048 -nodes \
    -keyout "$FRONTEND_KEY" \
    -out "$FRONTEND_CERT" \
    -days 365 \
    -subj "/CN=localhost" >/dev/null 2>&1

  echo "✅ Certificato Angular creato in: $FRONTEND_SSL_DIR"
else
  echo "ℹ️ Certificati Angular già presenti: $FRONTEND_SSL_DIR"
fi

echo "------------------------------------------"
echo "✅ Certificati SSL pronti per Spring e Angular!"
