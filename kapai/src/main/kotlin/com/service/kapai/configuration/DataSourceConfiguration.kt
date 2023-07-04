package com.service.kapai.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJdbcRepositories(basePackages = ["com.service.kapai.repository.**"])
@EnableJdbcAuditing
class DataSourceConfiguration : AbstractJdbcConfiguration()
