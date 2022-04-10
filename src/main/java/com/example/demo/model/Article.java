package com.example.demo.model;
import java.io.IOException;
import java.io.Serializable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Article implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String newsSite;
	private String publishedAt;
	private String url;
	
	
	public Article(int id, String title, String newsSite, String publishedAt, String url) {
		this.id = id;
		this.title = title;
		this.newsSite = newsSite;
		this.publishedAt = publishedAt;
		this.url = url;
		
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setNewsSite(String news_site) {
		this.newsSite = news_site;
	}
	
	public String getNewsSite() {
		return this.newsSite;
	}
	
	public void setPublishedDate(String publishedAt) {
		this.publishedAt = publishedAt;
	}
	
	public String getPublishedDate() {
		return this.publishedAt;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getArticle() {
		try {
			Document doc = Jsoup.connect(this.getUrl()).get();
			Elements synopsis = doc.select("p");
			return synopsis.text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
