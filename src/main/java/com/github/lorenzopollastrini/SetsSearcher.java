package com.github.lorenzopollastrini;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SetsSearcher {

    public static void main(String[] args) throws Exception {
        String usage = "Utilizzo: java com.github.lorenzopollastrini.SetsSearcher" +
                " [-index INDEX_PATH] [-query QUERY]\n\n";

        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.out.println(usage);
            System.exit(0);
        }

        String indexPathString = null;
        StringBuilder queryString = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-index":
                    indexPathString = args[++i];
                    break;
                case "-query":
                    queryString = new StringBuilder(args[++i]);
                    while (i < args.length - 1)
                        queryString.append(" ").append(args[++i]);
                    break;
                default:
                    throw new IllegalArgumentException("Parametro " + args[i] + " sconosciuto");
            }
        }

        if (indexPathString == null || queryString == null) {
            System.err.println(usage);
            System.exit(1);
        }

        Path indexPath = Paths.get(indexPathString);
        Directory indexDirectory = FSDirectory.open(indexPath);

        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser("terms", analyzer);
        Query query = parser.parse(String.valueOf(queryString));

        System.out.println("Inizializzazione del lettore dell'indice in corso...");
        Date readerInitStart = new Date();
        IndexReader reader = DirectoryReader.open(indexDirectory);
        Date readerInitEnd = new Date();
        System.out.println("Inizializzazione del lettore dell'indice completata in " +
                TimeUnit.MILLISECONDS.toSeconds(readerInitEnd.getTime() - readerInitStart.getTime()) + " s");
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new OverlapSimilarity());

        System.out.println("Interrogazione in corso...");
        Date queryRunStart = new Date();
        runQuery(searcher, query);
        Date queryRunEnd = new Date();
        System.out.println("Interrogato l'indice in " +
                TimeUnit.MILLISECONDS.toSeconds(queryRunEnd.getTime() - queryRunStart.getTime()) + " s");

        reader.close();
        indexDirectory.close();
        analyzer.close();
    }

    private static void runQuery(IndexSearcher searcher, Query query) throws IOException {
        TopDocs hits = searcher.search(query, 10);
        StoredFields storedFields = searcher.storedFields();
        for (ScoreDoc hit : hits.scoreDocs) {
            Document doc = storedFields.document(hit.doc);
            System.out.println("ID del set: " + hit.doc + "\n" +
                    "ID della tabella: " + doc.get("table_id") + "\n" +
                    "Score del set: " + hit.score + "\n" +
                    "Contenuto del set: " + doc.get("terms") + "\n");
        }
    }

}
