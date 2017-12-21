package com.sohu.cas.client.impl;

import java.util.HashMap;
import java.util.List;

import com.sohu.cas.client.AuthProxy;
import com.sohu.cas.client.AuthenticationBase;
import com.sohu.cas.client.Exception.AuthenticationException;
import com.sohu.cas.client.Exception.NotExistsException;
import com.sohu.cas.client.Message.Client;
import com.sohu.cas.client.Message.Message;
import com.sohu.cas.client.Message.Parser;
import com.sohu.cas.client.core.Group;
import com.sohu.cas.client.core.Node;
import com.sohu.cas.client.core.User;

/**
 * Created by IntelliJ IDEA. User: zichengxiong Date: 2010-3-29 Time: 11:23:31
 */
public class AuthProxyImpl extends AuthProxy {
	protected static String PROP_URL = "url";
	protected static String PROP_USERNAME = "username";
	protected static String PROP_PASSWORD = "password";
	protected static String PROP_SECCODE = "sec_code";
	protected static String url;
	protected static String username;
	protected static String password;
	protected static Client client;

	/**
	 * 获取单点登录
	 * 
	 * @return
	 */
	@Override
	public String getSsoUrl() {
		return url + "/sso/";
	}

	/**
	 * AuthProxy初始化
	 * 
	 * @param properties
	 *            初始化参数
	 */
	@Override
	public void init(HashMap<String, String> properties) {
		url = properties.get(PROP_URL);
		username = properties.get(PROP_USERNAME);
		password = properties.get(PROP_PASSWORD);
		String secCode = properties.get(PROP_SECCODE);
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		try {
			client = new Client(url, username, password, secCode);
		} catch (AuthenticationException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 根据用户id获取用户的Authentication对象
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public AuthenticationBase authenticate(int userId)
			throws AuthenticationException {
		String url = "/api/authentications/" + userId + "/";
		Message msg = client.Get(url);
		if (msg.getType() == Message.NOMATCH) {
			throw new NotExistsException();
		}
		AuthenticationBase auth = Parser.parseAuthentication(msg.getContent());
		return auth;
	}

	/**
	 * 根据用户名获取用户的Authentication对象
	 * 
	 * @param username
	 *            用户名
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public AuthenticationBase authenticate(String username)
			throws AuthenticationException {
		String url = "/api/authentications/";
		String[] args = new String[] { "q", "auth_by_username", "username",
				username };
		Message msg = client.Post(url, args);
		if (msg.getType() == Message.NOMATCH) {
			throw new NotExistsException();
		}
		AuthenticationBase auth = Parser.parseAuthentication(msg.getContent());
		return auth;
	}

	/**
	 * 验证用户的用户名和密码，并获取其Authentication对象
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public AuthenticationBase authenticate(String username, String password)
			throws AuthenticationException {
		String url = "/api/authentications/";
		String[] args = new String[] { "q", "auth_by_up", "username", username,
				"password", password };
		Message msg = client.Post(url, args);
		AuthenticationBase auth = Parser.parseAuthentication(msg.getContent());
		return auth;
	}

	/**
	 * 根据用户的sso ticket获取用户的Authentication对象
	 * 
	 * @param ticket
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public AuthenticationBase authenticateByTicket(String ticket)
			throws AuthenticationException {
		String url = "/api/authentications/";
		String[] args = new String[] { "q", "auth_by_ticket", "ticket", ticket };
		Message msg = client.Post(url, args);
		AuthenticationBase auth = Parser.parseAuthentication(msg.getContent());
		return auth;
	}

	/**
	 * 获取节点的子节点
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @param recursive
	 *            是否递归获取子节点的子节点
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<Node> getChildNodes(String nodeFullName, boolean recursive)
			throws AuthenticationException {
		Node n = new Node(nodeFullName);
		return getChildNodes(n, recursive);
	}

	/**
	 * 获取节点的子节点
	 * 
	 * @param node
	 *            节点
	 * @param recursive
	 *            是否递归获取子节点的子节点
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<Node> getChildNodes(Node node, boolean recursive)
			throws AuthenticationException {
		String url = "/api/nodes/";
		String[] args = new String[] { "full_name", node.getFullName(), "q",
				"children" };
		Message msg = client.Get(url, args);
		List<Node> nodes = Parser.parseNodes(msg.getContent());
		return nodes;
	}

	/**
	 * 获取用户在某节点下拥有权限的子节点
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @param user
	 *            用户
	 * @param code
	 *            权限值
	 * @param recursive
	 *            是否递归获取子节点的子节点
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<Node> getValidChildNodes(String nodeFullName, User user,
			int code, boolean recursive) throws AuthenticationException {
		Node n = new Node(nodeFullName);
		return getValidChildNodes(n, user, code, recursive);
	}

	/**
	 * 获取用户在某节点下拥有权限的子节点
	 * 
	 * @param node
	 *            节点
	 * @param user
	 *            用户
	 * @param code
	 *            权限值
	 * @param recursive
	 *            是否递归获取子节点的子节点
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<Node> getValidChildNodes(Node node, User user, int code,
			boolean recursive) throws AuthenticationException {
		String url = "/api/nodes/";
		String[] args = new String[] { "full_name", node.getFullName(), "q",
				"valid_children", "user_id", String.valueOf(user.getId()),
				"code", String.valueOf(code) };
		Message msg = client.Get(url, args);
		List<Node> nodes = Parser.parseNodes(msg.getContent());

		return nodes;
	}

	/**
	 * 创建节点
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @param stageName
	 * @param detail
	 *            节点详情 @return
	 * @throws AuthenticationException
	 */
	@Override
	public Node createNode(String nodeFullName, String stageName, String detail)
			throws AuthenticationException {
		String url = "/api/nodes/";
		Message msg = client.Post(url, new String[] { "full_name",
				nodeFullName, "stage_name", stageName, "detail", stageName });
		Node n = Parser.pareseNode(msg.getContent());
		return n;
	}

	/**
	 * 删除节点
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean removeNode(String nodeFullName)
			throws AuthenticationException {
		String url = "/api/nodes/" + nodeFullName + "/";
		Message msg = client.Delete(url);
		Boolean isSuccessed = Parser.parseBoolean(msg.getContent());
		return isSuccessed;
	}

	/**
	 * 获取节点
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Node getNode(String nodeFullName) throws AuthenticationException {
		String url = "/api/nodes/" + nodeFullName + "/";
		Message msg = client.Get(url);
		Node n = Parser.pareseNode(msg.getContent());
		return n;
	}

	/**
	 * 判断节点是否存在
	 * 
	 * @param nodeFullName
	 *            节点全名
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean isNodeExist(String nodeFullName)
			throws AuthenticationException {
		String url = "/api/nodes/" + nodeFullName + "/";
		String[] args = new String[] { "q", "exists" };
		Message msg = client.Get(url, args);
		Boolean isSuccessed = Parser.parseBoolean(msg.getContent());
		return isSuccessed;
	}

	/**
	 * 操作用户和组的ACL
	 * 
	 * @param id
	 *            用户或权限组的id
	 * @param nodeFullName
	 *            节点全名
	 * @param method
	 *            方法
	 * @param type
	 *            权限类型: 1--自己 2--子节点 3--自己以及子节点
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	private Boolean modEntry(int id, int principalType, String method,
			String nodeFullName, int type, int code)
			throws AuthenticationException {
		String url = "/api/entries/";
		String[] args = new String[] { "id", String.valueOf(id),
				"principal_type", String.valueOf(principalType), "q", method,
				"node", nodeFullName, "type", String.valueOf(type), "code",
				String.valueOf(code) };
		Message msg = client.Post(url, args);
		Boolean isSuccessed = Parser.parseBoolean(msg.getContent());
		return isSuccessed;
	}

	/**
	 * 增加用户ACL
	 * 
	 * @param userId
	 *            用户id
	 * @param nodeFullName
	 *            节点全名
	 * @param type
	 *            权限类型: 1--自己 2--子节点 3--自己以及子节点
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean addUserEntry(int userId, String nodeFullName, int type,
			int code) throws AuthenticationException {
		return modEntry(userId, 0, "add", nodeFullName, type, code);
	}

	/**
	 * 删除用户ACL
	 * 
	 * @param userId
	 *            用户id
	 * @param nodeFullName
	 *            节点全名
	 * @param type
	 *            权限类型: 1--自己 2--子节点 3--自己以及子节点
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean removeUserEntry(int userId, String nodeFullName, int type,
			int code) throws AuthenticationException {
		return modEntry(userId, 0, "remove", nodeFullName, type, code);
	}

	/**
	 * 增加权限组ACL
	 * 
	 * @param groupId
	 *            组id
	 * @param nodeFullName
	 *            节点全名
	 * @param type
	 *            权限类型: 1--自己 2--子节点 3--自己以及子节点
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean addGroupEntry(int groupId, String nodeFullName, int type,
			int code) throws AuthenticationException {
		return modEntry(groupId, 1, "add", nodeFullName, type, code);
	}

	/**
	 * 删除权限组ACL
	 * 
	 * @param groupId
	 *            组id
	 * @param nodeFullName
	 *            节点全名
	 * @param type
	 *            权限类型: 1--自己 2--子节点 3--自己以及子节点
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean removeGroupEntry(int groupId, String nodeFullName, int type,
			int code) throws AuthenticationException {
		return modEntry(groupId, 1, "remove", nodeFullName, type, code);
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<User> getUsers() throws AuthenticationException {
		String url = "/api/users/";
		Message msg = client.Get(url);
		List<User> users = Parser.parseUsers(msg.getContent());
		return users;
	}

	/**
	 * 获取对节点有权限的用户
	 * 
	 * @param nodeFullName
	 *            节点名
	 * @param code
	 *            权限值
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<User> getNodeValidUsers(String nodeFullName, int code)
			throws AuthenticationException {
		String url = "/api/users/";
		String[] args = new String[] { "q", "by_node", "node", nodeFullName,
				"code", String.valueOf(code) };
		Message msg = client.Get(url, args);
		List<User> users = Parser.parseUsers(msg.getContent());
		return users;
	}

	/**
	 * 获取某个组的所有用户
	 * 
	 * @param g
	 *            组
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public List<User> getGroupUsers(Group g) throws AuthenticationException {
		String url = "/api/users/";
		String[] args = new String[] { "q", "by_group", "group_id",
				String.valueOf(g.getId()) };
		Message msg = client.Get(url, args);
		List<User> users = Parser.parseUsers(msg.getContent());
		return users;
	}

	/**
	 * 登出指定用户
	 * 
	 * @param user
	 *            要登出的用户
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Boolean logout(User user) throws AuthenticationException {
		String url = "/api/users/" + user.getId() + "/";
		String[] args = new String[] { "q", "logout" };
		Message msg = client.Get(url, args);
		Boolean isSuccessed = Parser.parseBoolean(msg.getContent());
		return isSuccessed;
	}
}
