package com.sohu.cas.client.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.cas.client.AuthenticationBase;
import com.sohu.cas.client.Exception.AuthenticationException;
import com.sohu.cas.client.core.Group;
import com.sohu.cas.client.core.Node;
import com.sohu.cas.client.core.Perm;
import com.sohu.cas.client.core.User;
import com.sohu.cas.client.impl.AuthenticationBaseImpl;

/**
 * Created by IntelliJ IDEA. User: harry Date: 2010-4-7 Time: 18:03:06 To change
 * this template use File | Settings | File Templates.
 */
public class Parser {
	private static final Logger logger = LoggerFactory.getLogger(Parser.class);

	public static AuthenticationBase parseAuthentication(String content)
			throws AuthenticationException {
		try {
			JSONObject jo = new JSONObject(content);
			String user_content = jo.getString("user");
			String perms_content = jo.getString("perms");
			User user = Parser.parseUser(user_content);
			List<Perm> perms = Parser.parsePerms(perms_content);

			AuthenticationBase auth = new AuthenticationBaseImpl(user, perms);
			return auth;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析Message
	 * 
	 * @param content
	 * @return
	 */
	public static Message parseMessage(String content)
			throws AuthenticationException {
		try {
			JSONObject jo = new JSONObject(content);
			int status = jo.getInt("type");
			Date time = new Date(jo.getInt("time"));
			String msg_content = jo.getString("content");

			return new Message(status, time, msg_content);
		} catch (JSONException e) {
			logger.warn("JSON解析错误 json[{}]", content);
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析List<Perm>
	 * 
	 * @param content
	 * @return
	 */
	public static List<Perm> parsePerms(String content)
			throws AuthenticationException {
		try {
			JSONArray ja;
			List<Perm> perms = new ArrayList<Perm>();
			ja = new JSONArray(content);
			int len = ja.length();
			for (int i = 0; i < len; i++) {
				JSONArray jai = ja.getJSONArray(i);
				String nodeName = jai.getString(0);
				int code = jai.getInt(1);
				int type = jai.getInt(2);

				Perm p = new Perm(nodeName, code, type);
				perms.add(p);
			}
			return perms;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}

	}

	/**
	 * 从JSON中解析User
	 * 
	 * @param content
	 * @return
	 */
	public static User parseUser(String content) throws AuthenticationException {
		try {
			JSONObject jo;
			jo = new JSONObject(content);
			int id = jo.getInt("id");
			String username = jo.getString("username");
			String fullname = jo.getString("fullname");
			String email = jo.getString("email");
			Boolean isActive = jo.getBoolean("is_active");
			String phone = jo.getString("work_phone");
			String mobile = jo.getString("mobile_phone");
			String address = jo.getString("address");
			String desc = jo.getString("detail");
			String param = jo.getString("params");
			String gps = jo.getString("groups");

			List<Group> groups = parseGroups(gps);

			User user = new User();
			user.setGroups(groups);
			user.setName(username);
			user.setFullName(fullname);
			user.setId(id);
			user.setEmail(email);
			user.setPhone(phone);
			user.setMobile(mobile);
			user.setAddress(address);
			user.setParam(param);
			user.setIsActive(isActive);
			user.setDesc(desc);
			return user;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析List<Group>
	 * 
	 * @param content
	 * @return
	 * @throws JSONException
	 */
	private static List<Group> parseGroups(String content)
			throws AuthenticationException {
		try {
			List<Group> groups = new ArrayList<Group>();
			JSONArray jai = new JSONArray(content);
			int len = jai.length();
			for (int i = 0; i < len; i++) {
				Group g = parseGroup(jai.getString(i));
				groups.add(g);
			}
			return groups;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析Group
	 * 
	 * @param content
	 * @return
	 * @throws JSONException
	 */
	public static Group parseGroup(String content)
			throws AuthenticationException {
		try {
			JSONArray jai = new JSONArray(content);
			int gid = jai.getInt(0);
			String name = jai.getString(1);
			Group g = new Group(gid, name);
			return g;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析List<Node>
	 * 
	 * @param content
	 * @return
	 */
	public static List<Node> parseNodes(String content)
			throws AuthenticationException {
		try {
			JSONArray ja = new JSONArray(content);
			List<Node> nodes = new ArrayList<Node>();
			for (int i = 0; i < ja.length(); i++) {
				String fullName = ja.getString(i);
				Node n = new Node(fullName);
				nodes.add(n);
			}

			return nodes;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析Node
	 * 
	 * @param content
	 * @return
	 */
	public static Node pareseNode(String content)
			throws AuthenticationException {
		try {
			JSONObject jo = new JSONObject(content);
			String fullName = jo.getString("full_name");
			String detail = jo.getString("detail");
			return new Node(fullName, detail);
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析List<User>
	 * 
	 * @param content
	 * @return
	 */
	public static List<User> parseUsers(String content)
			throws AuthenticationException {
		try {
			JSONArray ja = new JSONArray(content);
			List<User> users = new ArrayList<User>();
			for (int i = 0; i < ja.length(); i++) {
				String userContent = ja.getString(i);
				User user = parseUser(userContent);
				users.add(user);
			}

			return users;
		} catch (JSONException e) {
			logger.warn("JSON解析错误");
			throw new AuthenticationException("JSON解析错误");
		}
	}

	/**
	 * 从JSON中解析Boolean
	 * 
	 * @param content
	 * @return
	 */
	public static Boolean parseBoolean(String content) {
		return content.toLowerCase().equals("true");
	}
}
