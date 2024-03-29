package com.minzheng.blog.util;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
 
public class TaobaoApi {
 
    private static final String SIGN_METHOD_MD5 = "md5";
    private static final String SIGN_METHOD_HMAC = "hmac";
    private static final String CHARSET_UTF8 = "utf-8";
    private static final String CONTENT_ENCODING_GZIP = "gzip";
 
    // TOP服务地址，正式环境需要设置为http://gw.api.taobao.com/router/rest
    private static final String serverUrl = "http://gw.api.taobao.com/router/rest";
    private static final String appKey = "33866842"; // 可替换为您的应用的appKey
    private static final String appSecret = "2ad5ad6226ef737eb425fd3912c57eb3"; // 可替换为您的应用的appSecret
    private static final String sessionKey = "test"; // 必须替换为授权得到的真实有效sessionKey
 
    public static void main1(String[] args) throws Exception {
        System.out.println(getItemCoupon());
    }
 
 
 
    private static String getItemCoupon() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        // 公共参数
        params.put("method", "taobao.tbk.itemid.coupon.get");
        params.put("app_key", appKey);
        params.put("session", sessionKey);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("timestamp", df.format(new Date()));
        params.put("format", "json");
        params.put("v", "2.0");
        params.put("sign_method", "hmac");
        // 业务参数
        params.put("platform", "2");
        params.put("pid", "mm_40663076_2226750240_111160300410");
        params.put("num_iid", "549944548546");
        // 签名参数
        params.put("sign", signTopRequest(params, appSecret, SIGN_METHOD_HMAC));
        // 请用API
        return callApi(new URL(serverUrl), params);
    }
 
    private static String getSellerItem() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        // 公共参数
        params.put("method", "taobao.time.get");
        params.put("app_key", appKey);
        params.put("session", sessionKey);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("timestamp", df.format(new Date()));
        params.put("format", "json");
        params.put("v", "2.0");
        params.put("sign_method", "hmac");
        // 业务参数
        params.put("fields", "num_iid,title,nick,price,num");
        params.put("num_iid", "123456789");
        // 签名参数
        params.put("sign", signTopRequest(params, appSecret, SIGN_METHOD_HMAC));
        // 请用API
        return callApi(new URL(serverUrl), params);
    }
 
    /**
     * 对TOP请求进行签名。
     */
    private static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
 
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (isNotEmpty(key) && isNotEmpty(value)) {
                query.append(key).append(value);
            }
        }
 
        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }
 
        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
 
    /**
     * 对字节流进行HMAC_MD5摘要。
     */
    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
 
    /**
     * 对字符串采用UTF-8编码后，用MD5进行摘要。
     */
    private static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(CHARSET_UTF8));
    }
 
    /**
     * 对字节流进行MD5摘要。
     */
    private static byte[] encryptMD5(byte[] data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data);
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
 
    /**
     * 把字节流转换为十六进制表示方式。
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
 
    private static String callApi(URL url, Map<String, String> params) throws IOException {
        String query = buildQuery(params, CHARSET_UTF8);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(CHARSET_UTF8);
        }
 
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Host", url.getHost());
            conn.setRequestProperty("Accept", "text/xml,text/javascript");
            conn.setRequestProperty("User-Agent", "top-sdk-java");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET_UTF8);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
 
        return rsp;
    }
 
    private static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }
 
        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;
 
        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (isNotEmpty(name) && isNotEmpty(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
 
                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
 
        return query.toString();
    }
 
    private static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        if (conn.getResponseCode() < 400) {
            String contentEncoding = conn.getContentEncoding();
            if (CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
                return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
            } else {
                return getStreamAsString(conn.getInputStream(), charset);
            }
        } else {// Client Error 4xx and Server Error 5xx
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
    }
 
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();
 
            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }
 
            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
 
    private static String getResponseCharset(String ctype) {
        String charset = CHARSET_UTF8;
 
        if (isNotEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (isNotEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
 
        return charset;
    }
 
    private static boolean isNotEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return true;
            }
        }
        return false;
    }
}