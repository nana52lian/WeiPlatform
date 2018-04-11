package com.yidatec.weixin.listeners;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yidatec.weixin.util.PropertiesUtil;

public class WebConfigListener implements ServletContextListener{

	public void contextInitialized(ServletContextEvent arg0) {
		PropertiesUtil.ppsConfig = new Properties();		
		try {
			// 加载系统配置属性文件
            String path = (getClass().getClassLoader().getResource("").toURI()).getPath();
			FileInputStream fis = new FileInputStream(path + "config.properties");
			PropertiesUtil.ppsConfig.load(fis);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}
