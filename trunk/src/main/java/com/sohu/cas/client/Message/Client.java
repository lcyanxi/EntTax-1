package com.sohu.cas.client.Message;

import com.sohu.cas.client.Exception.AuthenticationException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: harry
 * Date: 2010-5-13
 * Time: 15:43:41
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    private String baseUrl;
    private HttpClient hc;
    private String accessKey = "";
    private String secCode = "";
    private String username = "";
    private String password = "";
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(String url, String username, String password, String secCode) throws AuthenticationException {
        this.baseUrl = url;
        this.secCode = secCode;
        this.username = username;
        this.password = password;

        // 多线程支持
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        hc = new HttpClient(connectionManager);

        this.login();
    }

    /**
     * 权限系统客户端登录中心权限系统服务器端，获取访问Access_Key.
     */
    private void login() throws AuthenticationException {
        Message msg = Post("/client/login/", new String[]{"username", this.username, "password", this.password});
        accessKey = msg.getContent();
        logger.info("获取Access_Key成功！ access_key:[{}]", accessKey);
    }

    public Message Get(String url) throws AuthenticationException {
        return Get(url, new String[]{});
    }

    public Message Get(String url, String[] args) throws AuthenticationException {
        Message msg = _get(url, args);
        if(msg.getType() == Message.INVALID_ACCESSKEY){
            logger.warn("Get请求Access_Key失效，尝试重获取! url:[{}], access_key:[{}]", url, this.accessKey);
            login();
            msg = _get(url, args);
            
            if(msg.getType() == Message.INVALID_ACCESSKEY){
                throw new AuthenticationException("Access_Key无效！");
            }
        }
        return msg;
    }

    /**
     * Http Get请求
     *
     * @param url
     * @param args
     * @return
     * @throws IOException
     */
    public Message _get(String url, String[] args) throws AuthenticationException {
        String request_url = baseUrl + url;

        try {
            HttpMethod method = new GetMethod(request_url);
            method.setQueryString(prepareParams(url, args));

            hc.executeMethod(method);
            String content = extractContent(method);
            method.releaseConnection();
            Message msg = Parser.parseMessage(content);

            checkMessage(msg);

            return msg;
        }
        catch (IOException e) {
            logger.warn("网络错误 url:[{}]", request_url);
            e.printStackTrace();
            throw new AuthenticationException("网络错误");
        }
    }

    private void checkMessage(Message msg) throws AuthenticationException {
        if (msg.getType() >= 100) {
            logger.error("服务器端错误:{}", msg.getContent());
            throw new AuthenticationException("服务器端错误");
        }
    }

    public Message Post(String url) throws AuthenticationException {
        return Post(url, new String[]{});
    }

    public Message Post(String url, String[] args) throws AuthenticationException {
        Message msg = _post(url, args);
        if(msg.getType() == Message.INVALID_ACCESSKEY){
            logger.warn("Post请求Access_Key失效，尝试重获取! url:[{}], access_key:[{}]", url, this.accessKey);
            login();
            msg = _post(url, args);

            if(msg.getType() == Message.INVALID_ACCESSKEY){
                throw new AuthenticationException("Access_Key无效！");
            }
        }
        return msg;
    }

    /**
     * Http Post请求。
     *
     * @param url
     * @param args
     * @return
     * @throws IOException
     */
    public Message _post(String url, String[] args) throws AuthenticationException {
        String request_url = baseUrl + url;

        try {
            logger.info(request_url);
            PostMethod method = new PostMethod(request_url);
            method.setRequestBody(prepareParams(url, args));

            hc.executeMethod(method);
            String content = extractContent(method);
            method.releaseConnection();
            Message msg = Parser.parseMessage(content);
            checkMessage(msg);
            return msg;
        }
        catch (IOException e) {
            logger.warn("网络错误 url:[{}]", request_url);
            throw new AuthenticationException("网络错误");
        }
    }

    public Message Delete(String url) throws AuthenticationException{
        Message msg = _delete(url);
        if(msg.getType() == Message.INVALID_ACCESSKEY){
            logger.warn("Delete请求Access_Key失效，尝试重获取! url:[{}], access_key:[{}]", url, this.accessKey);
            login();
            msg = _delete(url);

            if(msg.getType() == Message.INVALID_ACCESSKEY){
                throw new AuthenticationException("Access_Key无效！");
            }
        }
        return msg;
    }

    /**
     * Http Delete请求。
     *
     * @param url
     * @return
     * @throws IOException
     */
    public Message _delete(String url) throws AuthenticationException {
        String request_url = baseUrl + url;

        try {
            logger.info(request_url);
            DeleteMethod method = new DeleteMethod(request_url);
            method.setQueryString(prepareParams(url, new String[]{}));

            hc.executeMethod(method);
            String content = extractContent(method);
            method.releaseConnection();
            Message msg = Parser.parseMessage(content);
            checkMessage(msg);
            return msg;
        }
        catch (IOException e) {
            logger.warn("网络错误 url:[{}]", request_url);
            e.printStackTrace();
            throw new AuthenticationException("网络错误");
        }
    }

    /**
     * 从网络流中提取请求返回的内容。
     *
     * @param method
     * @return
     * @throws IOException
     */
    private String extractContent(HttpMethod method) throws AuthenticationException {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
        catch (IOException e) {
            logger.warn("网络错误");
            e.printStackTrace();
            throw new AuthenticationException("网络错误");
        }
    }

    /**
     * 根据调用参数，访问的相对路劲等生成seccode,并将seccode和access_key附加到请求参数中。
     *
     * @param url  访问的相对路劲，用于生成seccode。
     * @param args 原有参数。
     * @return
     */
    private NameValuePair[] prepareParams(String url, String[] args) throws AuthenticationException {

        if (args.length % 2 != 0) {
            throw new AuthenticationException("参数数量不匹配");
        }

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        int pairCount = args.length / 2;
        for (int i = 0; i < pairCount; i++) {
            NameValuePair nvp = new NameValuePair(args[i * 2], args[i * 2 + 1]);
            nvps.add(nvp);
        }
        String verificationCode = generateVerificationCode(url, args);
        nvps.add(new NameValuePair("verificationCode", verificationCode));
        nvps.add(new NameValuePair("accessKey", accessKey));
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }

    /**
     * 字节数组转换成进制字符串。
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    /**
     * 生成完整性验证码
     *
     * @param url
     * @param args
     * @return
     */
    private String generateVerificationCode(String url, String[] args) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("没有合适的摘要方法:MD5");
            throw new RuntimeException("没有合适的摘要方法:MD5");
        }

        List<String> sources = new ArrayList<String>();
        for (String arg : args) {
            sources.add(arg);
        }
        sources.add(url);
        sources.add(secCode);
        Collections.sort(sources);

        StringBuilder sb = new StringBuilder();
        for (String s : sources) {
            sb.append(s);
        }

        String m = byte2hex(md.digest(sb.toString().getBytes()));

        return m;
    }
}

