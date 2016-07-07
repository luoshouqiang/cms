package mcarport.business.cms.entity;

import javax.persistence.*;


/**
 * The persistent class for the base_account database table.
 * 
 */
@Entity
@Table(name="base_account")
@NamedQuery(name="BaseAccount.findAll", query="SELECT b FROM BaseAccount b")
public class BaseAccount extends GenericEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="account_id")
	private long accountId;

	@Column(name="account_type")
	private int accountType;

	

	@Column(name="login_name")
	private String loginName;

	@Column(name="login_password")
	private String loginPassword;

	@Column(name="login_type")
	private int loginType;

	private String mobile;

	
	@Column(name="user_id")
	private long userId;

	@Column(name="user_name")
	private String userName;

	@Column(name="weixin_id")
	private int weixinId;

	public BaseAccount() {
	}

	
	public int getAccountType() {
		return this.accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	
	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return this.loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public int getLoginType() {
		return this.loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getWeixinId() {
		return this.weixinId;
	}

	public void setWeixinId(int weixinId) {
		this.weixinId = weixinId;
	}


	public long getAccountId() {
		return accountId;
	}


	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}
	

}