package edu.ukma.jwt;

public interface TokenProvider {
    String provide(String username);
}
