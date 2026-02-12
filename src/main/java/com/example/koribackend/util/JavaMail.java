package com.example.koribackend.util;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class JavaMail {

    private static Dotenv dotenv = Dotenv.load();
    private static String senha = dotenv.get("EMAIL_PASSWORD");


    public static boolean sendPasswordRecovery(String email, String token, String baseURL) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("korieducation@gmail.com",senha);
            }
        });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("korieducation@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            String link = baseURL + "/checkToken?token=" + token;
            message.setSubject("Test of link email");
            String formatacao = "<body><p>"+ link +"</p></body>";

            message.setContent(formatacao, "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
