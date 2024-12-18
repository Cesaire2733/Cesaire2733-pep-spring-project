package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



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
 
     // User Registration
     @PostMapping("/register")
     public ResponseEntity<?> register(@RequestBody Account account) {
         try {
             return accountService.register(account)
                     .map(ResponseEntity::ok)
                     .orElse(ResponseEntity.status(400).build()); // Invalid input
         } catch (IllegalStateException e) {
             return ResponseEntity.status(409).body(e.getMessage()); // Duplicate username
         }
     }
 
     // User Login
     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody Account account) {
         return accountService.login(account.getUsername(), account.getPassword())
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.status(401).build());
     }
 
     // Create a new message
     @PostMapping("/messages")
public ResponseEntity<?> createMessage(@RequestBody Message message) {
    return messageService.createMessage(message)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(400).body(new Message()));
}
 
     // Retrieve all messages
     @GetMapping("/messages")
     public ResponseEntity<List<Message>> getAllMessages() {
         return ResponseEntity.ok(messageService.getAllMessages());
     }
 
     // Retrieve a message by ID
     @GetMapping("/messages/{messageId}")
     public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
         return messageService.getMessageById(messageId)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.ok().build());
     }
 
     // Delete a message
     @DeleteMapping("/messages/{messageId}")
     public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
         if (messageService.deleteMessageById(messageId)) {
             return ResponseEntity.ok(1);
         }
         return ResponseEntity.ok().build();
     }
 
     // Update a message text
     @PatchMapping("/messages/{messageId}")
     public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
         return messageService.updateMessage(messageId, message.getMessageText())
                 .map(updatedMsg -> ResponseEntity.ok(1))
                 .orElse(ResponseEntity.status(400).build());
     }
 
     // Retrieve all messages by a particular user
     @GetMapping("/accounts/{accountId}/messages")
     public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
         return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
     }
 }