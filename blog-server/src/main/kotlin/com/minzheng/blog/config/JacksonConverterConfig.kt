package com.minzheng.blog.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.minzheng.blog.logger
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean
import java.io.IOException
import java.util.*

@Configuration
class JacksonConverterConfig {

    @Bean
    fun jackson2ObjectMapperFactoryBean(): Jackson2ObjectMapperFactoryBean {
        val jackson2ObjectMapperFactoryBean = Jackson2ObjectMapperFactoryBean()
        jackson2ObjectMapperFactoryBean.setDeserializers(DateJacksonConverter())
        return jackson2ObjectMapperFactoryBean
    }

    class DateJacksonConverter : JsonDeserializer<Date>() {
        companion object {
            private val pattern = arrayOf("yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy-MM-dd")
        }

        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): Date? {
            var resultDate: Date? = null
            val originDate = jsonParser.text
            if (StringUtils.isNotEmpty(originDate)) {

                if (originDate.length == 13) {
                    resultDate = try {
                        val longDate = originDate.toLong()
                        Date(longDate)
                    } catch (ex: NumberFormatException) {
                        logger().error("parse error: {}", ex.message)
                        throw IOException("parse error")
                    }
                }
                if (originDate.length == 19 || originDate.length == 10) {
                    resultDate = try {
                        DateUtils.parseDate(originDate, *pattern)
                    } catch (ex: NumberFormatException) {
                        logger().error("parse error: {}", ex.message)
                        throw IOException("parse error")
                    }
                }
            }
            return resultDate
        }

        override fun handledType(): Class<*> {
            return Date::class.java
        }
    }

    class BooleanStrJacksonConverter : JsonDeserializer<Int>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): Int? {
            var result: Int? = null
            val originStr = jsonParser.text
            if (StringUtils.isNotEmpty(originStr)) {
                if (originStr.equals("true")) {
                    result = 1
                }
                if (originStr.equals("false")) {
                    result = 0
                }
            }
            return result
        }

        override fun handledType(): Class<*> {
            return Int::class.java
        }
    }
}

