package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.GlobalProperties;
import com.example.demo.dao.ArticleRepository;
import com.example.demo.model.Article;
import com.example.demo.db.Articles;
import com.example.service.ArticleService;
import com.google.gson.Gson;

@Controller
public class ArticleController {
	
	@Autowired
	ArticleRepository repo;
	public static Map<String, List<Article>> articlesByNewsSite  = new HashMap<String, List<Article>>();
	private final static Gson GSON = new Gson();

	private GlobalProperties global;
	
	 @Autowired
	 public void setGlobal(GlobalProperties global) {
		 this.global = global;
	 }
	
	@RequestMapping("/")
	public String home() {	
		loadArticles();		
		return "home.jsp";		
	}
	
	public void loadArticles() {
		HttpClient client = HttpClient.newHttpClient();
		UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(global.getUrl())
                .buildAndExpand(global.getLimitForArticles(), "0");

		String urlString = uri.toUriString();
		HttpRequest request = HttpRequest.newBuilder()
		      .uri(URI.create(urlString))
		      .build();
		HttpResponse<String> response = null;
		
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		final Article[] articles = GSON.fromJson(response.body(), Article[].class);
		Map<String, List<Article>> articlesBySite = Stream.of(articles)
                .filter(article -> !containsAny(article.getTitle(), global.getBlackWords()))
                .sorted((a1, a2) -> a1.getPublishedDate().compareTo(a2.getPublishedDate()))
                .collect(Collectors.groupingBy(Article::getNewsSite));
		
		articlesByNewsSite = Stream.of(articlesByNewsSite, articlesBySite)
                .flatMap(map -> map.entrySet().stream())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (strings, strings2) -> {
                                    List<Article> newList = new ArrayList<>();
                                    newList.addAll(strings);
                                    newList.addAll(strings2);
                                    return newList;
                                }
                        )
                );
		ArticleService articleService = new ArticleService(repo);
		articleService.saveAll(articlesByNewsSite);
	}
	
	public static boolean containsAny(String str, ArrayList<String> names){
		boolean result = false;	     
	    for (String word: names) {
	    	boolean found = str.toLowerCase().contains(word.toLowerCase());
	        if (found) {result = found; break;}
	     }
	     return result;
	  }
	
	@RequestMapping("/getArticles")
	public ModelAndView getArticles() throws InterruptedException, ExecutionException {	
		ModelAndView mv = new ModelAndView("showArticles.jsp");
		Executor taskExecutor = Executors.newFixedThreadPool(global.getThreadPool());
		CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {

			@Override
			public void run() {
				List<Articles> articles = repo.findAll();
				mv.addObject("articles", articles);			
			}
			
		}, taskExecutor);
		
		runAsync.get();
		return mv;
	}
	
	@RequestMapping("/getArticleById")
	public ModelAndView getArticleById(@RequestParam int id) {		
		ModelAndView mv = new ModelAndView("showArticle.jsp");
		Articles article = repo.findById(id).orElse(new Articles());
		System.out.println(article.toString());
		mv.addObject("article", article);
		return mv;
	}
	
	@RequestMapping("/getArticleByNewsSite")
	public ModelAndView getArticleByNewsSite(@RequestParam String news_site) throws InterruptedException, ExecutionException {
		ModelAndView mv = new ModelAndView("showArticles.jsp");
		Executor taskExecutor = Executors.newFixedThreadPool(global.getThreadPool());
		CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {

			@Override
			public void run() {
				List<Articles> articles = repo.findByNewsSite(news_site);
				mv.addObject("articles", articles);			
			}
			
		}, taskExecutor);
		
		runAsync.get();
		return mv;
	}

}
