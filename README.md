# Homework 3 di ID

## Descrizione del progetto
Il progetto richiesto per l’Homework 3 è strutturato in tre classi Java:
* **SetsIndexer.java** contiene il codice che indicizza, tramite l'algoritmo MergeList, le colonne (anche chiamate
_set_) di un file in cui ogni riga è una tabella in formato JSON. In questa repository è disponibile un piccolo dataset 
utilizzato in fase di testing. Il dataset oggetto dell'analisi di questo progetto è disponibile
[qui](https://gitlab.com/Rm3UofA/Mentor/Datasets);
* **SetsSearcher.java** contiene il codice che interroga l’indice con una colonna di query. Vengono stampate le prime
dieci colonne del ranking;
* **StatsCalculator.java** contiene il codice che calcola e stampa alcune statistiche del dataset.

## Comandi
* `java com.github.lorenzopollastrini.SetsIndexer [-index INDEX_PATH] [-tables TABLES_PATH] [-update]`: indicizza le
colonne delle tabelle contenute nella directory al percorso `TABLES_PATH`, salvando l'indice al percorso `INDEX_PATH`.
Se si specifica `-update`, ad ogni esecuzione del comando l'indice non verrà cancellato e ricostruito, bensì aggiornato.
* `java com.github.lorenzopollastrini.SetsSearcher [-index INDEX_PATH] [-query QUERY]`: interroga l'indice al percorso
`INDEX_PATH` con la `QUERY` specificata, la quale può essere immessa nella [sintassi interpretata dal Query Parser
  classico di Apache Lucene](
https://lucene.apache.org/core/9_8_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html). Va
effettuato l'escaping di alcuni caratteri speciali interpretati da tale sintassi (ad esempio, va effettuato l'escaping
del carattere `"` con `\"`).