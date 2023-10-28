package com.github.lorenzopollastrini;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class OverlapSimilarity extends SimilarityBase {

    @Override
    protected double score(BasicStats basicStats, double v, double v1) {
        if (v > 0)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "OverlapSimilarity";
    }

}
