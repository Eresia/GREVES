# Gestion Répartie entre Etudiants des Viaferrata Enterrées et en Surface (GREVES)
Projet de GPI (Gestion de Projet Informatique) ayant pour objectif la simulation du comportement du RER A (Région Parisienne)

Pour se souvenir des classes et termes qu'il faudra faire :
--
* Ligne : Toute la ligne A, avec plusieurs branches
* Voie : en gros :  gare de départ et gare d'arrivée (d'un terminus à un autre) -> (en java) AL<Canton>
* Canton : Rail[2] + AL<Canton> adjacents (possède politique de sécurité)
* Rail : Train (peut être null + 1 seul par rail) (sûrement superposés dans l'interface graphique)
* Train : vitesse + position + Voie + Canton (possible chgt pour la localisation du train)