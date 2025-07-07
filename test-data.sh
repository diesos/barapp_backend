#!/bin/bash

# Script d'initialisation des données BarApp
echo "🍹 Initialisation des données BarApp..."

API_URL="http://localhost:8080/api"

# Fonction pour attendre que l'API soit prête
wait_for_api() {
    echo "⏳ Attente de l'API..."
    while ! curl -f $API_URL/actuator/health > /dev/null 2>&1; do
        sleep 2
    done
    echo "✅ API prête !"
}

wait_for_api
