package com.msb.linkerbackend.dtos;

import com.msb.linkerbackend.models.RefreshToken;

public record JwtTokens(RefreshToken refreshToken, String accessToken) {
}
