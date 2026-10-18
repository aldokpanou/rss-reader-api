package kaktus.rss_reader_api.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kaktus.rss_reader_api.dto.ArticleDto;
import kaktus.rss_reader_api.service.FeedCatalog;
import kaktus.rss_reader_api.service.RssService;

@RestController
@Tag (name = "RSS Controller", description = "Endpoints for fetching RSS feeds and articles by subject")
public class RssController {

    private final RssService rssService;

    private final FeedCatalog feedCatalog;

    public RssController(RssService rssService, FeedCatalog feedCatalog) {
        this.rssService = rssService;
        this.feedCatalog = feedCatalog;
    }

    @Operation (summary = "Get RSS feed articles", description = "Fetches articles from the specified RSS feed URL with an optional limit on the number of articles.")
    @GetMapping("/rss")
    public List<ArticleDto> getRssFeed(@RequestParam String url, @RequestParam(defaultValue = "5") int limit) {

        if (limit <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 1");
        }

        try {
            return rssService.getArticles(url, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(new ArticleDto("Error", "", "Error fetching RSS feed", new Date()));
        }
    }

    @Operation (summary = "Get articles by subject", description = "Fetches articles from predefined RSS feeds based on the specified subject with an optional limit on the number of articles.")
    @GetMapping ("rss/subjects")
    public List<ArticleDto> getArticlesBySubject(@RequestParam String subject, @RequestParam(defaultValue = "5") int limit) {

        if (limit <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 1");
        }

        return rssService.getArticlesBySubject(subject, limit);
        
    }

    @Operation (summary = "Get articles from Google News by subject", description = "Fetches articles from Google News based on the specified subject with an optional limit on the number of articles.")
    @GetMapping ("rss/google-news")
    public List<ArticleDto> getArticlesByGoogleNews(@RequestParam String subject, @RequestParam(defaultValue = "5") int limit) {

        if (limit <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 1");
        }

        String googleNewsUrl = "https://news.google.com/rss/search?q=" + subject;

        try {
            return rssService.getArticles(googleNewsUrl, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(new ArticleDto("Error", "", "Error fetching Google News feed", new Date()));
        }
    }
}
