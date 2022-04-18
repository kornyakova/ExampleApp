package com.example.service;

import java.util.ArrayList;
import java.util.List;

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
	
	public void saveAll(List<Article> articlesByNewsSite) {
		try {
			List<Articles> articles = parseList(articlesByNewsSite);
			try {
				articles = articleRepository.saveAll(articles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private List<Articles> parseList(List<Article> articlesByNewsSite) {
		List<Articles> listOfValues = new ArrayList<Articles>();
		for(Article a: articlesByNewsSite) {
			Articles articles = new Articles(a.getId(), a.getTitle(), a.getNewsSite(), 
					a.getPublishedDate(), a.getArticle());
			listOfValues.add(articles);
		}
		return listOfValues;
	}

}
