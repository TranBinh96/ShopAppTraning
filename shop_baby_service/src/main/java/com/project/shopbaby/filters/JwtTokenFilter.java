package com.project.shopbaby.filters;

import com.project.shopbaby.components.JwtTokenUtil;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends  OncePerRequestFilter {

    @Value("${api.prefix}")
    private  String apiUrl ;
    private final   UserDetailsService userDetailsService;
    private  final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull  FilterChain filterChain)
            throws ServletException, IOException {

        //filterChain.doFilter(request,response);
        try {
            if (isByPassToken(request)){
                filterChain.doFilter(request, response); //enable bypass
                return;
            }

            final  String authHeader = request.getHeader("Authorization");
            if (authHeader == null && !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"UNAUTHORIZED");
                return;
            }else {
                final  String token = authHeader.substring(7);
                String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                if (phoneNumber!= null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
                    if (jwtTokenUtil.validateToken(token,userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request,response); // enable bypass
        }catch (Exception exception){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }

    private  boolean isByPassToken(@NonNull HttpServletRequest request){

        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles", apiUrl), "GET"),
                Pair.of(String.format("%s/products", apiUrl), "GET"),
                Pair.of(String.format("%s/categories", apiUrl), "GET"),
                Pair.of(String.format("%s/users/register", apiUrl), "POST"),
                Pair.of(String.format("%s/users/login", apiUrl), "POST")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return  false;

    }
}
