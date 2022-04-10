package com.example.demo.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Articles {
	
	@Id
	private Integer id;
	private String title;
	private String newsSite;
	private String publishedAt;
	@Column(columnDefinition="text")
	private String article;
	
	public Articles(Integer id, String title, String newsSite, String publishedAt, String article) {
		super();
		this.id = id;
		this.title = title;
		this.newsSite = newsSite;
		this.publishedAt = publishedAt;
		this.article = article;
	}
	public Articles() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNewsSite() {
		return newsSite;
	}
	public void setNewsSite(String newsSite) {
		this.newsSite = newsSite;
	}
	public String getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", newsSite=" + newsSite + ", publishedAt=" + publishedAt
				+ ", article=" + article + "]";
	}

}

