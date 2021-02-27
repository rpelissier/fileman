# Objectif

Fileman a pour but d'importer différentes sources de fichiers pour pouvoir
les organiser, les sauvegarder, les partager et les indexer.
L'utilisateur importe des nouvelles sources de données dans une répertoire de son choix.
Ce répertoire est ajouté à Fileman qui va le scanner, indexer les fichiers, fusionner les doublons, 
et ajouter les nouveaux fichiers en créant des hardlink.
Le répertoire source n'est pas modifié et peut rester "vivant", par exemple en subissant des 
updates par rsync de manière incrémentale.

## Lot 1
- Scanner un répertoire
- L'indexer en BDD
  - convertir les noeuds des paths en tags
  - se souvenir du mapping avec le(s) documents d'origine
  - merging par clé [name + date + size]
- Hardlink des fichiers dans le stockage de référence local
- Filtrer les fichiers importés
  - Par extension
  - Par path

## Lot 2
Features
- supprimer les doublons
    - basés sur lastModified + size
    - ou sur un full hash (type md5)
- utiliser des hardlink pour l'importation du fichier et pour supprimer les doublons dans leur répertoire d'origine)
- dedupliquer les doublons
- merger les informations des doublons
- se souvenir des origines (multiples en cas de doublons) des fichiers
    - repertoire racine
    - chemin racine
- zéro overwrite ou perte d'information
- export d'un jeu de fichiers selon un pattern lié à la priorité des tags
    - du moins discriminant au plus discriminant
- sauvegarde cryptée (fichiers + db) dans AWS Glacier (ou equivalent)
- aucune limitation de volume ou de nombre de fichiers

  

Options
- accès aux fichiers et aux recherches depuis nextcloud
- manipulation des tags par batch de fichier, supprimer/ajouter un tag pour N fichiers obtenus à partir d'une recherche
- recherche par tag, extension, size et date
- renommage et merge des tags