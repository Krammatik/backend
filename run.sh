#!/usr/bin/env bash
echo "Creating certificates..."
echo "${SSL_CERT}" | base64 > /app/cert.pem
echo "${SSL_PRIVATEKEY}" | base64 > /app/privatekey.pem
openssl pkcs12 -export -passin 'pass:krammatik' -in /app/cert.pem -inkey /app/privatekey.pem -out /app/keystore.p12 -name "krammatik"
keytool -importkeystore -storepass krammatik -srckeystore /app/keystore.p12 -srcstoretype pkcs12 -destkeystore /app/keystore.jks
echo "Done"
java -jar /app/backend.jar