package com.sohu.cas.client.impl;

/**
 * Created by IntelliJ IDEA.
 * User: zichengxiong
 * Date: 2010-3-29
 * Time: 11:26:08
 */

import java.util.List;

import com.sohu.cas.client.AuthenticationBase;
import com.sohu.cas.client.core.Perm;
import com.sohu.cas.client.core.User;

public class AuthenticationBaseImpl implements AuthenticationBase {

	private static final long serialVersionUID = 1L;
	private User user;
	private List<Perm> perms;

	/**
	 * 用户
	 * 
	 * @return
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * 权限列表
	 * 
	 * @return
	 */
	public List<Perm> getPermission() {
		return this.perms;
	}

	public AuthenticationBaseImpl(User user, List<Perm> perms) {
		this.user = user;
		this.perms = perms;
	}

	/**
	 * 对节点是否拥有指定权限
	 * 
	 * @param nodFullName
	 *            节点全名
	 * @param code
	 *            权限值
	 * @return
	 */
	public boolean isValid(String nodFullName, int code) {
		return check_plus(nodFullName, code);
	}

	/**
	 * 对指定节点及其子节点是否拥有权限
	 * 
	 * @param nodFullName
	 *            节点全名
	 * @return
	 */
	@Override
	public boolean isShow(String nodFullName) {
		for (Perm p : perms) {
			if (p.getName().startsWith(nodFullName))
				return true;
			if ((nodFullName.equals(p.getName()) && ((p.getType() & 1) > 0))
					|| (nodFullName.startsWith(p.getName() + ".") && ((p
							.getType() & 2) > 0))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断赋权权限 待查code匹配了全部位则检查成功
	 * 
	 * @param node
	 * @param code
	 * @return
	 */
	public boolean check_plus(String node, int code) {
		for (Perm p : perms) {
			if ((node.equals(p.getName()) && ((p.getType() & 1) > 0))
					|| (node.startsWith(p.getName() + ".") && ((p.getType() & 2) > 0))) {
				code = code - (code & p.getCode());
				if (code == 0) {
					return true;
				}
			}
		}
		return false;
	}

}
