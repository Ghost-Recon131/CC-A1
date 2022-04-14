package rmit.cc.a1.EmailConfirmation.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rmit.cc.a1.EmailConfirmation.repository.EmailSender;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailSenderService implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    @Async
    public void sendEmail(String recipient, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject("Please take action on your account");
            mimeMessageHelper.setFrom("registeration@huce.edu.com"); // May like sus phishing email here
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("Failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

}
