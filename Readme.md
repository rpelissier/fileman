# Objectif

Fileman a pour but d'aggréger de manière continue plusieurs répertoires de données.
Ces données sont indexées, dé-dupliquées et exposées à des tiers services qui pourront à leur tour les exploiter.
Les répertoires source peuvent être modifiés et les évolutions seronts prises en compte de manière incrémentale.

Le répertoire source n'est pas modifié et peut rester "vivant", par exemple en subissant des 
updates par rsync de manière incrémentale.

## Lot INDEX DIRECTORY
- Scanner un répertoire
- L'indexer en BDD
  - convertir les noeuds des paths en tags
  - se souvenir du mapping avec le(s) documents d'origine
  - merging par clé [name + date + size]

## Lot HARDLINK DB
- Hardlink des fichiers dans le stockage de référence local
  
## Lot INDEX FILTER
- Filtrer les fichiers importés 
  - Par extension
  - Par path
  - Par directory-contains(file pattern) (pour filter les BDD de code)

OPT : rapport json de tous les fichiers / répertoire filtrés

## Lot SEARCH

- Chercher par :
  - extension
  - name

## Lot DUPLICATE REPORT

- Rapport json de tous les fichiers dupliqués

## Lot 3
Features
- supprimer les doublons
    - basés sur lastModified + size
    - ou sur un full hash (type md5)
- utiliser des hardlink pour l'importation du fichier et pour supprimer les doublons dans leur répertoire d'origine)
- dedupliquer les doublons
- merger les informations des doublons (les tags)
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