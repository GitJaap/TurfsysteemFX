/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.services;

import java.nio.ByteBuffer;
import java.util.List;
import javafx.concurrent.Task;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author Jaap
 */
public class ReadCardAndWaitTask extends Task<String> {

    public ReadCardAndWaitTask() {
    }
    @Override
    protected String call(){
                try {
                    
                    CardTerminal terminal = null;

                    // show the list of available terminals
                    TerminalFactory factory = TerminalFactory.getDefault();
                    List<CardTerminal> terminals = factory.terminals().list();
                    String readerName = "";

                    for (int i = 0; i < terminals.size(); i++) {

                        readerName = terminals.get(i).toString()
                                .substring(terminals.get(i).toString().length() - 2);
                        //terminal = terminals.get(i);

                        if (readerName.equalsIgnoreCase(" 0")) {
                            terminal = terminals.get(i);
                        }
                    }
                    if(terminal==null)
                        return "";
                    System.out.println("Wating for card absent");
                    terminal.waitForCardAbsent(0);
                    // Establish a connection with the card
                    System.out.println("Waiting for a card..");
                    terminal.waitForCardPresent(0);

                    Card card = terminal.connect("*");
                    CardChannel channel = card.getBasicChannel();
                      //Read UID
                    byte[] baReadUID = new byte[5];

                    baReadUID = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00,
                            (byte) 0x00, (byte) 0x00 };
                    String Check1 = send(baReadUID, channel);
                    
                    String Check2 = send(baReadUID, channel);
                    if(Check1.equals(Check2))
                        return Check1;
                    else
                        return "";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "";
                }
            }   
      
        /**
         * Send the read uid command to the specified cardchannel.
         * @param cmd
         * @param channel
         * @return hexadecimally formatted string of the uid
         */
        public String send(byte[] cmd, CardChannel channel) {

        String res = "";

        byte[] baResp = new byte[258];
        ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
        ByteBuffer bufResp = ByteBuffer.wrap(baResp);

        // output = The length of the received response APDU
        int output = 0;

        try {
            output = channel.transmit(bufCmd, bufResp);
                } catch (CardException ex) {
                    ex.printStackTrace();
                }

                for (int i = 0; i < output - 2; i++) {
                    res += String.format("%02X", baResp[i]);
                    // The result is formatted as a hexadecimal integer
                }

        return res;
    }
}
