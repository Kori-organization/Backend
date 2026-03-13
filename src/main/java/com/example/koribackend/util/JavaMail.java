package com.example.koribackend.util;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for handling email operations using Jakarta Mail.
 */
public class JavaMail {

    // Load environment variables from .env file
    private static Dotenv dotenv = Dotenv.load();
    // Retrieve the email password from environment variables
    private static String senha = dotenv.get("EMAIL_PASSWORD");


    public static boolean sendPasswordRecovery(String email, String token, String baseURL, HttpServletRequest request) throws IOException {
        // Set up SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a mail session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("korieducation@gmail.com", senha);
            }
        });

        // Enable debug mode to see mail logs in console
        session.setDebug(true);

        try {
            // Initialize the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("korieducation@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );

            message.setReplyTo(InternetAddress.parse("korieducation@gmail.com"));

            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "quoted-printable");

            // Generate the recovery link
            String link = baseURL + "/checkToken?token=" + token;
            message.setSubject("Recuperação de senha - Kori");

            // Define the HTML body content with inline CSS styling
            String format = "<body style='margin:0; padding:0; background-color:#ffffff; font-family:Arial, Helvetica, sans-serif; user-select:none;'>" +
                    "<div style='width:100%; display:flex; justify-content:center; padding:40px 0;'>" +
                    "    <div style='width:500px; max-width:90%; background-color:#FAF6EB; border:6.12px solid #53BDD9; border-radius:28px; overflow:hidden;'>" +
                    "        <div style='padding:30px; text-align:center;'>" +
                    "            <img src='cid:logoImage' alt='Kori Logo' width='100px' style='margin-bottom:15px;'>" +
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

            // Set content type to HTML and charset to UTF-8
            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(format, "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart imagePart = new MimeBodyPart();

            InputStream imageStream =
                    request.getServletContext()
                            .getResourceAsStream("/assets/logo.png");
            imagePart.setDataHandler(
                    new DataHandler(new ByteArrayDataSource(imageStream, "image/png"))
            );
            imagePart.setContentID("<logoImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            // Attempt to send the message
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            // Log the error if sending fails
            e.printStackTrace();
            return false;
        }
    }

    public static void sendEditGradesAdmin(String email,HttpServletRequest request) throws IOException {
        // Set up SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a mail session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("korieducation@gmail.com", senha);
            }
        });

        // Enable debug mode to see mail logs in console
        session.setDebug(true);

        try {
            // Initialize the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("korieducation@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );

            message.setReplyTo(InternetAddress.parse("korieducation@gmail.com"));

            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "quoted-printable");

            // Generate the recovery link
            message.setSubject("Alteração nas notas do aluno - Kori");

            // Define the HTML body content with inline CSS styling
            String format = "<body style='margin:0; padding:0; background-color:#ffffff; font-family:Arial, Helvetica, sans-serif; user-select:none;'>" +
                    "    <div style='width:100%; display:flex; justify-content:center; padding:40px 0;'>" +
                    "        <div style='width:500px; max-width:90%; background-color:#FAF6EB; border:6.12px solid #53BDD9; border-radius:28px; overflow:hidden;'>" +
                    "            <div style='padding:30px; text-align:center;'>" +
                    "                <img src='cid:logoImage' alt='Kori Logo' width='100px' style='margin-bottom:15px;'>" +
                    "                <h1 style='font-size:26px; font-weight:bold; color:#14323f; margin:0;'>" +
                    "                    Alteração nas notas do aluno" +
                    "                </h1>" +
                    "                <hr style='border:none; border-top:1px solid #dddddd; margin:20px 0;'>" +
                    "                <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                    Olá!" +
                    "                </p>" +
                    "                <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5; padding:8px 10px; background-color:#CCECF4; border-radius:8px;'>" +
                    "                    Informamos que as notas do aluno foram alteradas pela secretaria da escola." +
                    "                </p>" +
                    "                <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                    Caso essa alteração não fosse esperada ou você tenha alguma dúvida, pedimos que entre em contato com nossa equipe para mais informações." +
                    "                </p>" +
                    "                <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                    Clique no botão abaixo para falar conosco por e-mail:" +
                    "                </p>" +
                    "                <a href='mailto:korieducation@gmail.com?subject=Dúvida%20sobre%20alteração%20de%20notas'" +
                    "                   style='display:inline-block; margin:20px 0; padding:14px 40px; background-color:#F5C659; color:#14323f; text-decoration:none; border-radius:999px; font-weight:bold; font-size:15px;'>" +
                    "                    Entrar em contato" +
                    "                </a>" +
                    "                <p style='text-align:left; font-size:14px; color:#333333; margin:10px 0; line-height:1.5;'>" +
                    "                    Se preferir, você também pode responder entrando em contato diretamente pelo e-mail:" +
                    "                    <strong>korieducation@gmail.com</strong>" +
                    "                </p>" +
                    "            </div>" +
                    "            <div style='background-color:#FFBBCF; padding:20px 30px;'>" +
                    "                <p style='font-size:14px; color:#14323f; font-weight:bold; margin:0;'>" +
                    "                    Atenciosamente,<br>" +
                    "                    Educação Infantojuvenil Kori" +
                    "                </p>" +
                    "                <p style='font-size:12px; color:#E3809F; text-align:center; margin-top:10px;'>" +
                    "                    Não responda este email." +
                    "                </p>" +
                    "            </div>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>";

            // Set content type to HTML and charset to UTF-8
            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(format, "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);

            MimeBodyPart imagePart = new MimeBodyPart();

            InputStream imageStream =
                    request.getServletContext()
                            .getResourceAsStream("/assets/logo.png");
            imagePart.setDataHandler(
                    new DataHandler(new ByteArrayDataSource(imageStream, "image/png"))
            );
            imagePart.setContentID("<logoImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            // Attempt to send the message
            Transport.send(message);
        } catch (MessagingException e) {
            // Log the error if sending fails
            e.printStackTrace();
        }
    }


}