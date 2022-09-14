package com.minzheng.blog.service

import cn.hutool.core.date.DateUtil
import cn.hutool.core.date.LocalDateTimeUtil
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.RedisPrefixConst
import com.minzheng.blog.dao.UniqueViewDao
import com.minzheng.blog.dto.UniqueViewDTO
import com.minzheng.blog.entity.UniqueView
import com.minzheng.blog.enums.ZoneEnum
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * 访问量统计服务
 *
 * @author c
 * @date 2021/08/06
 */
@Service
class UniqueViewService(
    private val redisService: RedisService,
    private val uniqueViewDao: UniqueViewDao
) : ServiceImpl<UniqueViewDao, UniqueView>() {

    fun listUniqueViews(): List<UniqueViewDTO> {
        val startTime = DateUtil.beginOfDay(DateUtil.offsetDay(Date(), -7))
        val endTime = DateUtil.endOfDay(Date())
        return uniqueViewDao.listUniqueViews(startTime, endTime)
    }

    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    fun saveUniqueView() {
        // 获取每天用户量
        val count = redisService.sSize(RedisPrefixConst.UNIQUE_VISITOR)
        // 获取昨天日期插入数据
        val uniqueView = UniqueView().apply {
            createTime = LocalDateTimeUtil.offset(
                LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.zone)),
                -1,
                ChronoUnit.DAYS
            )
            viewsCount = count.toInt()
        }

        uniqueViewDao.insert(uniqueView)
    }

    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    fun clear() {
        // 清空redis访客记录
        redisService.del(RedisPrefixConst.UNIQUE_VISITOR)
        // 清空redis游客区域统计
        redisService.del(RedisPrefixConst.VISITOR_AREA)
    }
}