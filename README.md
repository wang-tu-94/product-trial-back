# ⚙️ Product Trial - Backend API

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java_21-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
</div>

<br />

Ce dépôt contient le code source de l'API backend de l'application **Product Trial**. Construit avec Spring Boot, ce backend expose des endpoints RESTful sécurisés pour gérer les produits, les paniers utilisateurs et les listes de souhaits.

## 📋 Table des matières
- [Fonctionnalités](#-fonctionnalités)
- [Prérequis](#-prérequis)
- [Installation et Lancement (Local)](#-installation-et-lancement-local)
- [Lancement avec Docker](#-lancement-avec-docker)
- [Tests](#-tests)
- [Aperçu de l'API REST](#-aperçu-de-lapi-rest)

---

## ✨ Fonctionnalités
- **Gestion des Produits** : Recherche filtrée, création, édition et suppression de produits.
- **Gestion du Panier (Cart)** : Ajout, modification de la quantité et suppression d'articles.
- **Liste de souhaits (Wishlist)** : Sauvegarde de produits favoris par utilisateur.
- **Sécurité** : Authentification par token JWT (`Bearer Token`).
- **Base de données** : PostgreSQL (avec support H2 pour les environnements locaux/tests).
- **CI/CD** : Déploiement automatisé via GitHub Actions avec push de l'image sur Docker Hub.

---

## 🛠 Prérequis

Pour exécuter ce projet localement, assurez-vous d'avoir installé :
- **Java 21** (JDK 21)
- **Docker** et **Docker Compose** (pour la base de données ou l'environnement complet)

---

## 🚀 Installation et Lancement (Local)

### 1. Cloner le projet
```bash
git clone [https://github.com/wang-tu-94/product-trial-back.git](https://github.com/wang-tu-94/product-trial-back.git)
cd product-trial-back
```

### 2. Configuration
Par défaut, l'application utilise une base de données **H2 en mémoire** grâce au profil `local` (utile pour tester sans configuration). Vous pouvez lancer le projet directement.

### 3. Démarrer l'application avec Gradle
Utilisez le wrapper Gradle inclus pour démarrer l'application (le profil Spring `local` sera utilisé par défaut si non spécifié, ou configurez-le via votre IDE) :
```bash
./gradlew bootRun
```
L'API sera accessible sur : `http://localhost:8080/api/product-backend`

---

## 🐳 Lancement avec Docker (Environnement Dev)

Le projet inclut un fichier `docker-compose-dev.yml` qui monte à la fois une base de données **PostgreSQL** et le **Backend** configuré pour le hot-reload.

```bash
# Démarrer l'infrastructure complète (Postgres + Backend)
docker-compose -f docker-compose-dev.yml up -d
```

**Note :** Le container backend expose le port `8080` pour l'API et le port `5005` pour permettre le débogage distant (Remote Debugging).

---

## 🧪 Tests

Pour lancer l'ensemble de la suite de tests unitaires et d'intégration :
```bash
./gradlew test
```

---

## 📡 Aperçu de l'API REST

> **Chemin de base :** `/api/product-backend`
> **Sécurité :** L'en-tête `Authorization: Bearer <token>` est requis pour accéder aux routes.

### 🛍 Produits (`/v1/products`)
- `GET /v1/products` : Récupérer tous les produits (avec filtres et pagination)
- `GET /v1/products/{id}` : Récupérer un produit par son ID
- `POST /v1/products` : Créer un produit
- `PUT /v1/products/{id}` : Mettre à jour un produit
- `DELETE /v1/products/{id}` : Supprimer un produit

### 🛒 Panier (`/v1/carts`)
- `GET /v1/carts` : Récupérer le panier de l'utilisateur courant
- `POST /v1/carts/{id}/items` : Ajouter un article au panier
- `PATCH /v1/carts/{cartId}/items/{cartItemId}` : Mettre à jour la quantité d'un article
- `DELETE /v1/carts/{cartId}/items/{cartItemId}` : Supprimer un article du panier
- `DELETE /v1/carts/{id}` : Vider le panier

### ❤️ Liste de souhaits (`/v1/wishlists`)
- `GET /v1/wishlists` : Récupérer la liste de souhaits de l'utilisateur
- `POST /v1/wishlists` : Initialiser la liste de souhaits
- `POST /v1/wishlists/{wishlistId}/products/{productId}` : Ajouter un produit
- `DELETE /v1/wishlists/{wishlistId}/products/{productId}` : Retirer un produit

---
*Maintenu par [wang-tu-94](https://github.com/wang-tu-94)*
