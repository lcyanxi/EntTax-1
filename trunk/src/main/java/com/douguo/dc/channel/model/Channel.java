package com.douguo.dc.channel.model;

import java.io.Serializable;

public class Channel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2363636380050424212L;
	private int id;
    private String channelCode;
    private String channelName;
    private int status;
    private String userName;
    private String principal;
    private String principalDep;
    private String principalContact;
    private String planDesc;
    private String channelCooperation;
    private String channelPlat;
    private Long channelType1;
    private Long channelType2;
    private Long channelType3;

    private String channelTypeName1;
    private String channelTypeName2;
    private String channelTypeName3;

	private String channelTag;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipalDep() {
		return principalDep;
	}

	public void setPrincipalDep(String principalDep) {
		this.principalDep = principalDep;
	}

	public String getPrincipalContact() {
		return principalContact;
	}

	public void setPrincipalContact(String principalContact) {
		this.principalContact = principalContact;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public String getChannelCooperation() {
		return channelCooperation;
	}

	public void setChannelCooperation(String channelCooperation) {
		this.channelCooperation = channelCooperation;
	}

	public String getChannelPlat() {
		return channelPlat;
	}

	public void setChannelPlat(String channelPlat) {
		this.channelPlat = channelPlat;
	}

	public Long getChannelType1() {
		return channelType1;
	}

	public void setChannelType1(Long channelType1) {
		this.channelType1 = channelType1;
	}

	public Long getChannelType2() {
		return channelType2;
	}

	public void setChannelType2(Long channelType2) {
		this.channelType2 = channelType2;
	}

	public Long getChannelType3() {
		return channelType3;
	}

	public void setChannelType3(Long channelType3) {
		this.channelType3 = channelType3;
	}

	public String getChannelTypeName1() {
		return channelTypeName1;
	}

	public void setChannelTypeName1(String channelTypeName1) {
		this.channelTypeName1 = channelTypeName1;
	}

	public String getChannelTypeName2() {
		return channelTypeName2;
	}

	public void setChannelTypeName2(String channelTypeName2) {
		this.channelTypeName2 = channelTypeName2;
	}

	public String getChannelTypeName3() {
		return channelTypeName3;
	}

	public void setChannelTypeName3(String channelTypeName3) {
		this.channelTypeName3 = channelTypeName3;
	}

	public String getChannelTag() {
		return channelTag;
	}

	public void setChannelTag(String channelTag) {
		this.channelTag = channelTag;
	}
}