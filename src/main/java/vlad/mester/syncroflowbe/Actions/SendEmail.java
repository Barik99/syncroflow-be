package vlad.mester.syncroflowbe.Actions;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.json.simple.JSONObject;
import vlad.mester.syncroflowbe.base.Actions;

import java.util.Properties;


@Getter
public class SendEmail extends Actions {
    public static final String type = "Send Email";
    private final String receiver;
    private final String subject;
    private final String body;

    public SendEmail(String name, String receiver, String subject, String body) {
        super(name, type, "Trimite un email la " + receiver + " cu subiectul " + subject + " și conținutul " + body + ".");
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public boolean execute() {
        String host = "smtp.office365.com";
        String port = "587";
        String username = "systemsyncroflow@outlook.com";
        String password = "Montagne@4321#!!!";
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Create a session with an authenticator.
            Authenticator auth = new Authenticator() {
                public jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new jakarta.mail.PasswordAuthentication(username, password);
                }
            };

            Session session = Session.getInstance(properties, auth);

            // Create a new email message.
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(username));
            InternetAddress[] toAddresses = { new InternetAddress(receiver) };
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new java.util.Date());
            msg.setText(body);

            // Send the email.
            Transport.send(msg);
            System.out.println("Email sent successfully.");
            return true;
        } catch (com.sun.mail.smtp.SMTPSendFailedException e) {
            e.printStackTrace();
            // Implement your retry mechanism or logging here.

            return false;
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject action = super.getJSONObject();
        action.put("receiver", this.receiver);
        action.put("subject", this.subject);
        action.put("body", this.body);
        return action;
    }
}
