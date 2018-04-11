package com.yidatec.weixin.entity.message;

import java.util.List;

public class ArticleCatagoryEntity {

   private String catagory_id;
	
	private String name;
	
	private List<ArticleEntity> articleList;

	public String getCatagory_id() {
		return catagory_id;
	}

	public void setCatagory_id(String catagory_id) {
		this.catagory_id = catagory_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ArticleEntity> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<ArticleEntity> articleList) {
		this.articleList = articleList;
	}
}
