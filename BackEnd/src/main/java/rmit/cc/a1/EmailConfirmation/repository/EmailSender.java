package rmit.cc.a1.EmailConfirmation.repository;

public interface EmailSender {

    void sendEmail(String recipient, String content);

}
