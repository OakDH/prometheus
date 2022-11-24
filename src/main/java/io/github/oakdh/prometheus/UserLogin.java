package io.github.oakdh.prometheus;

public record UserLogin 
(
    long id,
    String username,
    String password,
    String email
) {}
