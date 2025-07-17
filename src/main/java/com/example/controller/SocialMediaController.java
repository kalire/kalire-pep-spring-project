package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account newAccount = accountService.register(account);
            return ResponseEntity.ok(newAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build(); // Duplicate username
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Other validation failure
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Account found = accountService.login(account.getUsername(), account.getPassword());
        if (found != null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message created = messageService.createMessage(message);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("id") int id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok(message); // returns null body if not found, as specified
    }
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") int id) {
        int rowsAffected = messageService.deleteMessageById(id);
        if (rowsAffected == 1) {
            return ResponseEntity.ok(rowsAffected);
        } else {
            return ResponseEntity.ok().build(); // empty body
        }
    }

 
    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable("id") int id, @RequestBody Message message) {
        try {
            int rowsAffected = messageService.updateMessage(id, message.getMessageText());
            return ResponseEntity.ok(rowsAffected);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable("accountId") int accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}