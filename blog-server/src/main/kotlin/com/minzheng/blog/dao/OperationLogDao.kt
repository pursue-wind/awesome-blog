package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.OperationLog
import org.springframework.stereotype.Repository

/**
 * 操作日志
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface OperationLogDao : BaseMapper<OperationLog>