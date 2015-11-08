package io.egreen.cyloon.search.app.service;

import io.egreen.cyloon.search.app.model.SiteDate;
import io.egreen.cyloon.search.app.workers.IndexBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dewmal on 11/7/15.
 */
public class SearchService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private IndexBuilder indexBuilder;

    @Autowired
    private String indexDir;

    public void index() throws Exception {

        indexBuilder.build();

//        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
//        Directory index = new RAMDirectory();
//
//        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
//
//        IndexWriter w = new IndexWriter(index, config);
//        addDoc(w, "Lucene in Action", "193398817");
//        addDoc(w, "Lucene for Dummies", "55320055Z");
//        addDoc(w, "Managing Gigabytes", "55063554A");
//        addDoc(w, "The Art of Computer Science", "9900333X");
//        w.close();


    }


    private IndexSearcher searcher = null;
    private QueryParser parser = null;


    /**
     * Creates a new instance of SearchEngine
     */
    public void intIndex() throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexDir))));
        Analyzer keywordAnalyzer = new KeywordAnalyzer();
        String[] filesToSearch = {"title", "content", "location"};
        parser = new MultiFieldQueryParser(filesToSearch, keywordAnalyzer);

    }

    public TopDocs performSearch(String queryString, int n)
            throws IOException, ParseException {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }

    public List<SiteDate> find(String query) throws IOException, ParseException {

        if (searcher == null && parser == null) {
            intIndex();
        }

        TopDocs topDocs = performSearch(query, 5);
        List<SiteDate> documents = new ArrayList<>();
        System.out.println(topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = getDocument(scoreDoc.doc);


            documents.add(mongoTemplate.findById(document.get("_id"), SiteDate.class));


        }


        return documents;

    }
}
