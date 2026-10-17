package kaktus.rss_reader_api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class FeedCatalog {

    private final Map<String, List<String>> feedsBySubject = Map.of(
            "design", List.of(
                    "https://www.dezeen.com/feed/",
                    "https://design-milk.com/feed/"),
            "photographie", List.of(
                    "https://petapixel.com/feed/",
                    "https://digital-photography-school.com/feed/")
    );

    public List<String> getFeeds(String subject) {
        String key = subject.toLowerCase().replace("#", "").trim();
        List<String> feeds = feedsBySubject.get(key);
        if (feeds == null) {
            throw new IllegalArgumentException("Sujet inconnu : " + subject);
        }
        return feeds;
    }

    public Set<String> getSubjects() {
        return feedsBySubject.keySet();
    }
}