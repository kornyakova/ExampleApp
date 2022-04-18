package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.db.Articles;

@Repository
public interface ArticleRepository extends JpaRepository<Articles, Integer>{

	List<Articles> findByNewsSite(String news_site);

}
