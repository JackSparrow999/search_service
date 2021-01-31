package com.ykn.article_finder.dtos.googlesearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GoogleSearchResponse {

    List<SiteResponse> items;

}
