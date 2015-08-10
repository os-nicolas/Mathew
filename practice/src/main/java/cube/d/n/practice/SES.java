package cube.d.n.practice;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

/**
 * Created by Colin_000 on 8/9/2015.
 */
public class SES {

    public static void sendEmail(String receiver,String message){

        AWSCredentials credentials = Mathilda.getMathilda().getCreds();
        AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient( credentials );

//    String subjectText = "Feedback from " + nameField.getText();
    Content subjectContent = new Content("feedBack");

//    String bodyText = "Rating: " + ratingBar.getRating() + "\nComments\n" + commentsField.getText();
    Body messageBody = new Body(new Content(message));

    Message feedbackMessage = new Message(subjectContent,messageBody);

    String email = PropertyLoader.getInstance().getVerifiedEmail();
    Destination destination = new Destination().withToAddresses(email);

    SendEmailRequest request = new SendEmailRequest(email,destination,feedbackMessage);
    SendEmailResult result = clientManager.ses().sendEmail(request);
    }

}
