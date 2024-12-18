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

    public Optional<Account> register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return Optional.empty(); // Invalid input
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists"); // Conflict
        }

        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(acc -> acc.getPassword().equals(password));
    }
}