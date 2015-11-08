package io.egreen.cyloon.search.app.search;

import com.mongodb.BasicDBObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dewmal on 11/7/15.
 */
public class AnalyzedDBObject extends BasicDBObject {
    public static enum Condition {ALL, IN}

    private Analyzer analyzer;

    public AnalyzedDBObject(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public AnalyzedDBObject appendAndAnalyzeFullText(String name, String text)
            throws IOException {
        append(name, tokenize(analyzer.tokenStream(name, new StringReader(text))));
        return this;
    }

    private List<String> tokenize(TokenStream tokenStream) throws IOException {
        List<String> tokens = new ArrayList<>();
//        TokenStream tokenStream = analyzer.tokenStream(fieldName, reader);
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String term = charTermAttribute.toString();
            tokens.add(term);
        }
        return tokens;
    }
}
