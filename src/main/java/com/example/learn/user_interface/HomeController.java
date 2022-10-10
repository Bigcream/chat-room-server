package com.example.learn.user_interface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class HomeController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message){
        redisTemplate.opsForValue().set("loda","hello world");

        // In ra màn hình Giá trị của key "loda" trong Redis
        System.out.println("Value of key loda: "+ redisTemplate.opsForValue().get("loda"));
        Optional<String> test = Optional.ofNullable(redisTemplate.opsForValue().get("loda"));
        return ResponseEntity.ok(test.get());
    }

}
