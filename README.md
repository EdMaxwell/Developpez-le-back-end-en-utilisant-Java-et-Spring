# Chatop API

## Description

Chatop est une application web de gestion de locations immobilières développée avec Spring Boot. Elle fournit une API REST sécurisée permettant aux utilisateurs de créer des comptes, publier des annonces de location, et échanger des messages concernant les propriétés.

## Fonctionnalités

- **Authentification JWT** : Système d'inscription et de connexion sécurisé
- **Gestion des utilisateurs** : Profils utilisateur avec informations personnelles
- **Gestion des locations** : CRUD complet des annonces de location avec upload d'images
- **Système de messagerie** : Communication entre utilisateurs et propriétaires
- **Documentation API** : Interface Swagger/OpenAPI intégrée

## Architecture technique

### Technologies utilisées

- **Java 17** : Langage de programmation principal
- **Spring Boot 3.5.3** : Framework principal
- **Spring Security** : Authentification et autorisation
- **Spring Data JPA** : Persistance des données
- **MySQL** : Base de données relationnelle
- **JWT** : Authentification par token
- **Swagger/OpenAPI** : Documentation API
- **Lombok** : Réduction du code boilerplate
- **Maven** : Gestionnaire de dépendances

### Structure du projet

```
src/main/java/com/openclassrooms/chatop/
├── ChatopApplication.java          # Point d'entrée de l'application
├── configuration/                  # Classes de configuration
│   ├── ApplicationConfiguration.java
│   ├── DotenvLoader.java
│   ├── JwtAuthenticationFilter.java
│   ├── OpenApiConfig.java
│   └── SecurityConfiguration.java
├── controllers/                    # Contrôleurs REST
│   ├── AuthenticationController.java
│   ├── MessageController.java
│   ├── RentalController.java
│   └── UserController.java
├── dtos/                          # Objets de transfert de données
│   ├── CreateMessageDto.java
│   ├── CreateRentalDto.java
│   ├── LoginResponse.java
│   ├── LoginUserDto.java
│   ├── MeResponse.java
│   ├── RegisterUserDto.java
│   ├── RentalListItemDto.java
│   ├── RentalListResponse.java
│   ├── RentalResponse.java
│   └── UpdateRentalDto.java
├── entities/                      # Entités JPA
│   ├── Message.java
│   ├── Rental.java
│   └── User.java
├── repositories/                  # Interfaces de persistance
│   ├── MessageRepository.java
│   ├── RentalRepository.java
│   └── UserRepository.java
└── services/                      # Services métier
    ├── AuthenticationService.java
    ├── JwtService.java
    ├── MessageService.java
    ├── RentalService.java
    └── UserService.java
```

## Configuration

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- Git

### Variables d'environnement

Créez un fichier `.env` à la racine du projet en vous basant sur `.env.example` :

```properties
# Configuration base de données
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_NAME=chatop
DATABASE_USERNAME=root
DATABASE_PASSWORD=your_password

# Configuration JWT
JWT_SECRET_KEY=your_very_long_secret_key_minimum_256_bits
JWT_EXPIRATION_TIME=86400000

# Configuration application
SERVER_PORT=3001
```

### Configuration de la base de données

1. Créez une base de données MySQL :
```sql
CREATE DATABASE chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Les tables seront créées automatiquement au démarrage grâce à JPA.

## Installation et démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/EdMaxwell/Developpez-le-back-end-en-utilisant-Java-et-Spring.git
cd Developpez-le-back-end-en-utilisant-Java-et-Spring
```

### 2. Configuration de l'environnement

```bash
# Copier le fichier d'exemple et le configurer
cp .env.example .env
# Éditer le fichier .env avec vos paramètres
```

### 3. Compilation

```bash
# Nettoyer et compiler le projet
./mvnw clean compile

# Ou avec Maven global
mvn clean compile
```

### 4. Démarrage de l'application

```bash
# Démarrage en mode développement
./mvnw spring-boot:run

# Ou compilation et démarrage du JAR
./mvnw clean package
java -jar target/chatop-0.0.1-SNAPSHOT.jar
```

L'application sera disponible sur `http://localhost:3001`

## Tests

### Exécution des tests

```bash
# Tous les tests
./mvnw test

# Tests avec rapport de couverture (si configuré)
./mvnw test jacoco:report
```

### Types de tests

- **Tests unitaires** : Tests des services et logiques métier
- **Tests d'intégration** : Tests des contrôleurs et de la persistance
- **Tests de sécurité** : Tests des endpoints protégés

## Documentation API

### Swagger UI

Une fois l'application démarrée, la documentation interactive est disponible sur :
- **URL** : `http://localhost:3001/swagger-ui/index.html`
- **Spécification OpenAPI** : `http://localhost:3001/v3/api-docs`

### Endpoints principaux

#### Authentification
- `POST /api/auth/register` - Inscription d'un nouvel utilisateur
- `POST /api/auth/login` - Connexion utilisateur
- `GET /api/auth/me` - Informations de l'utilisateur connecté

#### Utilisateurs
- `GET /api/user/{id}` - Informations d'un utilisateur

#### Locations
- `GET /api/rentals` - Liste des locations
- `GET /api/rentals/{id}` - Détails d'une location
- `POST /api/rentals` - Création d'une location
- `PUT /api/rentals/{id}` - Modification d'une location
- `GET /api/rentals/{id}/picture` - Image d'une location

#### Messages
- `POST /api/messages` - Envoi d'un message

### Authentification

L'API utilise JWT Bearer Token pour l'authentification :

```http
Authorization: Bearer your-jwt-token-here
```

## Développement

### Standards de code

- **Naming** : camelCase pour les variables et méthodes, PascalCase pour les classes
- **Documentation** : Javadoc obligatoire pour les classes publiques et méthodes
- **Validation** : Utilisation des annotations Jakarta Validation
- **Sécurité** : Tous les endpoints sensibles protégés par authentification

### Bonnes pratiques

1. **DTOs** : Utilisation systématique pour les transferts de données
2. **Services** : Logique métier dans les services, contrôleurs légers
3. **Repositories** : Interface JPA avec méthodes custom si nécessaire
4. **Exception handling** : Gestion centralisée des erreurs
5. **Tests** : Couverture minimale de 80% recommandée

### Ajout de nouvelles fonctionnalités

1. Créer les entités et DTOs nécessaires
2. Implémenter les repositories
3. Développer les services métier
4. Créer les contrôleurs REST
5. Ajouter les tests unitaires et d'intégration
6. Mettre à jour la documentation

## Déploiement

### Build de production

```bash
# Compilation optimisée
./mvnw clean package -Dmaven.test.skip=true

# Le JAR sera généré dans target/
```

### Configuration production

- Utiliser des variables d'environnement pour les secrets
- Configurer SSL/HTTPS
- Optimiser les paramètres JVM
- Configurer les logs appropriés

## Maintenance

### Logs

Les logs sont configurés via `application.properties` :
- Niveau DEBUG en développement
- Niveau INFO/WARN en production
- Rotation automatique des fichiers de log

### Monitoring

- Health check endpoint : `GET /actuator/health`
- Métriques : `GET /actuator/metrics`

### Sauvegarde

- Sauvegardes régulières de la base de données MySQL
- Sauvegarde des images uploadées

## Licence

Ce projet est développé dans le cadre du parcours OpenClassrooms.

## Contact et support

Pour toute question ou problème :
- Ouvrir une issue sur le repository GitHub
- Consulter la documentation Swagger pour les détails de l'API