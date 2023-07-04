package com.service.kapai

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(
    exclude = [
        WebMvcAutoConfiguration::class,
        SecurityAutoConfiguration::class,
        UserDetailsServiceAutoConfiguration::class
    ],
    scanBasePackages = [
        "com.service",
    ]
)
@EnableScheduling
@EnableAsync
class KaPaiApplication {
    fun run(args: Array<String>) {
        val application = SpringApplication(KaPaiApplication::class.java)
        application.addListeners(ApplicationPidFileWriter())
        application.setAllowCircularReferences(true)
        application.run(*args)
    }
}

fun main(args: Array<String>) {
    KaPaiApplication().run(args)
}