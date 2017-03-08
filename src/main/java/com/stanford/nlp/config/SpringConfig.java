package com.stanford.nlp.config;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.stanford.nlp.rest")
public class SpringConfig {

    @Bean
    public CacheManager cacheManager() throws IOException, ClassNotFoundException {
        CacheManager cacheManager = new EhCacheCacheManager(ehCacheCacheManager().getObject());
        String posModel = "lib/models/pos/english-caseless-left3words-distsim-2016-10-31.tagger";
        MaxentTagger maxentTagger = new MaxentTagger(posModel);
        Cache cache = cacheManager.getCache("stanfordnlpmodelscache");
        cache.put("postagger", maxentTagger);

        String nerClassifier = "lib/models/ner/english.muc.7class.caseless.distsim.crf.ser.gz";
        AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(nerClassifier);
        cache.put("nerclassifier", classifier);
        return cacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }
}
