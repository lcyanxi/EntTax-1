package com.sohu.cas.client.core;

/**
 * Created by IntelliJ IDEA.
 * User: harry
 * Date: 2010-8-6
 * Time: 15:12:54
 * To change this template use File | Settings | File Templates.
 */
public class PermType {
    public static final int SELF                = 1;
    public static final int CHILDREN            = 2;
    public static final int SELF_AND_CHILDREN    = SELF | CHILDREN;
    public static final int NONE                = 0;
}
