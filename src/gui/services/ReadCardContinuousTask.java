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
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * Tries to read a card, if an exception occurs sets the return UID to "". Otherwise returns UID in property
 * @author Jaap
 */
public class ReadCardContinuousTask extends Task<String> {
    
    private boolean waitForCardAbsent;
    
    public ReadCardContinuousTask(boolean waitForCardAbsent) {
        this.waitForCardAbsent = waitForCardAbsent;
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
                    if(waitForCardAbsent)
                        terminal.waitForCardAbsent(0);
                        //try to find a card
                    if(terminal.isCardPresent()){
                        Card card = terminal.connect("*");
                        CardChannel channel = card.getBasicChannel();
                         //Read UID
                        byte[] baReadUID = new byte[5];

                        baReadUID = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00 };
                        String ret =  send(baReadUID, channel);
                        System.out.println("send exited");
                        return ret;
                    }
                    else{
                        return "";
                    }
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
        System.out.println("Send entered");
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
                return res;
            }

            for (int i = 0; i < output - 2; i++) {
                res += String.format("%02X", baResp[i]);
                // The result is formatted as a hexadecimal integer
            }

        return res;
    }
}

