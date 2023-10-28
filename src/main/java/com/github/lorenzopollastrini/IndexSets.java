package com.github.lorenzopollastrini;

import com.google.gson.Gson;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class IndexSets {

    public static void main(String[] args) throws IOException {
        String indexPathString = "target/index";
        String tablesPathString = "tables.json";
        Path indexPath = Paths.get(indexPathString);
        Directory indexDirectory = FSDirectory.open(indexPath);

        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setCodec(new SimpleTextCodec());
        config.setSimilarity(new OverlapSimilarity());

        IndexWriter writer = new IndexWriter(indexDirectory, config);

        indexSets(writer, tablesPathString);

        writer.close();
        indexDirectory.close();
        analyzer.close();
    }

    private static void indexSets(IndexWriter writer, String tablesPathString) throws IOException {
        List<Set<Cell>> sets;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(tablesPathString));

        Gson gson = new Gson();

        String line = bufferedReader.readLine();
        int i = 1;
        while (line != null) {
            System.out.println("Extracting sets from table #" + i + "...");
            Table table = gson.fromJson(line, Table.class);

            sets = table.getSets();

            for (Set<Cell> set : sets) {
                StringJoiner joiner = new StringJoiner(" ");
                for (Cell cell : set) {
                    joiner.add(cell.getCleanedText());
                }

                Document doc = new Document();
                doc.add(
                        new TextField(
                                "terms",
                                joiner.toString(),
                                Field.Store.YES
                        )
                );
                writer.addDocument(doc);
            }
            System.out.println("Indexed all sets from table #" + i);

            line = bufferedReader.readLine();
            i++;
        }

        writer.commit();
        bufferedReader.close();
    }

}
