# Objectif

Fileman permet d'indexer, explorer et manipuler des répertoires contenant de nombreux fichiers.

# Lots

## Lot File Index (100%)
- Scanner un répertoire
- Indexer les fichiers en BDD
  - par taille
  - par date
  - par path
  - par extension

## Lot Binary Index (100%)
- Scanner le contenu binaire des fichiers pour trouver les duplicates.

## Lot UI : Application canvas
- React TS front setup

## Lot UI : Reports
- Rapport sommaire (nb de fichiers, total size...)
- Tree map par size
  - https://www.highcharts.com/demo/treemap-large-dataset

## Lot Indexing Filters
Il faut pouvoir ignorer des sous répertoires au moment du scan.
Ex :
- fichiers systèmes
- fichiers binaires compilés

Il faut mettre en place des filtres.
Au niveau fichier:
- Par extension de fichier
- Par path/name regex de fichier
Au niveau subdir crawling
- Ne pas explorer le dir s'il contient un fichier xxx (pour filter les BDD de code)

Prévoir un rapport json pour savoir ce qui a été ignoré.

S'inspirer de .gitignore


## Lot SEARCH

- Chercher par :
  - extension
  - name

## Lot DUPLICATE REPORT

- Rapport json de tous les fichiers dupliqués

## Pour plus tard
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