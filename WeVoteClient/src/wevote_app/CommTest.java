package wevote_app;

import gnu.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

public class CommTest
{
	static CommPortIdentifier portId;
	static Enumeration portList;
	static int bauds[] = { 19200, 57600 };

        /**
         * Find COM port that has a modem that has name Main.modemName connected to it.
         *
         */
	public String[] doIt()
	{
            String[] portToUse={" ", " "};
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements())
		{
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				for (int i = 0; i < bauds.length; i++)
				{
					try
					{
						SerialPort serialPort;
						InputStream inStream;
						OutputStream outStream;
						int c;
						String response;
						serialPort = (SerialPort) portId.open("SMSLibCommTester", 1971);
						serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
						serialPort.setSerialPortParams(bauds[i], SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
						inStream = serialPort.getInputStream();
						outStream = serialPort.getOutputStream();
						serialPort.enableReceiveTimeout(1000);
						c = inStream.read();
						while (c != -1)
							c = inStream.read();
						outStream.write('A');
						outStream.write('T');
						outStream.write('\r');
						try
						{
							Thread.sleep(1000);
						}
						catch (Exception e)
						{
						}
						response = "";
						c = inStream.read();
						while (c != -1)
						{
							response += (char) c;
							c = inStream.read();
						}
						if (response.indexOf("OK") >= 0)
						{
							try
							{
								outStream.write('A');
								outStream.write('T');
								outStream.write('+');
								outStream.write('C');
								outStream.write('G');
								outStream.write('M');
								outStream.write('M');
								outStream.write('\r');
								response = "";
								c = inStream.read();
								while (c != -1)
								{
									response += (char) c;
									c = inStream.read();
								}
                                                                if (response.replaceAll("\\s+OK\\s+", "").replaceAll("\n", "").replaceAll("\r", "").equals(Main.modemModel)) {
                                                                    portToUse[0] = portId.getName();
                                                                    portToUse[1] = Integer.toString(bauds[i]);
                                                                    serialPort.close();
                                                                    break;
                                                                }
							}
							catch (Exception e)
							{
							}
						}
						serialPort.close();
					}
					catch (Exception e)
					{
					}
				}
			}
		}
                return portToUse;
	}
}
