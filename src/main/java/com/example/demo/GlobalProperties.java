package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class GlobalProperties {
	private Integer threadPool;
	private Integer limitForArticles;
	private String url;
	private Integer countOfArticlesForOneThread;
    private ArrayList<String> blackWords = new ArrayList<>();
    
	public Integer getThreadPool() {
		return threadPool;
	}
	public void setThreadPool(Integer threadPool) {
		this.threadPool = threadPool;
	}
	public ArrayList<String> getBlackWords() {
		return blackWords;
	}
	public void setBlackWords(ArrayList<String> blackWords) {
		this.blackWords = blackWords;
	}
	public Integer getLimitForArticles() {
		return limitForArticles;
	}
	public void setLimitForArticles(Integer limitForArticles) {
		this.limitForArticles = limitForArticles;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getCountOfArticlesForOneThread() {
		return countOfArticlesForOneThread;
	}
	public void setCountOfArticlesForOneThread(Integer countOfArticlesForOneThread) {
		this.countOfArticlesForOneThread = countOfArticlesForOneThread;
	}	
	
}
