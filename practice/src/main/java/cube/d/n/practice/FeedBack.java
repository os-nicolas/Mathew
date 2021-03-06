package cube.d.n.practice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cube.d.n.commoncore.BaseApp;
import cube.d.n.commoncore.SES;


public class FeedBack extends FullAct  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_feed_back);


        Button send = (Button) findViewById(R.id.feedback_send);
        final Activity that= this;
        send.setOnClickListener(new View.OnClickListener() {
            boolean active = true;
                                    @Override
                                    public void onClick(View v) {
                                        if (active) {
                                            active = false;
                                            final View overlay = findViewById(R.id.sending);

                                            overlay.setVisibility(View.VISIBLE);

                                            overlay.setAlpha(0);

                                            EditText te = (EditText) findViewById(R.id.feedback_input);

                                            final String body = te.getText().toString();

                                            overlay.animate().alpha(1).setDuration(400).withLayer();


                                            final View overlay2 = findViewById(R.id.sending2);

                                            overlay2.setVisibility(View.VISIBLE);

                                            overlay2.setAlpha(0);

                                            overlay2.animate().alpha(1).setDuration(1000).withLayer();

                                            Thread thm = new Thread() {
                                                @Override
                                                public void run() {sendEmail(body);}
                                            };
                                            thm.start();

                                            Thread th = new Thread() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        this.sleep(800);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    overlay.animate().alpha(0).setDuration(400).withLayer();
                                                    try {
                                                        this.sleep(300);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                    that.finish();
                                                }
                                            };
                                            th.start();
                                        }

                                    }
                                });


    }

    @Override
    public void onResume(){
        super.onResume();
        BaseApp.getApp().recordScreen("feedback");
    }

    //TODO call async
    public void sendEmail(String text){
        //Mail m = new Mail("emailerdrone@yahoo.com", "sklsodfgnbruioshkfjgbuoishgjkfdbi");
        //Mail m = new Mail("emailerdrone@myway.com", "sklsodfgnbruioshkfjgbuoishgjkfdbi");
        //Mail m = new Mail("emailerdone@gmx.com", "*^b(Ga&l;aa0[-9hDFjsdfj1klSSDFmd");
        //Mail m = new Mail("emailerdone@gmail.com", "*^b ( G a & l;a a0[-9j1 md");

        //String to ="MathildaApp@gmail.com";
        //m.setFrom("emailerDrone@yahoo.com");

        //m.setTo(to);
        //m.setSubject("feedback");
        //m.setBody(text);

        try{
            SES.sendEmail("MathildaApp@gmail.com","feedback", text);
           // m.send();
        } catch(Exception e) {

            Log.e("MailApp", "Could not send email", e);
        }

//        // Recipient's email ID needs to be mentioned.
//        String to = "MathildaApp@gmail.com";
//
//        // Sender's email ID needs to be mentioned
//        String from = "MathildaApp@gmail.com";
//
//        // Assuming you are sending email from localhost
//        String host = "localhost";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtp.host", host);
//
//        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
//
//        try{
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("MathildaFeedBack");
//
//            // Now set the actual message
//            message.setText(text);
//
//            // Send message
//            Transport.send(message);
//            Log.d("Send Email","Sent message successfully....");
//        }catch (MessagingException mex) {
//            Log.e("Send Email", mex.toString());
//        }
    }
}
