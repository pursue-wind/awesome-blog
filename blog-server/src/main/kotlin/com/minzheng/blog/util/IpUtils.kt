package com.minzheng.blog.util

import com.fasterxml.jackson.databind.ObjectMapper
import eu.bitwalker.useragentutils.UserAgent
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException
import javax.servlet.http.HttpServletRequest

/**
 * ip工具类
 *
 * @author 11921
 */
object IpUtils {
    /**
     * 获取用户ip地址
     *
     * @param request 请求
     * @return ip地址
     */
    @JvmStatic
    fun getIpAddress(request: HttpServletRequest): String? {
        var ipAddress: String? = null
        try {
            ipAddress = request.getHeader("x-forwarded-for")
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("Proxy-Client-IP")
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP")
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.remoteAddr
                if ("127.0.0.1" == ipAddress) {
                    // 根据网卡取本机配置的IP
                    var inet: InetAddress? = null
                    try {
                        inet = InetAddress.getLocalHost()
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                    }
                    ipAddress = inet!!.hostAddress
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
                }
            }
        } catch (e: Exception) {
            ipAddress = ""
        }
        return ipAddress
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val ipSource = getIpSource("183.15.206.221")
//        println(ipSource)
//    }

    /**
     * 解析ip地址
     *
     * @param ipAddress ip地址
     * @return 解析后的ip地址
     */
    @JvmStatic
    fun getIpSource(ipAddress: String?): String? {
        return try {
            val url = URL("http://opendata.baidu.com/api.php?query=$ipAddress&co=&resource_id=6006&oe=utf8")
            val reader = BufferedReader(InputStreamReader(url.openConnection().getInputStream(), "utf-8"))
            var line: String? = null
            val result = StringBuffer()
            while (reader.readLine().also { line = it } != null) {
                result.append(line)
            }
            reader.close()
            var mapper = ObjectMapper()
            var readValue = mapper.readValue(result.toString(), MutableMap::class.java)

            return readValue.get("data")?.let {
                var list = it as List<Map<String, String>>
                list[0]["location"]
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取访问设备
     *
     * @param request 请求
     * @return [UserAgent] 访问设备
     */
    @JvmStatic
    fun getUserAgent(request: HttpServletRequest): UserAgent {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"))
    }
}