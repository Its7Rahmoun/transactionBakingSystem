package org.sid.ebankingbackend.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.ClinetsOpenFeign.MlClient;
import org.sid.ebankingbackend.dtos.AuthRequest;
import org.sid.ebankingbackend.dtos.requestpredected;
import org.sid.ebankingbackend.dtos.saveUserRequest;
import org.sid.ebankingbackend.entities.UserApp;
import org.sid.ebankingbackend.services.JwtService;
import org.sid.ebankingbackend.services.UserAppService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
public class AuthenticationApi {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserAppService userAppService;
    private UserDetailsService userDetailsService;
    private MlClient mlClient;
    @PostMapping("/auth/logIn")
    public Map<String,String> generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            Collection<String> authorities = new ArrayList<>();
            authentication.getAuthorities().forEach(i-> authorities.add(i.getAuthority()));
            return jwtService.generateToken(authentication.getName(),authorities);
        }else {
            throw new UsernameNotFoundException("Invalid User");
        }
    }
    @GetMapping("/refreshToken")
    public Map<String,String> refreshToken(HttpServletResponse response, HttpServletRequest request){

        String authToken = request.getHeader("Authorization");
        if(authToken!=null && authToken.startsWith("Bearer ")){
            String jwt = authToken.substring(7);
            String username = jwtService.extractUsername(jwt);
            if(null != username){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtService.validateToken(jwt, userDetails)) {

                    Collection<String> authorities = new ArrayList<>();
                    userDetails.getAuthorities().forEach(i-> authorities.add(i.getAuthority()));
                    Claims claims = Jwts.claims();
                    claims.put("roles",authorities);
                    String accesToken = Jwts.builder()
                            .setClaims(claims)
                            .setSubject(username)
                            .setIssuedAt(new Date(System.currentTimeMillis()))
                            .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
                            .signWith(jwtService.getSecKey(), SignatureAlgorithm.HS256).compact();
                    Map<String,String> idTokens= new HashMap<>();
                    idTokens.put("accessToken",accesToken);
                    idTokens.put("refreshToken",jwt);
                    return idTokens;
                }
                else {
                    throw  new RuntimeException("invalid Refresh Token");
                }
            }
            else {
                throw  new RuntimeException("invalid Refresh Token");
            }
        }
        else {
            throw new RuntimeException("requered Authorization Hedaer");
        }
    }

    @PostMapping("/auth/save")
    public UserApp save(@RequestBody saveUserRequest user) throws Exception {
        requestpredected valueUsernameMl =mlClient.update(user);
        if(valueUsernameMl.getPredected().equals("1"))
          return userAppService.saveuser1(user.getNamecustomer(),user.getPassword(),user.getNamecustomer(),user.getRolename());
        else
        throw new Exception("can t use this username its sames like its builds by ia");
    }

    @GetMapping("/auth/test")
    public String  save(){
        return "{'oussama':12}";
    }
}
