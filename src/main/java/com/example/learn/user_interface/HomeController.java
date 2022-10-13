package com.example.learn.user_interface;

import com.example.learn.infrastructure.database.entity.UserEntity;

import com.example.learn.infrastructure.security.CustomUserDetails;
import com.example.learn.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

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


    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser( @RequestBody UserEntity user) {

//         Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

//         Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(jwt);
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/random")
    public ResponseEntity<String> randomStuff(){
        return ResponseEntity.ok("test.get()");
    }

}
