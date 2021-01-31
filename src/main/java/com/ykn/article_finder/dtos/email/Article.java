package com.ykn.article_finder.dtos.email;

import com.ykn.article_finder.dtos.googlesearch.SiteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Article {

    String title;

    String url;

    public Article(SiteResponse siteResponse){
        this.title = siteResponse.getTitle();
        this.url = siteResponse.getLink();
    }

}
