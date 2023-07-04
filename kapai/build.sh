#!/usr/bin/env bash
./gradlew clean
./gradlew bootJar
cd golib
go mod tidy
go build -buildmode=c-shared -o libweb3.so main.go
cp libweb3.so ../
cp libweb3.so ../build/libs/
cd ..
scp ./build/libs/kapai-0.0.1-SNAPSHOT.jar root@8.222.241.223:/home/gamecard/