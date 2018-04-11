package com.yidatec.weixin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.security.SecurityDao;
import com.yidatec.weixin.dao.sysmgr.ResourceDao;
import com.yidatec.weixin.entity.ParamEntity;
import com.yidatec.weixin.message.WeixinHelper;
import com.yidatec.weixin.security.tool.AntUrlPathMatcher;
import com.yidatec.weixin.security.tool.UrlMatcher;

/**
 * 朄1�7核心的地方，就是提供某个资源对应的权限定义，
 * 即getAttributes方法返回的结果�1�7�1�7 此类在初始化时，应该取到扄1�7有资源及其对应角色的定义〄1�7
 * 
 */
@Service
public class CustomInvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource {

	private static final Logger log = LogManager
			.getLogger(CustomInvocationSecurityMetadataSourceService.class);

	@Autowired
	private SecurityDao securityDao;
	
	private ResourceDao resourceDao;

	private UrlMatcher urlMatcher = new AntUrlPathMatcher();

	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public CustomInvocationSecurityMetadataSourceService() {
		// loadResourceDefine();
	}

	private void loadResourceDefine() {
		try {
			/*
			 * //从ClassPath路径下获取指定文件名的配置文件，返回Resource Resource resource = new
			 * ClassPathResource("applicationContext-service.xml");
			 * //通过XmlBeanFactory解析该文件，获取BeanFactory BeanFactory factory = new
			 * XmlBeanFactory(resource); //获取指定的bean。这里的jdbcTemplate是bean元素的id
			 * securityDao = (ISecurityDao)factory.getBean("securityDao"); //
			 * 在Web服务器启动时，提取系统中的所有权限�1�7�1�7
			 */
			List<String> authorities = securityDao.loadAuthorities();
			/*
			 * 应当是资源为key＄1�7 权限为value〄1�7 资源通常为url＄1�7 权限就是那些以ROLE_为前缄1�7的角色�1�7�1�7
			 * 丄1�7个资源可以由多个权限来访问�1�7�1�7
			 */
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			// 根据权限获取资源
			for (String auth_name : authorities) {
				ConfigAttribute ca = new SecurityConfig(auth_name);
				List<String> resources = securityDao
						.loadResourcesByAuthorityName(auth_name);
				for (String res : resources) {
					String url = res;
					/*
					 * 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要�1�7�过该url为key提取出权限集合，将权限增加到权限集合丄1�7
					 * 〄1�7
					 */
					if (resourceMap.containsKey(url)) {
						Collection<ConfigAttribute> value = resourceMap
								.get(url);
						value.add(ca);
						resourceMap.put(url, value);
					} else {
						Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
						atts.add(ca);
						resourceMap.put(url, atts);
					}
				}
			}
			System.out.println("load resource ok!");
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}
	
	/**
	 * 加载全局参数
	 * @throws Exception 
	 */
	public void loadParams() {
		try {
			List<ParamEntity> paramList = resourceDao.loadParams();
			Map<String, ParamEntity> params = new HashMap<String, ParamEntity>();
			for (ParamEntity param : paramList) {
				params.put(param.getParam_name(), param);
			}
			ParamsConfig.setParams(params);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}	

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	// 根据URL，找到相关的权限配置〄1�7
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		if (null == resourceMap) {
			return null;
		}
		// object 是一个URL，被用户请求的url〄1�7
		String url = ((FilterInvocation) object).getRequestUrl();
		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.pathMatchesUrl(url, resURL)) {
				return resourceMap.get(resURL);
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	public SecurityDao getSecurityDao() {
		return securityDao;
	}

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
		// 注入后就加载资源
		loadResourceDefine();
		// 初始化TOKEN
		//WeixinHelper msgHelper = new WeixinHelper();
		//WeixinHelper.ACCESS_TOKEN = msgHelper.getAccessToken();		
	}
	
	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
		// 注入后加载全局参数
		try {
			loadParams();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 获取资源MAP["/index.jsp":AUTH_XXX]
	 * 
	 * @return
	 */
	public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	public static void setResourceMap(
			Map<String, Collection<ConfigAttribute>> resourceMap) {
		CustomInvocationSecurityMetadataSourceService.resourceMap = resourceMap;
	}
}
