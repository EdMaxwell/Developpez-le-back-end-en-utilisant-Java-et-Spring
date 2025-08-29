package com.openclassrooms.chatop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale de Chatop - une plateforme de gestion de locations immobilières.
 * 
 * <p>Cette application Spring Boot fournit une API REST pour gérer les locations,
 * les utilisateurs et les messages. Elle inclut l'authentification JWT et la documentation
 * OpenAPI/Swagger.</p>
 * 
 * <p>Fonctionnalités principales :</p>
 * <ul>
 *   <li>Gestion des utilisateurs avec authentification JWT</li>
 *   <li>CRUD des locations immobilières</li>
 *   <li>Système de messagerie entre utilisateurs et propriétaires</li>
 *   <li>Upload et gestion d'images pour les locations</li>
 *   <li>Documentation API via OpenAPI/Swagger</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class ChatopApplication {

	/**
	 * Point d'entrée principal de l'application Chatop.
	 * 
	 * @param args arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(ChatopApplication.class, args);
	}

}
