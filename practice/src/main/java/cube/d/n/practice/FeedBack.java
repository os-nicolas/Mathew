package cube.d.n.practice;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class FeedBack extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        sendEmail("this is a test of my awesome feedback system");
    }

    //TODO call async
    public void sendEmail(String text){

        // Recipient's email ID needs to be mentioned.
        String to = "MathildaApp@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "MathildaApp@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("MathildaFeedBack");

            // Now set the actual message
            message.setText(text);

            // Send message
            Transport.send(message);
            Log.d("Send Email","Sent message successfully....");
        }catch (MessagingException mex) {
            Log.e("Send Email", mex.toString());
        }
    }
}
