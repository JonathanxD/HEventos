#!/bin/bash
echo Compilando...
mkdir output/
javac -cp ./library/spigot-1.7.9-R0.2-SNAPSHOT-1486.jar:./library/SimpleClans.jar:./library/Vault-1.4.1.jar:. -target 1.7 -source 1.7 -d output/ $(find -name '*.java')
cd output/
jar cf ../HEventos.jar $(find -name '*.class') ../plugin.yml ../config.yml ../README.md
cd ../
rm -R output/
echo Compilado! Salvo como HEventos.jar