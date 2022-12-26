package com.authserver.application.port.out.jwt;

import com.authserver.adapter.out.jwt.JwtToken;
import com.authserver.adapter.out.jwt.JwtToken.JwtTokenType;
import com.authserver.adapter.out.jwt.JwtVerifyResult;

public interface JwtPort {

    JwtToken create(String username, JwtTokenType tokenType);

    JwtVerifyResult verify(String token);

}
