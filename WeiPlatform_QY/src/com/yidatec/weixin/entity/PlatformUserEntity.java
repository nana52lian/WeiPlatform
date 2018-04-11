package com.yidatec.weixin.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;

import com.yidatec.weixin.security.CustomUserDetails;

/**
 * 用户实体
 * @author 
 *
 */
public class PlatformUserEntity extends BaseEntity implements CustomUserDetails, java.io.Serializable {

	// 废弃
	private String user_id = null;

	// 登录账号
	private String account = null;
	
	private String password = null;
	
	// 确认密码
	private String pass_confirm = null;

	// 角色ID集合
	private String role_ids = null;
	
	private String name = null;
	
	private String mail = null;
	
	private String mobile_phone = null;

	private int issys = 0;

	private int enabled = 1;
	
	private int busy = 0;
	
	private int sex = 2;
	
	// 账户类型
	private int type = 0;

	private List<RoleEntity> roles = null;

	//实现了UserDetails之后的相关变釄1�7    
    private  Set<GrantedAuthority> authorities;
    private  boolean accountNonExpired = true;
	private  boolean accountNonLocked = true;
    private  boolean credentialsNonExpired = true;
    
    //平台账户关联营业部id集合
	private String sales_department_ids = null;
	
	//区分不同微信号
	private String weichat_type;

	public String getSales_department_ids() {
		return sales_department_ids;
	}

	public void setSales_department_ids(String sales_department_ids) {
		this.sales_department_ids = sales_department_ids;
	}

	public PlatformUserEntity() {
    	
    }
    
    public PlatformUserEntity(String id, 
    				  String user_id,
    				  String account,
    				  String password,
    				  String name,
    				  String mail,
    				  String phone,
    				  int issys,
    				  int enabled,
    				  int busy,
    				  int type,
    				  String create_user,
    				  String create_date,
    				  String modify_user,
    				  String modify_date,
    				  List<RoleEntity> roles,
    				  Collection<GrantedAuthority> authorities,
    				  boolean accountNonExpired,
    				  boolean accountNonLocked,
    				  boolean credentialsNonExpired,
    				  String weichat_type,
    				  int sex
    				  ) {
    	this.setId(id);
    	this.user_id = user_id;
    	this.account = account;
    	this.password = password;
    	this.name = name;
    	this.mail = mail;
    	this.mobile_phone = phone;
    	this.issys = issys;
    	this.enabled = enabled;
    	this.busy = busy; 
    	this.type = type;
    	this.setCreate_user(create_user);
    	this.setCreate_date(create_date);
    	this.setModify_user(modify_user);
    	this.setModify_date(modify_date);
    	this.roles = roles;
    	this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));    	
    	this.accountNonExpired = accountNonExpired;
    	this.accountNonLocked = accountNonLocked;
    	this.credentialsNonExpired = credentialsNonExpired;    	
    	this.weichat_type = weichat_type;
    	this.sex = sex;
    }
    
    public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}	

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public int getIssys() {
		return issys;
	}

	public void setIssys(int issys) {
		this.issys = issys;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return authorities;
	}

	@Override
	public String getUsername() {		
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {		
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {		
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {		
		return enabled == 1;
	}

	@Override
	public String getUserId() {		
		return getUser_id();
	}

	@Override
	public String getUserAccount() {		
		return account;
	}

	@Override
	public String getUserPassword() {		
		return password;
	}

	@Override
	public String getUserName() {		
		return name;
	}

	@Override
	public String getUserMail() {		
		return mail;
	}
	
	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<GrantedAuthority> authorities) {        
        // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {            
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }
	
	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to the set.
            // If the authority is null, it is a custom authority and should precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
	
	public String getPass_confirm() {
		return pass_confirm;
	}

	public void setPass_confirm(String pass_confirm) {
		this.pass_confirm = pass_confirm;
	}

	public String getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(String role_ids) {
		this.role_ids = role_ids;
	}

	public String getStatus() {
		return enabled == 1 ? "正常" : "已停用";
	}

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	
	public int getBusy() {
		return busy;
	}

	public void setBusy(int busy) {
		this.busy = busy;
	}

	@Override
    public boolean equals(Object rhs) {
        if (rhs instanceof PlatformUserEntity) {
            return this.getAccount().equals(((PlatformUserEntity) rhs).getAccount());
        }
        return false;        
    }
	
	@Override
    public int hashCode() {
        return this.getAccount().hashCode();
    }

	public String getWeichat_type() {
		return weichat_type;
	}

	public void setWeichat_type(String weichat_type) {
		this.weichat_type = weichat_type;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
	
	
}
