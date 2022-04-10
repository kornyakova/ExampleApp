package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ArticleRepository;
import com.example.demo.db.Articles;
import com.example.demo.model.Article;

@Service
public class ArticleService {
	
	private static ArticleRepository articleRepository;
	
	@Autowired
	public ArticleService(final ArticleRepository articleRepository) {
		ArticleService.articleRepository = articleRepository;
	}

	public ArticleService() {
	}

	private List<Articles> parseMap(Map<String, List<Article>> arts) {
		
		List<Articles> listOfValues = new ArrayList<Articles>();
		for (Entry<String, List<Article>> set :
            arts.entrySet()) {
			for (Article a: set.getValue()) {
				Articles articles = new Articles(a.getId(), a.getTitle(), a.getNewsSite(), 
						a.getPublishedDate(), a.getArticle());
				listOfValues.add(articles);
			}
       }
		return listOfValues;
	}

	public void saveAll(Map<String, List<Article>> articlesByNewsSite) {
		try {
			List<Articles> articles = parseMap(articlesByNewsSite);
			try {
				articles = articleRepository.saveAll(articles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
