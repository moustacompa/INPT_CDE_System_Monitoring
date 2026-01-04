#!/bin/bash

echo "=== Nettoyage ==="
rm -rf bin
mkdir -p bin

echo "=== Compilation ==="

# Trouver tous les fichiers .java et les compiler
find src/main/java/inpt_cde/systemmonitor/ui -name "*.java" > sources.txt

javac -d bin @sources.txt

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie"
    echo ""
    echo "=== Lancement ==="
    java -cp bin inpt_cde.systemmonitor.ui.MainWindow
else
    echo "✗ Erreur de compilation"
    rm sources.txt
    exit 1
fi

rm sources.txt