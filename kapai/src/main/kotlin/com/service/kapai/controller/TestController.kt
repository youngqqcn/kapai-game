//package com.service.kapai.controller
//
//import com.service.kapai.task.Tasks
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.tags.Tag
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@Tag(name = "test")
//@RestController
//@RequestMapping("test")
//class TestController(
//    private val task: Tasks
//) {
//
//    @Operation(summary = "动态释放")
//    @GetMapping("statistics")
//    fun statistics() {
//        task.statistics()
//    }
//
//}