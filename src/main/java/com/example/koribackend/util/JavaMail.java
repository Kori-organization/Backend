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
            String formatacao = "<body style='margin:0; padding:0; background-color:#ffffff; font-family:Arial, Helvetica, sans-serif; user-select:none;'>" +
                    "<div style='width:100%; display:flex; justify-content:center; padding:40px 0;'>" +
                    "    <div style='width:500px; max-width:90%; background-color:#FAF6EB; border:6.12px solid #53BDD9; border-radius:28px; overflow:hidden;'>" +
                    "        <div style='padding:30px; text-align:center;'>" +
                    "            <img src='https://i.postimg.cc/BnnmG73y/logo.png' alt='Kori Logo' width='100px' style='margin-bottom:15px;'>" +
                    "            <h1 style='font-size:26px; font-weight:bold; color:#14323f; margin:0;'>" +
                    "                Esqueceu sua senha?" +
                    "            </h1>" +
                    "            <hr style='border:none; border-top:1px solid #dddddd; margin:20px 0;'>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                Olá!" +
                    "            </p>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5; padding:4px 10px; background-color:#CCECF4;'>" +
                    "                Alguém (esperamos que você!) solicitou a alteração da sua senha do Kori." +
                    "            </p>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                Por favor, clique no botão abaixo para alterar sua senha agora." +
                    "            </p>" +
                    "            <a href='" + link + "' style='display:inline-block; margin:20px 0; padding:14px 60px; background-color:#F5C659; color:#14323f; text-decoration:none; border-radius:999px; font-weight:bold; font-size:15px;'>" +
                    "                Alterar Senha" +
                    "            </a>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                Se você não fez essa solicitação, por favor, desconsidere este e-mail." +
                    "            </p>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                Este link expirará em um dia. Se o seu link tiver expirado, você pode solicitar outro." +
                    "            </p>" +
                    "            <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                Se você solicitou vários e-mails de redefinição, certifique-se de clicar no link mais recente." +
                    "            </p>" +
                    "        </div>" +
                    "        <div style='background-color:#FFBBCF; padding:20px 30px;'>" +
                    "            <p style='font-size:14px; color:#14323f; font-weight:bold; margin:0;'>" +
                    "                Atenciosamente,<br>" +
                    "                Educação Infantojuvenil Kori" +
                    "            </p>" +
                    "            <p style='font-size:12px; color:#E3809F; text-align:center; margin-top:10px;'>" +
                    "                Não responda este email." +
                    "            </p>" +
                    "        </div>" +
                    "    </div>" +
                    "</div>" +
                    "</body>";

            message.setContent(formatacao, "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
