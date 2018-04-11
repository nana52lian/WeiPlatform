package com.yidatec.weixin.action.article;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.action.BaseAction;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.service.article.ArticleService;

public class ArticleAction extends BaseAction {
	
	private static final Logger log = LogManager.getLogger(ArticleAction.class);
	
	ArticleService articleService = null;
	
	// 文章分类
	private String section = null;
	
	// 文章子类
	private String category = null;
	
	/**
	 * 加载文章分类
	 */
	public void loadSections() {
		try {
			sendJson(JSONArray.fromObject(articleService.loadSections()).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载文章子类
	 */
	public void loadCategories() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("section", section);
			jsonObj.put("offset", getOffset());
			jsonObj.put("rows", getRows());			
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", articleService.loadCategoriesCount(jsonObj));
			jsonMap.put("rows", articleService.loadCategories(jsonObj));
			sendJson(JSONObject.fromObject(jsonMap).toString());			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载文章子类
	 */
	public void loadCategoriesBySection() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("section", section);
			sendJson(JSONArray.fromObject(articleService.loadCategories(jsonObj)).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载文章
	 */
	public void loadArticles() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("section", section);
			jsonObj.put("category", category);
			jsonObj.put("offset", getOffset());
			jsonObj.put("rows", getRows());
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", articleService.loadArticlesCount(jsonObj));
			jsonMap.put("rows", articleService.loadArticles(jsonObj));
			sendJson(JSONObject.fromObject(jsonMap).toString());			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 根据文章ID加载文章
	 */
	public void loadArticleById() {
		try {
			Map<String, Object> article = articleService.loadArticleById(req_params);
			if (article == null) {
				ResponseEntity res = new ResponseEntity();
				res.setRes(EnumRes.FAILED);
				sendJson(JSONObject.fromObject(res).toString());
			} else {
				sendJson(JSONObject.fromObject(article).toString());
			}						
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 处理文章分类的增、删、改
	 */
	public void sectionAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setRes(articleService.sectionAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 处理文章子类的增、删、改
	 */
	public void categoryAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setRes(articleService.categoryAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 处理文章的增、删、改、发布
	 */
	public void articleAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setRes(articleService.articleAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

}
