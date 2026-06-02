package com.jug.url.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naijajug.saasurlshortner.dto.helper.TokenClaims;
import com.naijajug.saasurlshortner.exceptions.AccessDeniedException;
import com.naijajug.saasurlshortner.exceptions.ErrorResponse;
import com.naijajug.saasurlshortner.service.UserLoginSessionService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserLoginSessionService userLoginSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        TokenClaims claims = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            claims = jwtService.extractTokenClaims(token);
        }

        try {
            if (claims != null && claims.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getUsername());
                if (claims.getTenancyId() == null || claims.getUserSessionId() == null){
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Invalid token");
                    return;
                }
                String userActiveSessionId = userLoginSessionService.getUserSession(UUID.fromString(claims.getTenancyId()));
                if (!userActiveSessionId.equals(claims.getUserSessionId())){
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Invalid token");
                    return;
                }
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        }catch (AccessDeniedException ex){

            formatResponse(response,ex.getMessage(),HttpStatus.UNAUTHORIZED,HttpServletResponse.SC_UNAUTHORIZED);
        }catch (ExpiredJwtException ex){
            formatResponse(response,"Invalid auth token",HttpStatus.FORBIDDEN,HttpServletResponse.SC_FORBIDDEN);
        }catch (Exception ex){
            ex.printStackTrace();
            formatResponse(response,"Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void formatResponse(HttpServletResponse response,String message,HttpStatus httpStatus,int httpServletResponse) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .statusCode(httpStatus.value())
                .message(message)
                .build();

        response.setStatus(httpServletResponse);
        response.setContentType("application/json");

        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(error));
    }
}