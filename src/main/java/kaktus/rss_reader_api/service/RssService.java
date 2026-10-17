package kaktus.rss_reader_api.service;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import kaktus.rss_reader_api.dto.ArticleDto;

@Service
public class RssService {
    private final FeedCatalog feedCatalog;
    
    public RssService(FeedCatalog feedCatalog) {
        this.feedCatalog = feedCatalog;
    }

    public List<ArticleDto> getArticles(String url, int limit) throws Exception {

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(URI.create(url).toURL()));

        return feed.getEntries().stream()
                .limit(limit)
                .map(this::toDto)
                .toList();
    }

    private ArticleDto toDto(SyndEntry entry) {
        String description = entry.getDescription() != null
                ? entry.getDescription().getValue()
                : "";
        return new ArticleDto(entry.getTitle(), entry.getLink(), description, entry.getPublishedDate());
    }

    public List<ArticleDto> getArticlesBySubject(String subject, int limit) {

        if (limit <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 1");
        }

        return feedCatalog.getFeeds(subject).stream()
                .flatMap(url -> {
                    try {
                        return getArticles(url, limit).stream();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .limit(limit)
                .toList();
    }
}