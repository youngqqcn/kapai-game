package com.service.kapai.configuration

import com.service.boot.converter.enums.EnumCodeConverterFactory
import com.service.boot.converter.enums.EnumIntegerConverterFactory
import com.service.boot.converter.enums.EnumStringConverterFactory
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(basePackages = ["com.service.kapai.repository.**"])
@EnableJdbcAuditing //支持@LastModifiedDate与@CreatedDate
class DataSourceConfiguration : AbstractJdbcConfiguration() {
    override fun userConverters(): MutableList<*> {
        return mutableListOf(EnumStringConverterFactory(), EnumIntegerConverterFactory(), EnumCodeConverterFactory())
    }

}
