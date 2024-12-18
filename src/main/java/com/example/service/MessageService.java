package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository; // Verify this is correct!

    public Optional<Message> createMessage(Message message) {
        // Check if the user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            return Optional.empty(); // User ID does not exist
        }

        // Validate message text
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return Optional.empty(); // Invalid message text
        }

        return Optional.of(messageRepository.save(message));
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public Optional<Message> updateMessage(Integer messageId, String newMessageText) {
        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            return Optional.empty();
        }

        return messageRepository.findById(messageId).map(msg -> {
            msg.setMessageText(newMessageText);
            return messageRepository.save(msg);
        });
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}