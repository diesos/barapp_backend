# BarApp Backend 🍹

## Description
Application backend pour BarApp - Un système de gestion de bar et de commandes de cocktails développé avec Spring Boot et PostgreSQL.

## Fonctionnalités

### 👥 Gestion des utilisateurs
- **Clients** : Consultation de la carte, gestion du panier, passation de commandes
- **Barmakers** : Gestion de la carte, traitement des commandes, gestion des stocks
- Authentification JWT sécurisée

### 🍸 Gestion des cocktails
- Catalogue de cocktails avec catégories
- Gestion des recettes et ingrédients
- Système de prix et promotions
- Disponibilité en temps réel

### 🛒 Système de commandes
- Panier intelligent
- Suivi des commandes en temps réel
- Statuts : Commandée → En préparation → Terminée

## 🚀 Démarrage rapide

### Prérequis
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### 1. Cloner le projet
```bash
git clone <votre-repo>
cd barapp-backend
```

### 2. Lancement avec Docker (Recommandé)
```bash
# Démarrer tous les services
docker-compose up -d

#Initiliaser fake DB:

docker exec -i barapp-postgres psql -U barapp_user -d barapp < init-db2.sql

# Démarrer avec pgAdmin (pour développement)
docker-compose --profile dev up -d

# Voir les logs
docker-compose logs -f barapp-backend
```

### 3. Lancement manuel
```bash
# Démarrer PostgreSQL
docker-compose up -d postgres

# Compiler et lancer l'application
./mvnw clean compile
./mvnw spring-boot:run
```

## 📡 API Endpoints

### 🔑 Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion
- `GET /api/auth/me` - Profil utilisateur

### 🍸 Cocktails
- `GET /api/cocktails` - Liste des cocktails disponibles
- `GET /api/cocktails/all` - Tous les cocktails (barmaker)
- `GET /api/cocktails/{id}` - Détail d'un cocktail
- `POST /api/cocktails` - Créer un cocktail (barmaker)
- `PUT /api/cocktails/{id}` - Modifier un cocktail (barmaker)

### 🛒 Panier
- `GET /api/basket` - Mon panier
- `POST /api/basket/add` - Ajouter au panier
- `PUT /api/basket/update` - Modifier quantité
- `DELETE /api/basket/remove/{cocktailId}` - Supprimer du panier

### 📋 Commandes
- `POST /api/orders` - Créer une commande
- `GET /api/orders` - Mes commandes
- `GET /api/orders/{id}` - Détail d'une commande
- `GET /api/orders/pending` - Commandes en attente (barmaker)
- `PATCH /api/orders/{id}/start` - Démarrer préparation (barmaker)
- `PATCH /api/orders/{id}/complete` - Terminer commande (barmaker)

## 🧪 Tests

### Lancer les tests
```bash
# Tests unitaires
./mvnw test

# Tests avec couverture
./mvnw verify

# Tests d'intégration
./mvnw failsafe:integration-test
```

### Couverture de code
- Objectif : **85%+**
- Rapport disponible dans `target/site/jacoco/index.html`

## 🐳 Docker

### Build de l'image
```bash
docker build -t barapp-backend .
```

### Variables d'environnement
```env
SPRING_PROFILES_ACTIVE=docker
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/barapp
SPRING_DATASOURCE_USERNAME=barapp_user
SPRING_DATASOURCE_PASSWORD=barapp_password
JWT_ALGORITHM_KEY=your-secret-key
JWT_EXPIRY_IN_SECONDS=604800
```

## 🗄️ Base de données

### Connexion PostgreSQL
- **Host** : localhost:5432
- **Database** : barapp
- **Username** : barapp_user
- **Password** : barapp_password

### pgAdmin (développement)
- **URL** : http://localhost:5050
- **Email** : admin@barapp.com
- **Password** : admin123

### Données de test
Le script `init-db.sql` contient :
- Comptes par défaut (admin, barmaker, client)
- Catalogue de cocktails
- Ingrédients et recettes

## 👥 Comptes par défaut

| Email | Mot de passe | Rôle |
|-------|-------------|------|
| admin@barapp.com | Admin123! | ADMIN |
| barmaker@barapp.com | Barmaker123! | BARMAKER |
| test@barapp.com | Test123! | USER |

## 📁 Structure du projet

```
src/
├── main/java/com/Side/Project/barapp_backend/
│   ├── controller/          # Controllers REST
│   ├── service/            # Services métier
│   ├── models/             # Entités JPA
│   │   └── dao/           # Repositories
│   ├── api/               # DTOs et modèles API
│   │   ├── models/        # DTOs
│   │   └── security/      # Configuration sécurité
│   └── exception/         # Exceptions personnalisées
└── test/                  # Tests unitaires et d'intégration
```

## 🔧 Configuration

### Profils Spring
- **default** : Développement local
- **docker** : Conteneurisation
- **test** : Tests automatisés

### CORS
Configuré pour autoriser les requêtes depuis :
- http://localhost:3000 (frontend Vue.js)
- http://localhost:8080

## 📊 Monitoring

### Health Check
- **URL** : http://localhost:8080/api/actuator/health
- **Status** : UP/DOWN avec détails database

### Métriques
- **URL** : http://localhost:8080/api/actuator/metrics

## 🚀 Déploiement

### Production
```bash
# Build optimisé
./mvnw clean package -Pprod

# Lancement avec profil docker
docker-compose -f docker-compose.prod.yml up -d
```

## 🐛 Dépannage

### Problèmes courants

**Base de données non accessible**
```bash
# Vérifier le statut
docker-compose ps

# Redémarrer postgres
docker-compose restart postgres
```

**Port déjà utilisé**
```bash
# Modifier le port dans docker-compose.yml
ports:
  - "8081:8080"  # au lieu de 8080:8080
```

**Problème de permissions**
```bash
# Donner les droits d'exécution
chmod +x mvnw
```

## 📝 TODO

- [ ] Système de notifications temps réel (WebSocket)
- [ ] Upload d'images pour les cocktails
- [ ] Gestion des stocks avancée
- [ ] Historique détaillé des commandes
- [ ] API de statistiques pour les barmakers

## 🤝 Contribution

1. Fork du projet
2. Créer une branche feature
3. Commit des modifications
4. Push vers la branche
5. Créer une Pull Request

## 📄 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.
