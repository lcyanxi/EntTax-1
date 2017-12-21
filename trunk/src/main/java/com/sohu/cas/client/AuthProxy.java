package com.sohu.cas.client;

import com.sohu.cas.client.Exception.AuthenticationException;
import com.sohu.cas.client.config.AuthConfig;
import com.sohu.cas.client.config.ConfigHandler;
import com.sohu.cas.client.core.Group;
import com.sohu.cas.client.core.Node;
import com.sohu.cas.client.core.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * AuthProxy 权限代理，负责与服务器端的通讯
 */
public abstract class AuthProxy {
    /**
     * AuthProxy初始化
     * @param properties 初始化参数
     */
    public abstract void init(HashMap<String, String> properties);

    /**
     * 根据用户id获取用户的Authentication对象
     * @param userId 用户id
     * @return
     * @throws AuthenticationException
     */
    public abstract AuthenticationBase authenticate(int userId) throws AuthenticationException;

    /**
     * 根据用户名获取用户的Authentication对象
     * @param username 用户名
     * @return
     * @throws AuthenticationException
     */
    public abstract AuthenticationBase authenticate(String username) throws AuthenticationException;

    /**
     * 验证用户的用户名和密码，并获取其Authentication对象
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws AuthenticationException
     */
    public abstract AuthenticationBase authenticate(String username, String password) throws AuthenticationException;

    /**
     * 根据用户的sso ticket获取用户的Authentication对象
     * @param ticket
     * @return
     * @throws AuthenticationException
     */
    public abstract AuthenticationBase authenticateByTicket(String ticket) throws AuthenticationException;

    /**
     * 获取节点的子节点
     * @param nodeFullName 节点全名
     * @param recursive 是否递归获取子节点的子节点
     * @return
     * @throws AuthenticationException
     */
    public abstract List<Node> getChildNodes(String nodeFullName, boolean recursive) throws AuthenticationException;

    /**
     * 获取节点的子节点
     * @param node 节点
     * @param recursive 是否递归获取子节点的子节点
     * @return
     * @throws AuthenticationException
     */
    public abstract List<Node> getChildNodes(Node node, boolean recursive) throws AuthenticationException;

    /**
     * 获取用户在某节点下拥有权限的子节点
     * @param nodeFullName 节点全名
     * @param user 用户
     * @param code 权限值
     * @param recursive 是否递归获取子节点的子节点
     * @return
     * @throws AuthenticationException
     */
    public abstract List<Node> getValidChildNodes(String nodeFullName, User user, int code, boolean recursive) throws AuthenticationException;

    /**
     * 获取用户在某节点下拥有权限的子节点
     * @param node 节点
     * @param user 用户
     * @param code 权限值
     * @param recursive 是否递归获取子节点的子节点
     * @return
     * @throws AuthenticationException
     */
    public abstract List<Node> getValidChildNodes(Node node, User user, int code, boolean recursive) throws AuthenticationException;

    /**
     * 获取某个组的所有用户
     * @param g 组
     * @return
     * @throws AuthenticationException
     */
    public abstract List<User> getGroupUsers(Group g) throws AuthenticationException;

    /**
     * 创建节点
     * @param nodeFullName 节点全名
     * @param stageName
     *@param detail  节点详情  @return
     * @throws AuthenticationException
     */
    public abstract Node createNode(String nodeFullName, String stageName, String detail) throws AuthenticationException;

    /**
     * 删除节点
     * @param nodeFullName 节点全名
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean removeNode(String nodeFullName) throws AuthenticationException;

    /**
     *  获取节点
     * @param nodeFullName 节点全名
     * @return
     * @throws AuthenticationException
     */
    public abstract Node getNode(String nodeFullName) throws AuthenticationException;

    /**
     * 判断节点是否存在
     * @param nodeFullName 节点全名
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean isNodeExist(String nodeFullName) throws AuthenticationException;

    /**
     * 增加用户ACL
     * @param userId 用户id
     * @param nodeFullName 节点全名
     * @param type 权限类型: 1--自己 2--字节点 3--自己以及字节点
     * @param code 权限值
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean addUserEntry(int userId, String nodeFullName, int type, int code) throws AuthenticationException;

    /**
     * 删除用户ACL
     * @param userId 用户id
     * @param nodeFullName 节点全名
     * @param type 权限类型: 1--自己 2--字节点 3--自己以及字节点
     * @param code 权限值
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean removeUserEntry(int userId, String nodeFullName, int type, int code) throws AuthenticationException;

    /**
     * 增加权限组ACL
     * @param groupId 组id
     * @param nodeFullName 节点全名
     * @param type 权限类型: 1--自己 2--字节点 3--自己以及字节点
     * @param code 权限值
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean addGroupEntry(int groupId, String nodeFullName, int type, int code) throws AuthenticationException;

    /**
     * 删除权限组ACL
     * @param groupId 组id
     * @param nodeFullName 节点全名
     * @param type 权限类型: 1--自己 2--字节点 3--自己以及字节点
     * @param code 权限值
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean removeGroupEntry(int groupId, String nodeFullName, int type, int code) throws AuthenticationException;

    /**
     * 获取对节点有权限的用户
     * @param nodeFullName 节点名
     * @param code 权限值
     * @return
     * @throws AuthenticationException
     */
    public abstract List<User> getNodeValidUsers(String nodeFullName, int code) throws AuthenticationException;

    /**
     * 获取所有用户
     * @return
     * @throws AuthenticationException
     */
    public abstract List<User> getUsers() throws AuthenticationException;

    /**
     * 登出指定用户
     * @param user 要登出的用户
     * @return
     * @throws AuthenticationException
     */
    public abstract Boolean logout(User user) throws AuthenticationException;

    /**
     * 获取单点登录
     * @return
     */
    public abstract String getSsoUrl();

    public static AuthProxy getInstance(){
        if(authProxy == null){
            authProxy = buildProxy();
        }
        return authProxy;
    }

    private static AuthProxy buildProxy(){
        logger.info("初始化AuthProxy");
        try{
            InputStream is = AuthProxy.class.getClassLoader().getResourceAsStream(configFile);
            AuthConfig authConfig = ConfigHandler.fromXML(AuthConfig.class, is);
            logger.info("AuthProxy实例: {}", authConfig.getProxy());
            authProxy = (AuthProxy)Class.forName(authConfig.getProxy()).newInstance();
            authProxy.init(authConfig.getProperties());
            return authProxy;
        }
        catch(Exception e){
            logger.error("初始化AuthProxy失败, Exception:[{}]", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("初始化AuthProxy失败!", e);
        }
    }
    
    private static Logger logger = LoggerFactory.getLogger(AuthProxy.class);
    private static String configFile = "auth.xml";
    private static AuthProxy authProxy = null;
}
