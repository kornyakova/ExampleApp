package com.example.demo.thread;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.example.demo.queue.ArticleQueue;

public class MyThread implements Runnable{
    private String url;
    private final ArticleQueue dataQueue;
    
    public MyThread(String url, ArticleQueue dataQueue){
        this.url = url;
        this.dataQueue = dataQueue;
    }
    public void run(){
        try{ 
        	HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create(url))
			      .build();
			
			HttpResponse<String> response = null;
		    try {
				response = client.send(request, BodyHandlers.ofString());
				dataQueue.add(response.body());
	            dataQueue.notifyAllForEmpty(); 
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
        }catch(Exception err){
            err.printStackTrace();
        }
    }
}