package com.service.kapai

import com.service.kapai.task.Tasks
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplicationStartupRunner : ApplicationRunner {

    private val logger = LoggerFactory.getLogger(ApplicationStartupRunner::class.java)

    @Autowired
    private lateinit var tasks: Tasks

    override fun run(args: ApplicationArguments) {
        logger.info("应用程序启动完成，开始初始化")
        logger.info("native library path: {}/{}", System.getProperty("user.dir"), "libweb3.so")
        tasks.startCheckCastingTransaction()
    }
}