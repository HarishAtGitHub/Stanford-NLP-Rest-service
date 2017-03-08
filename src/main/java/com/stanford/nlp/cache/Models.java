package com.stanford.nlp.cache;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.io.IOException;
import java.io.Serializable;

public class Models implements Serializable {
    @Autowired
    CacheManager cacheManager;

    private MaxentTagger maxentTagger;

    public Models() throws IOException, ClassNotFoundException {
        maxentTagger = new MaxentTagger("lib/models/pos/english-caseless-left3words-distsim-2016-10-31.tagger");
        Cache cache = cacheManager.getCache("stanfordnlpmodelscache");
        cache.put("postagger", maxentTagger);
    }

    public MaxentTagger getPosTagger() {
        return maxentTagger;
    }
}
