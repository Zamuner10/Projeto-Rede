package com.example.vinicius.config;

import com.example.vinicius.repository.UserRepository;
import com.example.vinicius.service.CustomUserDetailsService;
import com.example.vinicius.service.JwtService;
import com.example.vinicius.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.Map;


@Component
public class WebSocketAuthInterceptor  implements ChannelInterceptor {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    public WebSocketAuthInterceptor(CustomUserDetailsService customUserDetailsService, JwtService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String autHeader = accessor.getFirstNativeHeader("Authorization");
            if(autHeader != null && autHeader.startsWith("Bearer ")){
                String token = autHeader.substring(7);
                String username = jwtService.validateToken(token);

                if (username != null && !username.isEmpty()) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}
