/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cz.portal.recenze;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
/**
 *
 * @author jctibor
 */

public class clsTimerInput {    
    public interface infTimerInputEventListener {
        void input_received(clsInputEvent e);
    }
    
    public static class clsInputEvent {
        public String type;
        public String value;
        public int counter;
        public clsInputEvent(String _type,String _value)
        {
            this.type=_type;
            this.value=_value;
        }
    }
    
    public static final String defaultText="---clsTimerInput---";
    private static ArrayList<infTimerInputEventListener> listeners=new ArrayList<infTimerInputEventListener>();
    private static int counter=0;
    
    public static void add_listener(infTimerInputEventListener listener)
    {
        listeners.add(listener);
    }
    private static void raise_event(clsInputEvent e)
    {
        counter+=1;
        e.counter=counter;
        for (infTimerInputEventListener l:listeners)
        {
            l.input_received(e);
        }
        setClipboard(defaultText);
        if (counter==5)
            stop();
    }
    
    static Timer T;
    
    public static void start()
    {
        T=new Timer();
        setClipboard(defaultText);
        
        T.scheduleAtFixedRate(
        new TimerTask() {
            @Override
            public void run() {
                String clipboard=clsTimerInput.getClipboard();
                if (!clipboard.equals(clsTimerInput.defaultText))
                {
                    clsTimerInput.raise_event(new clsInputEvent("",clipboard));
                }
            
            }
        },200,200);
    }
    public static void stop()
    {
        T.cancel();
    }
    private static void setClipboard(String text)
    {
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Clipboard clipboard=toolkit.getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);
    }
    private static String getClipboard()
    {
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Clipboard clipboard=toolkit.getSystemClipboard();
        try
        {
            String text=(String)clipboard.getData(DataFlavor.stringFlavor);
            return text;
        }
        catch(Exception e)
        {
            return "";
        }
    }
}
