package kaktus.rss_reader_api.service;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import kaktus.rss_reader_api.dto.ArticleDto;

@Service
public class RssService {

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
}