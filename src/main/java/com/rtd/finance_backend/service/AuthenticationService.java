package com.rtd.finance_backend.service;

import com.rtd.finance_backend.dto.AuthenticationRequest;
import com.rtd.finance_backend.dto.AuthenticationResponse;
import com.rtd.finance_backend.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String refreshToken);
}
