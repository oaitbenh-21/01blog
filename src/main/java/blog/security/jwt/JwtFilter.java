package blog.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest req,
            @NotNull HttpServletResponse res,
            @NotNull FilterChain filter)
            throws ServletException, IOException {
        System.out.println("I Arrived Here");
        if (req.getServletPath().contains("api/v1/auth")) {
            filter.doFilter(req, res);
            return;
        }
        final String AuthHeader = req.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        final String jwt;
        System.out.println("I Arrived Here");
        if (AuthHeader == null || !AuthHeader.startsWith("Bearer ")) {
            filter.doFilter(req, res);
            return;
        }
        System.out.println("I Arrived Here||||||||||||||");
        jwt = AuthHeader.substring(7);
        final String userName = jwtService.extractUsername(jwt);
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("I Arrived Here(((((((((((((((");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("I Arrived Here))))))))))");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        System.out.println("I Arrived Here");
        filter.doFilter(req, res);
    }
}
