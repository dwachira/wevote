// ReadMessages.java - Sample application.
//
// This application shows you the basic procedure needed for reading
// SMS messages from your GSM modem, in synchronous mode.
//
// Operation description:
// The application setup the necessary objects and connects to the phone.
// As a first step, it reads all messages found in the phone.
// Then, it goes to sleep, allowing the asynchronous callback handlers to
// be called. Furthermore, for callback demonstration purposes, it responds
// to each received message with a "Got It!" reply.
//
// Tasks:
// 1) Setup Service object.
// 2) Setup one or more Gateway objects.
// 3) Attach Gateway objects to Service object.
// 4) Setup callback notifications.
// 5) Run

package wevote_app;

import java.util.ArrayList;
import javax.crypto.spec.SecretKeySpec;
import org.smslib.AGateway;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Service;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.TimeoutException;
import org.smslib.crypto.AESKey;
import org.smslib.modem.SerialModemGateway;

public class DeleteMessages
{
	Service srv;
        /**
         * Deletes all messages from SIM cards memory.
         *
         */
	public void doIt(int mode) throws Exception
	{
		// Define a list which will hold the read messages.
		ArrayList<InboundMessage> msgList;

		// Create the notification callback method for inbound & status report
		// messages.
		InboundNotification inboundNotification = new InboundNotification();

		// Create the notification callback method for inbound voice calls.
		CallNotification callNotification = new CallNotification();

		//Create the notification callback method for gateway statuses.
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();

		OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();

		try
		{

			// Create new Service object - the parent of all and the main interface
			// to you.
                        //System.out.println("222");
			this.srv = new Service();
                        

			// Create the Gateway representing the serial GSM modem.
			SerialModemGateway gateway = new SerialModemGateway("modem.com1", Main.commPort, Main.baud, Main.modemManufactorer, Main.modemModel);

                        
			// Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
			gateway.setProtocol(Protocols.PDU);

			// Do we want the Gateway to be used for Inbound messages?
			gateway.setInbound(true);

			// Do we want the Gateway to be used for Outbound messages?
			gateway.setOutbound(true);

			// Let SMSLib know which is the SIM PIN.
			gateway.setSimPin(Main.pinCode);

			// Set up the notification methods.
			this.srv.setInboundMessageNotification(inboundNotification);
			this.srv.setCallNotification(callNotification);
			this.srv.setGatewayStatusNotification(statusNotification);
			this.srv.setOrphanedMessageNotification(orphanedMessageNotification);

			// Add the Gateway to the Service object.
			this.srv.addGateway(gateway);

			// Similarly, you may define as many Gateway objects, representing
			// various GSM modems, add them in the Service object and control all of them.

			// Start! (i.e. connect to all defined Gateways)
			this.srv.startService();

			// In case you work with encrypted messages, its a good time to declare your keys.
			// Create a new AES Key with a known key value.
			// Register it in KeyManager in order to keep it active. SMSLib will then automatically
			// encrypt / decrypt all messages send to / received from this number.
			this.srv.getKeyManager().registerKey(Main.modemNumber, new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));

			// Read Messages. The reading is done via the Service object and
			// affects all Gateway objects defined. This can also be more directed to a specific
			// Gateway - look the JavaDocs for information on the Service method calls.
			msgList = new ArrayList<InboundMessage>();
//                        if(mode ==1) {
//                            Main.answersArrayTemp = msgList;
//                        } else if( mode == 2) {
//                            Main.answersArrayTemp = msgList;
//                        }

                        if (!Main.deviceDetected) {
                            Main.deviceDetected = true;
                            Main.frame.refreshLog("The device has been found.");
                        }

			this.srv.readMessages(msgList, MessageClasses.ALL);
			for (InboundMessage msg : msgList) {
                            gateway.deleteMessage(msg);
                            //System.out.println("DELETED FROM DELETE");
                        }
                        Main.frame.setBattery(Integer.toString(gateway.getBatteryLevel()));
                        Main.frame.setSignal(Integer.toString(gateway.getSignalLevel()));

                                
		}
		catch (Exception e)
		{
                    Main.frame.setBattery("0");
                    Main.frame.setSignal("0");
                    Main.frame.refreshLog("Please connect your modem/mobile phone to " + Main.commPort +". (Connection could not be established)" );
                    Main.deviceDetected = false;
                    //e.printStackTrace();
		}

		finally
		{
			this.srv.stopService();
                        //System.out.println("Stopped");
		}
	}

	public class InboundNotification implements IInboundMessageNotification
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg)
		{
			if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
			else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
			//System.out.println(msg);
		}
	}

	public class CallNotification implements ICallNotification
	{
		public void process(AGateway gateway, String callerId)
		{
			//System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			//System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}

	public class OrphanedMessageNotification implements IOrphanedMessageNotification
	{
		public boolean process(AGateway gateway, InboundMessage msg)
		{
			//System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
			//System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned message part.
			return false;
		}
	}
}
