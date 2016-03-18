# Gestion Répartie entre Etudiants des Viaferrata Enterrées et en Surface (GREVES)
Projet de GPI (Gestion de Projet Informatique) ayant pour objectif la simulation du comportement du RER A (Région Parisienne)

Pour se souvenir des classes et termes qu'il faudra faire :
--
* Ligne : Toute la ligne A, avec plusieurs branches
* Voie : en gros :  gare de départ et gare d'arrivée (d'un terminus à un autre) -> (en java) AL\<Canton\>
* Canton : Rail[2] + AL\<Canton\> adjacents (possède politique de sécurité)
* Rail : Train (peut être null + 1 seul par rail) (sûrement superposés dans l'interface graphique)
* Train : vitesse + position + Voie + Canton (possible chgt pour la localisation du train)


# Modèle de javadoc :
	/**
	 * ATTENTION : Toute la doc doit être faite en anglais
	 * Pour des exemples : Voir la javadoc dans les classes de src/model/configuration
	 *
	 * Le Pourquoi de la methode.
	 * Les possibles limites (complexité par exemple) de la méthode, avec éventuellement du "Comment"
	 * est faite l'implémentation (pas trop, juste assez pour expliquer les limites)
	 *
	 *Pour chaque paramètre :
	 * @param nom_du_paramètre
	 * 			(Type_Du_paramètre) Description du contenu du paramètre (brièvement, 1 ligne)
	 *
	 *Si il y a un return :
	 * @return
	 * 			(Type_du_Return) Valeur et Description de ce qui est return.
	 *S'il y a plusieurs returns différents, donner les différentes valeurs returns et la condition
	 *du return exemple :
	 * @return
	 * 			(Integer) 	0 if both parameters are equal
	 *						1 if not
	 *
	 * S'il y a des throws :
	 * @throws
	 * 			Nom_de_l'exception condition du throw
	 * 
	 * Si il existe des méthodes importantes que la méthode actuelle appelle
	 * ou s'il existe des méthodes similaires avec quelques différences
	 * ou si certaines variables importantes/return/arguments sont des instances d'une classe :
	 * @see Nom_Classe#Nom_methode(Type_param1, Type_param2, etc...)
	 * @see Nom_Classe
	 */