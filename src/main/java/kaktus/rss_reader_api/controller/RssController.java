package kaktus.rss_reader_api.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import kaktus.rss_reader_api.dto.ArticleDto;
import kaktus.rss_reader_api.service.FeedCatalog;
import kaktus.rss_reader_api.service.RssService;

@RestController
public class RssController {

    private final RssService rssService;

    private final FeedCatalog feedCatalog;

    public RssController(RssService rssService, FeedCatalog feedCatalog) {
        this.rssService = rssService;
        this.feedCatalog = feedCatalog;
    }

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

    @GetMapping ("rss/subjects")
    public List<ArticleDto> getArticlesBySubject(@RequestParam String subject, @RequestParam(defaultValue = "5") int limit) {

        if (limit <= 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be greater than 1");
        }

        return rssService.getArticlesBySubject(subject, limit);
        
    }
}
