/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cz.portal.recenze;

import java.nio.file.*;
import jctib.helper.*;
/**
 *
 * @author jctibor
 */
public class Recenze extends clsHelper {
    public static final String str_feed_path="/home/jctibor/Dokumenty/práce/Portál/sc_portalovky.xml";
    public static final String p1="/run/media/jctibor/D58B-CDB0/Portál/";
    public static final String p2="/home/jctibor/Dokumenty/práce/Portál/";
    static Path feed_path;
    
    static clsFeed feed;
    
    public static void main(String[] args) 
    {
        feed_path=Paths.get(str_feed_path);
        
        frmMain frm = new frmMain();
        frm.setVisible(true);
        
        String fs=clsFeed.feed_status();
        if (fs!="" && fs != "neexistuje")
        {
            print("feed načten");
            feed = new clsFeed();
        }
        else
            System.out.println("feed nenačten");   
    }
}
