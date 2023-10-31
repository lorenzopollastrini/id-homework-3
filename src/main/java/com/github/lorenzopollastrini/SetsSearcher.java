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

public class SetsSearcher {

    public static void main(String[] args) throws Exception {
        String indexPathString = "target/index";
        Path indexPath = Paths.get(indexPathString);
        Directory indexDirectory = FSDirectory.open(indexPath);

        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser("terms", analyzer);
        Query query = parser.parse("Tom Holland Daniel Radcliffe Elijah Wood Robert Woods");

        IndexReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new OverlapSimilarity());

        runQuery(searcher, query);

        reader.close();
        indexDirectory.close();
        analyzer.close();
    }

    private static void runQuery(IndexSearcher searcher, Query query) throws IOException {
        TopDocs hits = searcher.search(query, 10);
        StoredFields storedFields = searcher.storedFields();
        for (ScoreDoc hit : hits.scoreDocs) {
            Document doc = storedFields.document(hit.doc);
            System.out.println("Document " + hit.doc + ": " +
                    doc.get("terms") + " (" + hit.score + ")");
        }
    }

}
