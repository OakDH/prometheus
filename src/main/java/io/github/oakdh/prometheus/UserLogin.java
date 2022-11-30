package io.github.oakdh.prometheus;

public record UserLogin 
(
    long id,
    String username,
    String password,
    String email
) {
    public static UserLogin EMPTY = new UserLogin(-1, null, null, null);
}
