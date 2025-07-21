#!/bin/bash

INTERSHOP_JAR="../yandex/intershop/target/intershop-0.0.1-SNAPSHOT.jar"
PAYMENT_JAR="../yandex/payment-service/target/payment-service-1.0.0.jar"

chmod +x "$INTERSHOP_JAR"
chmod +x "$PAYMENT_JAR"

"$PAYMENT_JAR" &
"$INTERSHOP_JAR"
