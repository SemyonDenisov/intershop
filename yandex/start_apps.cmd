@echo off
setlocal

REM Переход в директорию yandex
cd /d ..\yandex

REM Запуск payment-service
echo Запуск payment-service...
start "payment-service" java -jar payment-service\target\payment-service-1.0.0.jar

REM Запуск intershop
echo Запуск intershop...
start "intershop" java -jar intershop\target\intershop-0.0.1-SNAPSHOT.jar

echo Все приложения запущены.
endlocal
pause