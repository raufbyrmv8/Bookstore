package com.example.bookstorefull.service;


import com.example.bookstorefull.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;

    public void sendBookPublishedNotification(MailStudentDto mailStudentDto, BookDto bookDto, MailAuthorDto mailAuthorDto){
        String to = mailStudentDto.getName();
        String subject = "New Book Published by " + mailAuthorDto.getName();
        String text = "Dear " + mailStudentDto.getName() + ",\n\n" +
                "Author " + mailAuthorDto.getName() + " has published a new book titled \"" + bookDto.getName() + "\".\n" +
                "Check it out now!\n\nBest Regards,\nBookstore Team";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }
}
