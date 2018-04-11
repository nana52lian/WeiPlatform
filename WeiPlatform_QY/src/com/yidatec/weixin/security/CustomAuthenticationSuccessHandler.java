package com.yidatec.weixin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.yidatec.weixin.dao.seatmgr.AdviserManageDao;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final Logger log = LogManager.getLogger(CustomAuthenticationSuccessHandler.class);
		
	@Autowired
	private AdviserManageDao adviserManageDao;
	
	public AdviserManageDao getAdviserManageDao() {
		return adviserManageDao;
	}

	public void setAdviserManageDao(AdviserManageDao adviserManageDao) {
		this.adviserManageDao = adviserManageDao;
	}

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        // TODO: 处理验证成功后的操作
		//更新登陆时间
		try {
			adviserManageDao.updateLoginTime();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		super.onAuthenticationSuccess(request, response, authentication);
    }

}
