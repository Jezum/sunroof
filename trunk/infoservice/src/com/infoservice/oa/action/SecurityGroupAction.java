package com.infoservice.oa.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.infoservice.domain.SecurityGroup;
import com.infoservice.global.action.BaseAction;
import com.infoservice.global.service.ICommonService;
import com.infoservice.util.Contants;

@Controller
@Scope("prototype")
@ParentPackage(value = "struts-default")
@Namespace(value = "/ugroup")
@Results( { @Result(name = "jsonStringPage", location = "/jsonString.jsp") })
public class SecurityGroupAction extends BaseAction {

	private static final long serialVersionUID = -7202440540672603833L;

	// Field
	private SecurityGroup secGp;
	private List<SecurityGroup> secGpList;
	private String jsonString;

	// service
	@Autowired
	@Qualifier("commonService")
	private ICommonService<SecurityGroup, Long> commonSev;

	@Action(value = "ugroup")
	public String bulidJsonList() {
		StringBuffer _JSONStr = null;
		try {
			secGpList = commonSev.getAll();
			JSONArray jsonObject = JSONArray.fromObject(secGpList);
			_JSONStr = new StringBuffer();
			String callback = this.callback;
			_JSONStr.append(callback).append("({\"total\":").append(
					secGpList.size()).append(",\"results\":");
			_JSONStr.append(jsonObject.toString());
			_JSONStr.append("})");
			this.jsonString = _JSONStr.toString();
		} catch (Exception e) {
			log.error("获取用户组失败", e);
			this.jsonString = "[]";
		}
		return Contants.JSONSTRING_RESULT_NAME;
	}

	@Action(value = "saveGroup")
	public String saveGroup() throws IOException {
		this.response.reset();
		this.response.setCharacterEncoding("utf-8");
		String outText = "{success:false,msg:'保存失败'}";
		try {
			if (null == secGp.getIsLocked())
				secGp.setIsLocked(new Byte("1"));
			commonSev.save(secGp);
			outText = "{success:true,msg:'保存成功'}";
			response.getWriter().print(outText);
		} catch (Exception e) {
			log.error("添加用户组失败", e);
			response.getWriter().print(outText);
		}
		return null;
	}

	public SecurityGroup getSecGp() {
		return secGp;
	}

	public void setSecGp(SecurityGroup secGp) {
		this.secGp = secGp;
	}

	public List<SecurityGroup> getSecGpList() {
		return secGpList;
	}

	public void setSecGpList(List<SecurityGroup> secGpList) {
		this.secGpList = secGpList;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

}
