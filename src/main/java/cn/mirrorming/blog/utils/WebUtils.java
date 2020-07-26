package cn.mirrorming.blog.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@UtilityClass
public class WebUtils {


    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (reqAttrs == null) {
            return null;
        }
        return reqAttrs.getRequest();
    }

    public static HttpServletResponse getCurrentRespost() {
        ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return reqAttrs.getResponse();
    }

    /**
     * 在很多应用下都可能有需要将用户的真实IP记录下来，这时就要获得用户的真实IP地址
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。
     * 但是在转发请求的HTTP头信息中，增加了X－FORWARDED－FOR信息。用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址。
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Client-IP");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!StringUtils.isEmpty(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static Long ipToLong(String ipString) {
        Long[] ip = new Long[4];
        int pos1 = ipString.indexOf(".");
        int pos2 = ipString.indexOf(".", pos1 + 1);
        int pos3 = ipString.indexOf(".", pos2 + 1);
        ip[0] = Long.parseLong(ipString.substring(0, pos1));
        ip[1] = Long.parseLong(ipString.substring(pos1 + 1, pos2));
        ip[2] = Long.parseLong(ipString.substring(pos2 + 1, pos3));
        ip[3] = Long.parseLong(ipString.substring(pos3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String getRemoteIp() {
        HttpServletRequest request = getCurrentRequest();
        return getRemoteIp(request);
    }

    public static boolean isMulitpart(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType != null && contentType.contains("multipart");
    }

    public static String getRequestParametersString(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            sb.append("&").append(entry.getKey()).append("=");
            for (String value : entry.getValue()) {
                sb.append(value).append(",");
            }
            if (!StringUtils.isEmpty(entry.getValue())) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        if (sb.length() > 0) {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

    public static Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String key = entry.getKey();
            String value;
            if (entry.getValue().length > 0) {
                StringBuffer sb = new StringBuffer();
                for (String item : entry.getValue()) {
                    sb.append(item).append(",");
                }
                if (!StringUtils.isEmpty(entry.getValue())) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                value = sb.toString();
            } else {
                value = entry.getValue().toString();
            }
            params.put(key, value);
        }
        return params;
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getX_SessionID() {
        return getCurrentRequest().getHeader("X-SessionID");
    }

    public static String getX_ClientID() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_ClientID(request);
    }

    public static String getX_ClientID(HttpServletRequest request) {
        return request.getHeader("X-ClientID");
    }

    public static String getX_Channel() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_Channel(request);
    }

    public static String getX_Channel(HttpServletRequest request) {
        return request.getHeader("X-Channel");
    }


    public static String getX_RequestID() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_RequestID(request);
    }

    public static String getX_RequestID(HttpServletRequest request) {
        return request.getHeader("X-RequestID");
    }


    public static String getX_Token() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_Token(request);
    }

    public static String getX_Token(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        if (token == null) {
            token = request.getParameter("x-token");
        }
        if (token == null) {
            Cookie cookie = getCookie(request, "X-Token");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        return token;
    }

    public static String getX_ClientVer() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_ClientVer(request);
    }

    public static String getX_ClientVer(HttpServletRequest request) {
        return request.getHeader("X-ClientVer");
    }


    public static String getX_DeviceID() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getX_DeviceID(request);
    }

    public static String getX_DeviceID(HttpServletRequest request) {
        return request.getHeader("X-DeviceID");
    }

    public static String getUsetAgent() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getUsetAgent(request);
    }

    public static String getUsetAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    public static String getReferer() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return getReferer(request);
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    public static String buildServerUrl(HttpServletRequest request, String path) {
        String host = request.getHeader("x-forwarded-host");
        if (StringUtils.isEmpty(host)) {
            host = request.getHeader("host");
        }
        String proto = request.getHeader("x-forwarded-proto");
        if (StringUtils.isEmpty(proto)) {
            proto = request.getScheme();
        }
        return proto + "://" + host + path;
    }
}
