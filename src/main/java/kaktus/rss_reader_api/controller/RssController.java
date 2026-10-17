package kaktus.rss_reader_api.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kaktus.rss_reader_api.dto.ArticleDto;
import kaktus.rss_reader_api.service.RssService;

@RestController
public class RssController {

    private final RssService rssService;

    public RssController(RssService rssService) {
        this.rssService = rssService;
    }

    @GetMapping("/rss")
    public List<ArticleDto> getRssFeed(@RequestParam String url, @RequestParam(defaultValue = "5") int limit) {
        try {
            return rssService.getArticles(url, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(new ArticleDto("Error", "", "Error fetching RSS feed", new Date()));
        }
    }
}