package com.stanford.nlp.rest.pos;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.CacheManager;

@RestController
public class Tagger {
    @Autowired
    CacheManager cacheManager;

    @RequestMapping(value = "/status/",
            method = RequestMethod.GET)
    public @ResponseBody  String accountInformation() {
        MaxentTagger maxentTagger = (MaxentTagger) (cacheManager
                .getCache("stanfordnlpmodelscache")
                .get("postagger").get());
        String op = maxentTagger.tagString("your stanford nlp rest service is up");
        return op;
    }

    @RequestMapping(value="/pos/tag/", method=RequestMethod.GET)
    public @ResponseBody String tag_pos(@RequestParam("text") String text) {
        MaxentTagger maxentTagger = (MaxentTagger) (cacheManager
                .getCache("stanfordnlpmodelscache")
                .get("postagger").get());
        String op = maxentTagger.tagString(text.toLowerCase().trim());
        return op;
    }


    @RequestMapping(value="/ner/tag/", method=RequestMethod.GET)
    public @ResponseBody String tag_ner(@RequestParam("text") String text) {
        AbstractSequenceClassifier classifier = (AbstractSequenceClassifier) (cacheManager
                .getCache("stanfordnlpmodelscache")
                .get("nerclassifier").get());
        String op = classifier.classifyToString(text.toLowerCase().trim());
        return op;
    }
}
