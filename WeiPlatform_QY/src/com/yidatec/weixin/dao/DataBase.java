package com.yidatec.weixin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.util.CommonMethod;
import com.yidatec.weixin.util.PropertiesUtil;

/**
 * get database connection
 * @author Lance
 *
 */
public class DataBase {	
	
	public JdbcTemplate jdbcTemplate = null;
	
	public final static String SYSTEM_USER = "system";
	
	public final static String TABLE_PREFIX = "wei_";
	
	public final static String PROJECT_NAME = PropertiesUtil.ppsConfig.getProperty("PROJECT_NAME");
	
	public final static String IMAGE_PATH = PropertiesUtil.ppsConfig.getProperty("IMAGE_PATH");
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * get guid
	 * @return
	 */
	public String getGUID() {
		return java.util.UUID.randomUUID().toString();
	}
	
	public String getTableName(String tableName) {
		return TABLE_PREFIX + tableName;
	}
	
	/**
	 * 获取上传图片路径
	 * @param img_url
	 * @return
	 */
	public static String getImageUrl(String img_url) {
		if (null == img_url || img_url.isEmpty()) {
			return "";
		}
		return "/" + PROJECT_NAME + IMAGE_PATH + img_url;
	}
	
	/**
	 * 在全路径中获取图片名称
	 * @param img_url
	 * @return
	 */
	public static String getImageName(String img_url) {
		return img_url.substring(img_url.lastIndexOf("/")+ 1);
	}
	
	/**
	 * 获得当前时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String getCurrentDate() {
		return CommonMethod.getStringDateLong();
	}
	
	/**
	 * 获取当前登录用户的信息
	 * @return
	 */
	public PlatformUserEntity getCurrentUser() {
		SecurityContext ctx = SecurityContextHolder.getContext(); 
		Authentication auth = ctx.getAuthentication(); 
		if (auth.getPrincipal() instanceof PlatformUserEntity) {
			return (PlatformUserEntity) auth.getPrincipal();
		}
		return new PlatformUserEntity();
	}
	
	/**
	 * 获取指定类型的流水号
	 * @param noType
	 * @return
	 * @throws Exception
	 */
	public String getSerialNo(SerialNoType noType) throws Exception {
		// 取ID
		String objectID = this.getObjectID(noType);
		// 更新ID
		int res = this.updateObjectID(objectID, noType);
		if (res > 0) {
			return CommonMethod.getStringDatePrefix() + objectID;
		}
		return null;
	}
	
	/**
	 * 获取指定类型的单据号
	 * @param noType
	 * @return
	 * @throws Exception
	 */
	public String getObjectID(SerialNoType noType) throws Exception {
		String sql = "select serialno from " + getTableName("autono") + " where type = ? ";
		@SuppressWarnings("unchecked")		
		List<String> serialNo = jdbcTemplate.query(sql, new Object[]{noType.getType()}, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getString("serialno");
			}
		});		
		if (serialNo.size() > 0) {
			return getNextSerialNo(serialNo.get(0));			
		}
		return null;
	}
	
	/**
	 * 更新流水号
	 * @param objectID
	 * @param noType
	 * @return
	 * @throws Exception
	 */
	public int updateObjectID(String objectID, SerialNoType noType) throws Exception {
		String sql = "update " + getTableName("autono") + " set serialno = ? where type = ? ";
		return jdbcTemplate.update(sql, new Object[]{ objectID, noType.getType() });
	}
			
	/**
	 * 48～57为0到9十个阿拉伯数字
	 * 65～90为26个大写英文字母
	 * 97～122号为26个小写英文字母
	 * @param oldSerialNo
	 * @return
	 */
	public String getNextSerialNo(String oldSerialNo) {
		byte _oldSerialNo[] = oldSerialNo.getBytes();
		int i = _oldSerialNo.length - 1;
		do
		{
			if (i < 0)	
				break;
			byte byte0 = (byte)(_oldSerialNo[i] + 1);
			boolean flag = false;
			byte byte1 = byte0;
			if (byte0 > 57) {
		        byte1 = 48;
		        flag = true;
			}
			if (byte1 == 58)
				byte1 = 48;
			_oldSerialNo[i] = byte1;
			if (!flag)
				break;
			i--;
		} while (true);
		return new String(_oldSerialNo);
	}
	
	/**
	 * 自动生成ID的类型
	   1	任务单流水号		201308030000000001
	 * @author Lance
	 */
	public enum SerialNoType{
		/** 任务单流水号 */
	    TASK_ID(1),
	    ;
	    
	    private final int type;
	    
	    private SerialNoType(int type) {
		     this.type = type;
		}
	    
	    public int getType() {
	    	return type;
	    }

	}
	
}
