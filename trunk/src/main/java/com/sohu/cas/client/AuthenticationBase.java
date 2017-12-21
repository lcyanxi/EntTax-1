package com.sohu.cas.client;

import com.sohu.cas.client.core.Perm;
import com.sohu.cas.client.core.User;

import java.io.Serializable;
import java.util.List;

/**
 * Authentication基础接口
 */
public interface AuthenticationBase extends Serializable
{
    /**
     * 用户
     * @return
     */
    public User getUser();

    /**
     * 权限列表
     * @return
     */
    public List<Perm> getPermission();

    /**
     * 对节点是否拥有指定权限
     * @param nodFullName 节点全名
     * @param code 权限值
     * @return
     */
    public boolean isValid(String nodFullName, int code);

    /**
     * 对指定节点及其子节点是否拥有权限
     * @param nodFullName 节点全名
     * @return
     */
    public boolean isShow(String nodFullName);
}
