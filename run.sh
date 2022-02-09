#!/usr/bin/env bash
set -e
echo "Creating certificates..."
mkdir -p /certs
echo -n "${SSL_CERT}" | openssl base64 -d -A -out /certs/cert.pem
echo -n "${SSL_KEY}" | openssl base64 -d -A -out /certs/privkey.pem
openssl pkcs12 -export -passout 'pass:krammatik' -in /certs/cert.pem -inkey /certs/privkey.pem -out /certs/keystore.p12 -name "krammatik"
keytool -importkeystore -srcstorepass krammatik -storepass krammatik -srckeystore /certs/keystore.p12 -srcstoretype pkcs12 -destkeystore /certs/keystore.jks
echo "Done"
java -jar /app/backend.jar