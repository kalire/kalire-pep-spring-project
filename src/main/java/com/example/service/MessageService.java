package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private AccountRepository accountRepo;
    
    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            throw new RuntimeException("Message text is blank");
        }

        if (message.getMessageText().length() > 255) {
            throw new RuntimeException("Message text too long");
        }

        if (!accountRepo.existsById(message.getPostedBy())) {
            throw new RuntimeException("Invalid postedBy account");
        }

        return messageRepo.save(message);
    }
    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }
    public Message getMessageById(int id) {
        return messageRepo.findById(id).orElse(null);
    }

    public int deleteMessageById(int id) {
        if (messageRepo.existsById(id)) {
            messageRepo.deleteById(id);
            return 1;
        }
        return 0;
    }

  
    public int updateMessage(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new RuntimeException("Invalid message text");
        }

        Optional<Message> optional = messageRepo.findById(messageId);
        if (optional.isPresent()) {
            Message message = optional.get();
            message.setMessageText(newText);
            messageRepo.save(message);
            return 1;
        } else {
            throw new RuntimeException("Message ID not found");
        }
    }

   
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepo.findByPostedBy(accountId);
    }
}