package kaktus.rss_reader_api.dto;

import java.util.Date;

public record ArticleDto(String title, String link, String description, Date publishedDate) {
    
}
