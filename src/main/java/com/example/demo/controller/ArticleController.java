package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.GlobalProperties;
import com.example.demo.consumer.Consumer;
import com.example.demo.dao.ArticleRepository;
import com.example.demo.thread.MyThread;
import com.example.demo.db.Articles;
import com.example.demo.queue.ArticleQueue;

@Controller
public class ArticleController {
	
	@Autowired
	ArticleRepository repo;
	private GlobalProperties global;

	private AtomicBoolean stop = new AtomicBoolean(false);


	 @Autowired
	 public void setGlobal(GlobalProperties global) {
		 this.global = global;
	 }
	
	@RequestMapping("/")
	public String home() throws InterruptedException, ExecutionException {	
		loadArticles();
		return "home.jsp";		
	}
	
	public void loadArticles() {
		int numberOfTasks = global.getThreadPool();
        ExecutorService executor= Executors.newFixedThreadPool(global.getThreadPool());

		ArticleQueue dataQueue = new ArticleQueue(global.getLimitForArticles());

		Consumer consumer = new Consumer(dataQueue, stop);
		consumer.setBlackWords(global.getBlackWords());
		consumer.setRepo(repo);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
        try{
            for (int i = 0; i < numberOfTasks; i++){
            	int skipped_articles = i * global.getCountOfArticlesForOneThread();
    			UriComponents uri = UriComponentsBuilder
    	                .fromHttpUrl(global.getUrl())
    	                .buildAndExpand(global.getCountOfArticlesForOneThread(), skipped_articles);
    			String urlString = uri.toUriString();
                executor.execute(new MyThread(urlString, dataQueue));                
            }
        }catch(Exception err){
            err.printStackTrace();
        }
        executor.shutdown();
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
		List<Articles> articles = repo.findAll();
		mv.addObject("articles", articles);
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
		List<Articles> articles = repo.findByNewsSite(news_site);
		mv.addObject("articles", articles);	
		return mv;
	}

}
