package com.yidatec.weixin.security;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.yidatec.weixin.dao.security.SecurityDao;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.util.Encrypt;

/**
 *该类的主要作用是为Spring Security提供丄1�7个经过用户认证后的UserDetails〄1�7
 *该UserDetails包括用户名�1�7�密码�1�7�是否可用�1�7�是否过期等信息〄1�7 
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger log = LogManager.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private SecurityDao securityDao;	
	
	@Override
	public UserDetails loadUserByUsername(String username) {		
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();		
		// 根据用户名取得一个SysUsers对象，以获取该用户的其他信息〄1�7
		PlatformUserEntity user = null;
		try {
			// 得到用户的权附1�7
			auths = securityDao.loadUserAuthoritiesByName(username);
			user = securityDao.loadUserByAccount(username);
			if (user == null) return new PlatformUserEntity();			
			// 加密的时候用用户名做了salt,这里要移附1�7
			user.setPassword(Encrypt.decryptPwdWithSalt(user.getPassword(), user.getAccount()));
		} catch (Exception e) {			
			log.error(e.getMessage(), e);
			return null;
		}		
		return new PlatformUserEntity(user.getId(),
							  user.getUser_id(),
							  user.getAccount(),
							  user.getPassword(),
							  user.getName(),
							  user.getMail(),
							  user.getMobile_phone(),
							  user.getIssys(),
							  user.getEnabled(),
							  user.getBusy(),
							  user.getType(),
							  user.getCreate_user(),
							  user.getCreate_date(),
							  user.getModify_user(),
							  user.getModify_date(),
							  user.getRoles(),
							  auths,
							  user.isAccountNonExpired(),
							  user.isAccountNonLocked(),
							  user.isCredentialsNonExpired(),
							  user.getWeichat_type()
				,user.getSex()			  
				);		
	}
	
	public SecurityDao getSecurityDao() {
		return securityDao;
	}

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}
	
}
