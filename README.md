/*************** SOMMAIRE ***************/

1. Présentation du projet
2. Membres du groupe
3. Glossaire
4. Conventions
 4.1 Convention : Javadoc
 4.2 Convention : Documentation
5. Récupération de données fxml
6. Problèmes Connus
 6.1 ArrayIndexOutOfBounds:-1


/****************************************/

#1. Présentation du projet

  Ce projet est réalisé au cours de la 3ème année de Licence Informatique à l'université de Cergy-Pontoise, au sein du module "Gestion de Projet Informatique" donné par Tianxiao LIU (https://depinfo.u-cergy.fr/~tliu/index.php).
  
  Le but initial de ce module est l'introduction aux méthodes agiles et à l'apprentissage du travail en équipe au niveau professionel.
  
  Afin d'apprendre la gestion d'un projet, il faut d'abord avoir un projet. Le projet que nous devons réalisé est une simulation logicielle en Java de la ligne A du RER, qui traverse la région parisienne.
  
  Nous avons choisi pour nom de projet l'acronyme "G.R.E.V.E.S.", correspondant à "Gestion Répartie entre Etudiants des Viaferrata Enterrées et en Surface"

#2. Membres du groupe

  Le groupe est composé de six personnes, chacune ayant un rôle défini au sein du projet :
 - Antoine REGNIER 	: CHEF	(https://github.com/nowanda92)
 - Bastien LEPESANT : MOE	(https://github.com/Eresia)
 - Vincent MONOT	: MOE	(https://github.com/Agurato)
 - Paul VALENTIN	: QA	(https://github.com/Sephage)
 - Bruno TESSIER	: MOA	(https://github.com/BrunoBob)
 - Filipe GAMA		: DOC	(https://github.com/Baalon)

#3. Glossaire

  Au cours de ce projet, nous avons dû définir de façon commune certains termes, de façon à ce qu'aucune confusion ne soit possible et que l'on puisse tous discuter et se comprendre lorsque l'on en parle. Cette définition peut indiquer certaines caractéristiques que devront comporter les classes correspondantes dans le code.

* Canton : Zone de taille variable dans laquelle il ne peut y avoir qu'un seul Train en même temps. Chaque Canton connait le Canton suivant. Chaque Canton possède une vitesse maximum limite que les Trains ne peuvent dépasser. Peut également contenir une Gare (au maximum).

* Voie (RailWay) : Ensemble des Cantons entre une gare de départ et une gare d'arrivée (Terminus).

* Terminus : Dernier Canton d'une Voie, dans lequel se trouve une Gare, signifiant la fin de celle-ci.

* Ligne (Line) : Ensemble des Voies formant toute la ligne A du RER (incluant donc les branches).

* Gare (Station) : Lieux sur la Ligne où peuvent se croiser des Voies et où les Trains doivent s'arrêter lorsqu'ils y passent, sauf exceptions.

* Parcours (RoadMap) : Ensemble de Gares et de Voies constituant la route que doit suivre un Train, depuis son départ jusqu'à son arrivée.

* Train : Se déplace sur la Ligne. En fonction de son Parcours, il passera ou non par certaines Voies et Gares.

#4. Conventions

  En plus des mots définis dans le Glossaire, certaines conventions ont dû être mises au point. Encore une fois, ceci est fait dans un but d'améliorer le travail d'équipe, d'éviter les confusions et d'avoir un résultat cohérent.

  4.1 Convention : Javadoc

  Cette convention concerne la forme de la documentation du code (javadoc) :

   - Toute la javadoc doit être faite en anglais.
   - La javadoc doit être présente sur toutes les méthodes et suivre le modèle type ci-dessous.

    /**
    * Description de la méthode, comprenant :
    *  - Le "Pourquoi" de la méthode.
    *  - Les limites présentes (complexité des algos si importante, cas non gérés, etc.)
    *  - Très peu de "Comment" est faite l'implémentation, le préciser seulement quand nécessaire
    *		(ex : pour expliquer les limites)
    *
    * Pour chaque paramètre de la méthode :
    * @param Nom_Du_Paramètre
    *			(Type_Du_Paramètre) Description brève du contenu du paramètre (1 ligne)
    *
    * Dans le cas d'un return de la méthode :
    * @return
    *			(Type_Du_Return) Valeur et Description de ce qui est return.
    *
    * ATTENTION : Cas avec plusieurs valeurs de returns différentes :
    *		Il faut indiquer les valeurs possibles et les conditions correspondantes à l'obtention
    *		de ces valeurs. Exemple :
    * @return
    *		(Integer) Returns the result of the comparison between the two parameters
    *					1 if both parameters are equal
    *					0 if both parameters are different
    *
    * Pour chaque Throw d'Exception :
    * @throws
    *			Nom_De_L'Exception Condition_du_throw
    *
    * Si il existe des méthodes importantes que la méthode appelle
    * ou s'il existe des méthodes similaires avec quelques différences
    * ou si certaines variables importantes / returns / paramètres sont des instances d'une classe
    *	qui n'appartient pas aux types de base de Java (String, Integer, etc.) :
    * @see Nom_Classe#Nom_Methode(Type_Param1, ..., Type_ParamN)
    * ou
    * @see Nom_Classe
    */

 4.2 Convention : Documentation

   Différents documents étaient à réaliser, demandant chacun la participation de différents membres de l'équipe.
   Afin d'unifier l'ensemble des documents, il a fallu établir certaines conventions :

    - Tous les documents doivent être écrits en LaTeX, sur sharelatex.com.
    - Tous les documents doivent être écrits en français.
    - Tous les documents doivent utiliser la même police (celle par défaut) et taille de caractère (12).

    - Chaque section doit être dans un fichier à part, qui a pour nom celui de la section.
    - La page de garde et l'abstract sont également dans des fichiers à part.
    - Chaque section, subsection et figure doit avoir un label unique attribué.

#5. Récupération de Données fxml

	Pour récupérer un élément de la fenêtre il suffit de prendre la racine de la fenêtre (root) et utiliser
	la méthode root.lookup("#id de l'élément à récupérer").

	Liste des id :

		Global view :
			-affichage graphiqe de la ligne (ScrollPane) = LineDraw
			-affichage de l'heure (Label) = TimeLabel
			-liste des trains (ComboBox) = TrainList
			-bouton normaliser un train (Button) = StartTrain
			-bouton arrêter un train (Button) = StopTrain
			-bouton supprimer un train (Button) = DeleteTrain;
			-bouton de la vue ajouter un train (Button) = AddTrainViewButton
			-bouton vue conducteur (Button) = DriverViewButton
			-affichage de l'etat du canton selectionner (Label) = CantonState
			-bouton arrêt du canton (Button) = StopCanton
			-bouton ralentir le canton (Button) = SlowCanton
			-bouton vitesse normale du canton (Button) = NormalCanton
			-barre de vitesse globale (Slider) = ChangeSpeed
			-liste des gares (TableView) = StationList
			-bouton vue des horaire de la gare (Button) = StationViewButton

		Driver view :
			-prochaine station (Label) = NextStationName
			-temps avant prochaine gare (Label) = NextStationTime
			-destination (Label) = FinalStation
			-avance/retard (Label) = TimeState
			-affichage graphiqe de la ligne du conducteur (ScrollPane) = DriverLineDraw

		Station view :
			-prochain trains voie 1 (TableView) = NextTrainFirstTable
			-gares desservies voie 1 (TableView) = NextStationFirstTable
			-prochain trains voie 2 (TableView) = NextTrainSecondTable
			-gares desservies voie 2 (TableView) = NextStationSecondTable

		AddTrain view :
			-liste des trajets possibles (ComboBox) = RoadMapsList
			-bouton ajouter un train (Button) = AddTrain

#6. Problèmes Connus

  Cette section comporte les différents problèmes connus qui sont présents dans le programme, et les informations que nous avons sur ces problèmes. Lorsqu'un problème est résolu, il faut le retirer de cette liste.

