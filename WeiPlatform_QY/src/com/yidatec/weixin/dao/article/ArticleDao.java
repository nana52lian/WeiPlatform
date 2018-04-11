package com.yidatec.weixin.dao.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.util.CommonMethod;

public class ArticleDao extends DataBase {
	
	/**
	 * 加载文章分类
	 */
	public List<Map<String, Object>> loadSections() throws Exception {
		String sql = " select * from " + getTableName("article_sections") + " order by create_date";
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list_sections = jdbcTemplate.query(sql, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", rs.getString("id"));				
				value.put("code", rs.getString("code"));				
				value.put("name", rs.getString("name"));				
				return value;
			}
		});
		return list_sections;
	}
	
	/**
	 * 加载文章子类
	 */
	public List<Map<String, Object>> loadCategories(JSONObject jsonObj) throws Exception {
		String section = jsonObj.optString("section");
		int offset = jsonObj.optInt("offset");
		int rows = jsonObj.optInt("rows") == 0 ? 100 : jsonObj.optInt("rows");		
		String sql = " select a.*, b.name section_name from " + getTableName("article_catagories") + " a "
				   + " left join " + getTableName("article_sections") + " b "
				   + " on a.section_id = b.id "
				   + " where 1=1 ";
		Object[] params = null;
		if (section.length() > 0) {
			sql = sql + " and a.section_id = ? ";
			params = new Object[]{ section, offset, rows };
		} else {
			params = new Object[]{ offset, rows };
		}
		sql = sql + " order by create_date desc limit ?, ? ";
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list_categories = jdbcTemplate.query(sql,
				params,
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", rs.getString("id"));
				value.put("name", rs.getString("name"));
				value.put("section_id", rs.getString("section_id"));
				value.put("section_name", rs.getString("section_name"));
				value.put("create_date", rs.getString("create_date").replace(".0", ""));
				return value;
			}
		});
		return list_categories;
	}
	
	/**
	 * 加载文章子类
	 */
	public int loadCategoriesCount(JSONObject jsonObj) throws Exception {
		String section = jsonObj.optString("section");		
		String sql = " select count(a.id) from " + getTableName("article_catagories") + " a "
				   + " left join " + getTableName("article_sections") + " b "
				   + " on a.section_id = b.id "
				   + " where 1=1 ";
		Object[] params = null;
		if (section.length() > 0) {
			sql = sql + " and a.section_id = ? ";
			params = new Object[]{ section };
		}		
		return jdbcTemplate.queryForInt(sql, params);
	}
	
	/**
	 * 加载文章
	 */
	public List<Map<String, Object>> loadArticles(JSONObject jsonObj) throws Exception {
		String section = jsonObj.optString("section");
		String category = jsonObj.optString("category");
		String published = jsonObj.optString("published");
		String title = jsonObj.optString("title");
		int offset = jsonObj.optInt("offset");
		int rows = jsonObj.optInt("rows") == 0 ? 10 : jsonObj.optInt("rows");
		List<Object> params = new ArrayList<Object>();		
		String sql = " select a.*, b.`name` section, c.id category_id, c.`name` category from " 
				   + getTableName("article") + " a "
				   + " left join " + getTableName("article_sections") + " b "
				   + " on a.section_id = b.id "
				   + " left join " + getTableName("article_catagories") + " c "
				   + " on a.catagory_id = c.id "
				   + " where 1=1 ";
		if (section.length() > 0) {
			sql = sql + " and a.section_id = ? ";
			params.add(section);
		}
		if (category.length() > 0) {
			sql = sql + " and a.catagory_id = ? ";
			params.add(category);
		}
		if (published.length() > 0) {
			sql = sql + " and a.status = 1 ";
		}
		if (title.length() > 0) {
			sql = sql + " and a.title like ? ";
			params.add("%" + title + "%");
		}
		sql = sql + " order by create_date desc limit ?, ? ";
		params.add(offset);
		params.add(rows);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list_Articles = jdbcTemplate.query(sql,
				params.toArray(),
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", rs.getString("id"));
				value.put("title", rs.getString("title"));
				value.put("writer", rs.getString("writer"));				
				value.put("section", rs.getString("section"));
				value.put("category_id", rs.getString("category_id"));
				value.put("category", rs.getString("category"));
				value.put("summary", rs.getString("summary"));
				value.put("status", rs.getInt("status"));
				value.put("create_date", rs.getString("create_date").replace(".0", ""));
				return value;
			}
		});
		return list_Articles;
	}
	
	/**
	 * 加载文章数量
	 */
	public int loadArticlesCount(JSONObject jsonObj) throws Exception {
		String section = jsonObj.optString("section");
		String category = jsonObj.optString("category");
		String published = jsonObj.optString("published");
		String title = jsonObj.optString("title");
		List<Object> params = new ArrayList<Object>();		
		String sql = " select count(a.id) from " + getTableName("article") + " a "
				   + " left join " + getTableName("article_sections") + " b "
				   + " on a.section_id = b.id "
				   + " left join " + getTableName("article_catagories") + " c "
				   + " on a.catagory_id = c.id "
				   + " where 1=1 ";
		if (section.length() > 0) {
			sql = sql + " and a.section_id = ? ";
			params.add(section);
		}
		if (category.length() > 0) {
			sql = sql + " and a.catagory_id = ? ";
			params.add(category);
		}
		if (published.length() > 0) {
			sql = sql + " and a.status = 1 ";
		}
		if (title.length() > 0) {
			sql = sql + " and a.title like ? ";
			params.add("%" + title + "%");
		}
		return jdbcTemplate.queryForInt(sql, params.toArray());
	}
	
	/**
	 * 根据文章ID加载文章
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadArticleById(JSONObject jsonObj) throws Exception {
		String sql = " select a.*, b.`name` section, c.`name` category from " + getTableName("article") + " a "
		   		   + " left join " + getTableName("article_sections") + " b "
		   		   + " on a.section_id = b.id "
		   		   + " left join " + getTableName("article_catagories") + " c "
		   		   + " on a.catagory_id = c.id "
		   		   + " where a.id = ? ";
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list_Articles = jdbcTemplate.query(sql,
				new Object[]{ jsonObj.optString("id") },
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", rs.getString("id"));
				value.put("title", rs.getString("title"));
				value.put("writer", rs.getString("writer"));				
				value.put("section_id", rs.getString("section_id"));
				value.put("section", rs.getString("section"));
				value.put("catagory_id", rs.getString("catagory_id"));
				value.put("category", rs.getString("category"));
				value.put("content", rs.getString("content"));
				value.put("summary", rs.getString("summary"));				
				value.put("status", rs.getInt("status"));
				value.put("create_date", rs.getString("create_date").replace(".0", ""));
				return value;
			}
		});
		if (list_Articles.size() > 0)
			return list_Articles.get(0);		
		
		return null;
	}
	
	/**
	 * 新增分类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addSection(JSONObject jsonObject) throws Exception {
		String sql = " insert into " + getTableName("article_sections")
				   + " (id, code, name, create_date,create_user) "
				   + " values(?,?,?,?,?) ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					getGUID(),
					jsonObject.optString("code"),
					jsonObject.optString("name"),
					getCurrentDate(),
					getCurrentUser().getId()
				});
	}
	
	/**
	 * 新增子类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addCategory(JSONObject jsonObject) throws Exception {
		String sql = " insert into " + getTableName("article_catagories")
				   + " (id, name, section_id, create_date,create_user) "
				   + " values(?,?,?,?,?) ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					getGUID(),
					jsonObject.optString("name"),
					jsonObject.optString("section"),
					getCurrentDate(),
					getCurrentUser().getId()
				});
	}
	
	
	/**
	 * 新增文章
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addArticle(JSONObject jsonObject) throws Exception {
		String sql = " insert into " + getTableName("article")
				   + " (id, section_id, catagory_id, title, content, summary, status, writer, create_date, create_user) "
				   + " values(?,?,?,?,?, ?,?,?,?,?) ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					getGUID(),
					jsonObject.optString("section"),
					jsonObject.optString("category"),
					jsonObject.optString("title"),
					jsonObject.optString("content"),
					jsonObject.optString("content"), // summary
					jsonObject.optInt("status"),
					jsonObject.optString("writer"),
					getCurrentDate(),
					getCurrentUser().getId()
				});
	}
	
	
	/**
	 * 更新分类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateSection(JSONObject jsonObject) throws Exception {
		String sql = " update "	+ getTableName("article_sections")
				   + " set code = ?, name = ? ,modify_date = ?, modify_user = ? "
				   + " where id = ? ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					jsonObject.optString("code"),
					jsonObject.optString("name"),
					getCurrentDate(),
					getCurrentUser().getId(),
					jsonObject.optString("id")
				});
	}
	
	/**
	 * 更新分类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateCategory(JSONObject jsonObject) throws Exception {
		String sql = " update "	+ getTableName("article_catagories")
				   + " set name = ?, section_id = ? ,modify_date = ?, modify_user = ? "
				   + " where id = ? ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					jsonObject.optString("name"),
					jsonObject.optString("section"),
					getCurrentDate(),
					getCurrentUser().getId(),
					jsonObject.optString("id")
				});
	}
	
	/**
	 * 更新文章
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateArticle(JSONObject jsonObject) throws Exception {		
		String sql = " update "	+ getTableName("article")
				   + " set section_id = ?, catagory_id = ?, title = ?, content = ?, "
				   + " summary = ?, status = ?, writer = ?, modify_user = ?, modify_date = ? "
				   + " where id = ? ";	
		return jdbcTemplate.update(sql,
				new Object[] {
					jsonObject.optString("section"),
					jsonObject.optString("category"),
					jsonObject.optString("title"),
					jsonObject.optString("content"),
					jsonObject.optString("content"), // summary
					jsonObject.optInt("status"),
					jsonObject.optString("writer"),
					getCurrentUser().getId(),
					getCurrentDate(),
					jsonObject.optString("id")
				});
	}
	
	/**
	 * 更新文章是否发布
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int publishArticle(final JSONObject jsonObject) throws Exception {
		final String[] ids = jsonObject.optString("ids").split(",");
		String sql = " update "	+ getTableName("article")
		   		   + " set status = ? where id = ? ";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setInt(1, jsonObject.optInt("status"));
						ps.setString(2, ids[index]);
					}
					public int getBatchSize() {
						return ids.length;
					}
				});
		if (CommonMethod.batchUpdateSuccess(res, ids.length)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除分类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteSection(JSONObject jsonObject) throws Exception {
		final String[] ids = jsonObject.optString("ids").split(",");
		String sql = " delete from " + getTableName("article_sections") 
				   + " where id = ?";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, ids[index]);
					}
					public int getBatchSize() {
						return ids.length;
					}
				});
		if (CommonMethod.batchUpdateSuccess(res, ids.length)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除子类
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteCategory(JSONObject jsonObject) throws Exception {
		final String[] ids = jsonObject.optString("ids").split(",");
		String sql = " delete from " + getTableName("article_catagories") 
				   + " where id = ?";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, ids[index]);
					}
					public int getBatchSize() {
						return ids.length;
					}
				});
		if (CommonMethod.batchUpdateSuccess(res, ids.length)) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除文章
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteArticle(JSONObject jsonObject) throws Exception {
		final String[] ids = jsonObject.optString("ids").split(",");
		String sql = " delete from " + getTableName("article") 
				   + " where id = ?";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, ids[index]);
					}
					public int getBatchSize() {
						return ids.length;
					}
				});
		if (CommonMethod.batchUpdateSuccess(res, ids.length)) {
			return 1;
		}
		return 0;
	}
	
	

}
