package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    /*
        in this class:
            Validate Credentials (username password) of logged user
            Create TOKEN
            or Validate
    */

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //getToken from header
       String jwtToken =   getTokenFromHeader(request);

       //validate token
        try {
            if(jwtToken!=null && jwtProvider.validateToken(jwtToken)){
                 String userName =  jwtProvider.extractUserNameFromJwToken(jwtToken); // extract user from token
                 UserDetails userDetails =  userDetailsService.loadUserByUsername(userName); //get security recognized user

                //to place logged user into security context we need to use UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,  //authenticated user
                                null, //if we need extra data about the user we can add here
                                userDetails.getAuthorities() //role of user
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);//place authenticated user in security context
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }
       //This allows subsequent filters to perform their operations before reaching the final resource.
        filterChain.doFilter(request, response);

    }

    //we are extracting Token from header using request.
    private String getTokenFromHeader(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        //TOKEN Format (Value "Authorization" )Bearer adfjladjl;sjw.fkjaslkfjaasdfasfsf.dsjfklasfjalks
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;

    }

    /*
        --> "permitAll" is used to explicitly allow unauthenticated access to a resource or
        endpoint, bypassing authentication and authorization checks.
         -->"shouldNotFilter" is used to specify that a specific filter should not be applied to a particular request,
        bypassing the execution of that filter for the matching request.
   */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antMatcher = new AntPathMatcher();
        return antMatcher.match("/register", request.getServletPath()) ||
                antMatcher.match("/login", request.getServletPath());
    }
}
