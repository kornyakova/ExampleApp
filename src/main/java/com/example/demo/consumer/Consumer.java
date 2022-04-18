package com.example.demo.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.dao.ArticleRepository;
import com.example.demo.model.Article;
import com.example.demo.queue.ArticleQueue;
import com.example.service.ArticleService;
import com.google.gson.Gson;

public class Consumer implements Runnable {
    private final ArticleQueue dataQueue;
    private final AtomicBoolean runFlag;
    private Map<String, List<Article>> buffer = new HashMap<String, List<Article>>();
    private final static Gson GSON = new Gson();
    private ArrayList<String> blackWords;
	private ArticleRepository repo;
	public int counter = 0;
	final Timer timer = new Timer();

    public Consumer(ArticleQueue dataQueue, AtomicBoolean runFlag) {
        this.dataQueue = dataQueue;
        this.runFlag = runFlag;
    }
        
    public ArrayList<String> getBlackWords() {
		return blackWords;
	}

	public void setBlackWords(ArrayList<String> blackWords) {
		this.blackWords = blackWords;
	}	

	public ArticleRepository getRepo() {
		return repo;
	}

	public void setRepo(ArticleRepository repo) {
		this.repo = repo;
	}

	@Override
    public void run() {
        consume();
    }

    public void consume() {
        while (!runFlag.get()) {
            String message;
            if (dataQueue.isEmpty()) {
                try {
                    dataQueue.waitOnEmpty();
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (runFlag.get()) {
                saveAllArticles();
                break;
            }
            message = dataQueue.remove();
            dataQueue.notifyAllForFull();
            useMessage(message);
        }
    }
    
    public void stop() {
    	System.out.println("Consumer stopped");
    	runFlag.set(true);
    	saveAllArticles();
    }

	private void saveAllArticles() {
		System.out.println("saveAllArticles");
		for (Entry<String, List<Article>> set :
            buffer.entrySet()) {
			ArticleService articleService = new ArticleService(repo);
			articleService.saveAll(set.getValue());
		}
		buffer.clear();		
	}

	private void useMessage(String message) {
		final Article[] articles = GSON.fromJson(message, Article[].class);
		Map<String, List<Article>> articlesBySite = Stream.of(articles)
                .filter(article -> !containsAny(article.getTitle(), blackWords))
                .sorted((a1, a2) -> a1.getPublishedDate().compareTo(a2.getPublishedDate()))
                .collect(Collectors.groupingBy(Article::getNewsSite));
		
		if (buffer.isEmpty()) {
			buffer.putAll(articlesBySite);
		}else {
			mergeMaps(articlesBySite);
		}
		checkBuffer();	
	}
	
	private void checkBuffer() {
		Set<Entry<String, List<Article>>> setOfEntries = buffer.entrySet(); 
		Iterator<Entry<String, List<Article>>> iterator = setOfEntries.iterator();
		while (iterator.hasNext()) { 
			Entry<String, List<Article>> entry = iterator.next(); 
			if (entry.getValue().size() >= 10) { 
				ArticleService articleService = new ArticleService(repo);
				articleService.saveAll(entry.getValue());
				iterator.remove();
			}
		}
		
		timer.schedule(new Stopper(), 10000);
	}
	
	private class Stopper extends TimerTask {

		@Override
		public void run() {
			if (dataQueue.isEmpty()){
				saveAllArticles();
			}else {
				timer.schedule(new Stopper(), 10000);
			}
		}		
	}


	private void mergeMaps(Map<String, List<Article>> articlesBySite) {
		Map<String, List<Article>>[] arrr = new Map[]{buffer,articlesBySite};
		Map<String, List<Article>> merged = new HashMap<>();
		for(Map<String, List<Article>> input:arrr){
		  for(Entry<String, List<Article>> e:input.entrySet()){
		    merged.merge(e.getKey(), e.getValue(), (v1,v2)->{v1.addAll(v2);return v1;});
		  }
		}
		buffer.clear();
		buffer.putAll(merged);
		merged.clear();		
	}


	public static boolean containsAny(String str, ArrayList<String> names){
		boolean result = false;	     
	    for (String word: names) {
	    	boolean found = str.toLowerCase().contains(word.toLowerCase());
	        if (found) {result = found; break;}
	     }
	     return result;
	}
}
