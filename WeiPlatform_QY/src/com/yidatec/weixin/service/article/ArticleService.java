package com.yidatec.weixin.service.article;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.dao.article.ArticleDao;

public class ArticleService {
	
	ArticleDao articleDao = null;
	
	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	/**
	 * 加载文章分类
	 */
	public List<Map<String, Object>> loadSections() throws Exception {
		return articleDao.loadSections();
	}
	
	/**
	 * 加载文章子类
	 */
	public List<Map<String, Object>> loadCategories(JSONObject jsonObj) throws Exception {
		return articleDao.loadCategories(jsonObj);
	}
	
	/**
	 * 加载文章子类数量
	 */
	public int loadCategoriesCount(JSONObject jsonObj) throws Exception {
		return articleDao.loadCategoriesCount(jsonObj);
	}
	
	/**
	 * 加载文章
	 */
	public List<Map<String, Object>> loadArticles(JSONObject jsonObj) throws Exception {
		return articleDao.loadArticles(jsonObj);
	}
	
	/**
	 * 加载文章
	 */
	public int loadArticlesCount(JSONObject jsonObj) throws Exception {
		return articleDao.loadArticlesCount(jsonObj);
	}
	
	/**
	 * 根据文章ID加载文章
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadArticleById(String req_params) throws Exception {
		JSONObject jsonObj = JSONObject.fromObject(req_params);
		return articleDao.loadArticleById(jsonObj);
	}
	
	/**
	 * 处理文章分类的增、删、改
	 * @param req_params
	 * @throws Exception
	 */
	public EnumRes sectionAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int action_no = jsonObject.optInt("action_no");
		int res = 0;
		switch (action_no) {
			case 1: //新增
				res = articleDao.addSection(jsonObject);
				break;
			case 2: //修改
				res = articleDao.updateSection(jsonObject);
				break;
			case 3: //删除
				res = articleDao.deleteSection(jsonObject);
				break;			
		}
		if (res > 0) {
			return EnumRes.SUCCESS;
		} else {
			return EnumRes.FAILED;
		}
	}
	
	/**
	 * 处理文章子类的增、删、改
	 * @param req_params
	 * @throws Exception
	 */
	public EnumRes categoryAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int action_no = jsonObject.optInt("action_no");
		int res = 0;
		switch (action_no) {
			case 1: //新增
				res = articleDao.addCategory(jsonObject);
				break;
			case 2: //修改
				res = articleDao.updateCategory(jsonObject);
				break;
			case 3: //删除
				res = articleDao.deleteCategory(jsonObject);
				break;			
		}
		if (res > 0) {
			return EnumRes.SUCCESS;
		} else {
			return EnumRes.FAILED;
		}
	}	
	
	/**
	 * 处理文章的增、删、改
	 * @param req_params
	 * @throws Exception
	 */
	public EnumRes articleAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int action_no = jsonObject.optInt("action_no");
		int res = 0;
		switch (action_no) {
			case 1: //新增
				res = articleDao.addArticle(jsonObject);
				break;
			case 2: //修改
				res = articleDao.updateArticle(jsonObject);
				break;
			case 3: //删除
				res = articleDao.deleteArticle(jsonObject);
				break;
			case 4: //发布、停止发布
				res = articleDao.publishArticle(jsonObject);
				break;
				
		}
		if (res > 0) {
			return EnumRes.SUCCESS;
		} else {
			return EnumRes.FAILED;
		}
	}
}
