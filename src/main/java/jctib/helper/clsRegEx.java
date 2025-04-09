/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jctib.helper;
import java.util.regex.*;
import java.util.*;
/**
 *
 * @author jctibor
 */
public abstract class clsRegEx {
    public static boolean matches(String text,String pattern)
    {
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(text);
        return m.find();
    }
    public static ArrayList<String> matchAll(String text, String pattern)
    {
        ArrayList<String> matches=new ArrayList<>();
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(text);
        while (m.find())
            matches.add(m.group());
        
        return matches;
    }
    public static String match(String text, String pattern)
    {
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(text);
        while (m.find())
            return m.group();
        return "";
    }
}
