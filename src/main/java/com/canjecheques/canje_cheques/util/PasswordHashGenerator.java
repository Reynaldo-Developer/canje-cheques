package com.canjecheques.canje_cheques.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

        System.out.println("admin123 => " + enc.encode("admin123"));
        System.out.println("user123  => " + enc.encode("user123"));
    }
}