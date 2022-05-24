package de.fraunhofer.fkie;

import edu.gmu.c4i.c2simclientlib2.C2SIMClientException;
import edu.gmu.c4i.c2simclientlib2.C2SIMClientSTOMP_Lib;
import edu.gmu.c4i.c2simclientlib2.C2SIMSTOMPMessage;
import org.w3c.dom.Document;

import java.nio.charset.StandardCharsets;

public class StompTest implements Runnable {

    public static void main(String[] args) {
        Thread t = new Thread(new StompTest());
        t.start();
    }

    /**
     * Thread run() method to receive STOMP
     */
    C2SIMClientSTOMP_Lib stomp;

    public void run() {
        System.out.println("Making STOMP connection");

        // Create STOMP Client Object
        stomp = new C2SIMClientSTOMP_Lib();

        // Set STOMP parameters
        String hostSource = "10.2.10.70";
        stomp.setHost(hostSource);

        // echo parameters to log
        System.out.println("STOMP client parameters: ");
        System.out.println("STOMP host: " + hostSource);

        // Connect to the host
        C2SIMSTOMPMessage response = null;
        boolean subscriberIsOn;
        if (true) {
            System.out.println("Connecting to STOMP server at " +
                    stomp.getHost());
            try {
                response = stomp.connect();
                if (response.getMessageBody() == null) {
                    System.err.println(
                            "Failed to connect to STOMP host: " +
                                    hostSource + " - " + response.getMessageBody());
                    subscriberIsOn = false;
                    System.out.println("not connected");
                    return;
                }
            } catch (C2SIMClientException bce) {
                System.err.println("exception connect to STOMP: " +
                        bce.getCauseMessage());
            }
        }
        System.out.println("STOMP connect response: " +
                response.getMessageType());
        if (response == null) {
            System.out.println("Response = " + response);
            return;
        }
        System.out.println("connected");
        subscriberIsOn = true;

        // Start listening
        C2SIMSTOMPMessage message = null;
        try {
            while (subscriberIsOn) {
                // read message with STOMP blocking until next
                message = stomp.getNext_Block();

                // network read error - display popup and stop reading
                if (message == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ie) {
                    }
                    continue;
                }

                // if we are recording, send a copy to the recorder

                // extract parameters from header
                String selectorDomain =
                        message.getHeader("selectorDomain");
                String protocol = message.getHeader("protocol");
                String protocolVersion =
                        message.getHeader("c2sim-version");
                String submitter = message.getHeader("submitter");
                String firstforwarder =
                        message.getHeader("firstforwarder");
                System.out.println(
                        "received STOMP selectorDomain:" + selectorDomain +
                                " protocol:" + protocol + " submitter:" + submitter +
                                " protocolVersion:" + protocolVersion +
                                " firstforwarder:" + firstforwarder);
                String messageBody = message.getMessageBody().trim();
                if (messageBody.length() == 0) continue;
//              interpretMessage(messageBody, protocolVersion);
                System.out.println("\n Message: \n");
                System.out.println(messageBody);
                // reset message and go back to wait loop
                message = null;

            }// end Subscriber while(subscriberIsOn)

        } catch (NullPointerException npe) {
            System.out.println("STOMP NULLPOINTER");
            npe.printStackTrace();
            subscriberIsOn = false;
            System.out.println("not connected");
        } catch (C2SIMClientException bce) {
            System.err.println("C2SIMClientException:" + bce.getMessage() +
                    " cause:" + bce.getCauseMessage());
            subscriberIsOn = false;
            System.out.println("not connected");
        } catch (Exception e) {
            System.out.println("STOMP SUBSCRIBER EXCEPTION " + e.getMessage());
            e.printStackTrace();
            subscriberIsOn = false;
            System.out.println("not connected");
        }

        // wait for messages to be delivered to the interpretMessage method
        while (subscriberIsOn)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }

        // disconnect STOMP
        if (true) {
            try {
                System.out.println("disconnect");
                stomp.disconnect();
            } catch (C2SIMClientException bce) {
                System.out.println("error disconnecting STOMP:" +
                        bce.getCauseMessage());
            }
        }
        System.out.println("not connected");

    }// end run()
}
