package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    public Account register(Account account){
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new RuntimeException("username is blank");
        }
        if (account.getPassword()== null || account.getPassword().length()<4) {
            throw new RuntimeException("password too short");
        }
        Optional<Account> existing = accountRepository.findByUsername(account.getUsername());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username alredy exists");
            
        }
        return accountRepository.save(account);
    }
    public Account login(String username, String password){
        return accountRepository.findByUsername(username)
        .filter(acc -> acc.getPassword().equals(password))
        .orElse(null);
    }
}
