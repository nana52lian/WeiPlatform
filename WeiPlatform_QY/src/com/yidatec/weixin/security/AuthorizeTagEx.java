package com.yidatec.weixin.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthorizeTagEx extends TagSupport {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(AuthorizeTagEx.class);
	
	private String resKey = null;

	@Override
    public int doStartTag() throws JspException {
        try {    		
            if (authorize()) {
            	return EVAL_BODY_INCLUDE;
            }            
        } catch(Exception e) {
        	log.error(e.getMessage());
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
	
	/**
	 * 验证当前登录用户是否有权附1�7
	 * @return
	 */
	public boolean authorize() throws IOException {		
		// 获取资源MAP
		final Collection<? extends GrantedAuthority> granted = getPrincipalAuthorities();
		if (granted.size() == 0) {
			return false;
		}
		Collection<ConfigAttribute> required = CustomInvocationSecurityMetadataSourceService.getResourceMap().get(resKey);
		if (required == null) {
			return false;
		}
		Set<GrantedAuthority> grantedCopy = retainAll(granted, toAuthorities(required.toString().replace("[", "").replace("]", "")));
        if (grantedCopy.isEmpty()) {
            return false;
        }		
		return true;
	}
	   
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
    
    @Override
    public void release() {
        super.release();
        resKey = null;
    }
    
    public String getResKey() {
		return resKey;
	}

	public void setResKey(String resKey) {
		this.resKey = resKey;
	}
	
	/**
	 * 获取用户权限
	 * @return
	 */
	private Collection<? extends GrantedAuthority> getPrincipalAuthorities() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (null == currentUser) {
            return Collections.emptyList();
        }
        return currentUser.getAuthorities();
    }
	
	private Set<GrantedAuthority> toAuthorities(String authorizations) {
        final Set<GrantedAuthority> requiredAuthorities = new HashSet<GrantedAuthority>();
        requiredAuthorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(authorizations));
        return requiredAuthorities;
    }
	
	private Set<GrantedAuthority> retainAll(final Collection<? extends GrantedAuthority> granted,
            final Collection<GrantedAuthority> required) {
		Set<String> grantedRoles = authoritiesToRoles(granted);
		Set<String> requiredRoles = authoritiesToRoles(required);
		grantedRoles.retainAll(requiredRoles);
		
		return rolesToAuthorities(grantedRoles, granted);
	}
	
	private Set<String> authoritiesToRoles(Collection<? extends GrantedAuthority> c) {
        Set<String> target = new HashSet<String>();
        for (GrantedAuthority authority : c) {
            if (null == authority.getAuthority()) {
                throw new IllegalArgumentException(
                        "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
                                + authority.toString());
            }
            target.add(authority.getAuthority());
        }
        return target;
    }

    private Set<GrantedAuthority> rolesToAuthorities(Set<String> grantedRoles, Collection<? extends GrantedAuthority> granted) {
        Set<GrantedAuthority> target = new HashSet<GrantedAuthority>();
        for (String role : grantedRoles) {
            for (GrantedAuthority authority : granted) {
                if (authority.getAuthority().equals(role)) {
                    target.add(authority);
                    break;
                }
            }
        }
        return target;
    }
	
}
