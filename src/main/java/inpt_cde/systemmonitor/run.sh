#!/bin/bash

echo "=== Compilation ==="

# Créer le dossier bin s'il n'existe pas
mkdir -p bin

# Compiler tous les fichiers
javac -d bin src/main/java/inpt_cde/systemmonitor/model/*.java
javac -cp bin -d bin src/main/java/inpt_cde/systemmonitor/ui/*.java
javac -cp bin -d bin src/main/java/inpt_cde/pkg_agent/controller/*.java
javac -cp bin -d bin src/main/java/inpt_cde/pkg_server/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie"
    echo ""
    echo "=== Lancement de l'application ==="
    java -cp bin inpt_cde.systemmonitor.ui.MainWindow
else
    echo "✗ Erreur de compilation"
    exit 1
fi