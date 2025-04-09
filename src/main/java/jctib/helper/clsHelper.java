/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jctib.helper;
import javax.swing.JOptionPane;

/**
 *
 * @author jctibor
 */
public abstract class clsHelper
{    
    public static void print(String string)
    {
        System.out.println(string);
    }
    public static boolean matches(String text,String pattern)
    {
        return clsRegEx.matches(text, pattern);
    }
    
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    
}
