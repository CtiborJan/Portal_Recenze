/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cz.portal.recenze;
import static cz.portal.recenze.Recenze.feed_path;
import static cz.portal.recenze.Recenze.str_feed_path;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import jctib.helper.*;
/**
 *
 * @author jctibor
 */
public class clsFeed {
    Document doc;
    public clsFeed()
    {
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();        
            this.doc = db.parse(new File(str_feed_path));
            
        }
        catch(Exception e){System.out.println("Chyba v clsFeed:clsFeed");}
    }
    public static void download_feed() throws Exception
    {
        System.out.println("Začínám stahovat xml feed");
        URL feed = new URL("https://obchod.portal.cz/provisiondata/sc_portalovky.xml");
        InputStream io = feed.openStream();
        Files.copy(io, feed_path,StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(feed_path)==true)
            System.out.println("Soubor uložen");
        else
            System.out.println("Soubor nestažen!");
        
    }
    public static String feed_status()
    {
        if (Files.exists(feed_path)==false)
            return "neexistuje";
        else
            try
            {
                return Files.getLastModifiedTime(feed_path).toString();
            }
            catch(IOException e)
            {
                return "";
            }
    }
    
    public NodeList xpath(String str_xpath)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try
        {
            NodeList r;
            r=(NodeList)xpath.compile(str_xpath).evaluate(this.doc,XPathConstants.NODESET);
            return r;
        }
        catch(Exception e)
        {
            clsHelper.print("chyba v clsFeed:xpath > " + e.getMessage());
            return null;
        }
    }
    private clsBook get_book_from_node(Node n)
    {
        clsBook b=new clsBook();
        if (n.getNodeName()=="SHOPITEM")
        {
            NodeList nl=n.getChildNodes();
            Node n2;
            for (var i=0;i<nl.getLength();i++)
            {
                n2=nl.item(i);
                String n2nn=n2.getNodeName();
                if ("PRODUCTNAME".equals(n2nn))
                    b.name=n2.getTextContent();
                else if ("EAN".equals(n2nn))
                    b.ean=n2.getTextContent();
                else if ("PRODUCTNO".equals(n2nn))
                    b.kod=n2.getTextContent();
                else if ("URL".equals(n2nn))
                    b.str_url=n2.getTextContent();
                else if ("PARAM".equals(n2nn))
                {
                    String i0=n2.getChildNodes().item(0).getTextContent();
                    if ("Autor".equals(i0))
                    {
                        b.autor=n2.getChildNodes().item(1).getTextContent();
                    }
                    
                }
            }
        }
            return b;
    }
    public ArrayList<clsBook> find_books(String ident)
    {
        String xpath="";
        ArrayList<clsBook> books=new ArrayList();
        if (clsRegEx.matches(ident,"[0-9]{13}"))
        {
            xpath="/SHOP/SHOPITEM[EAN/text()=\""+ident+"\"]";
        }
        else if (clsHelper.matches(ident, "[0-9]{8}"))
        {
            xpath="/SHOP/SHOPITEM[ITEM_ID='"+ident+"']";
        }
        else
        {
            xpath="/SHOP/SHOPITEM[starts-with(PRODUCTNAME,'"+ident+"')]";
        }
        //clsHelper.print(xpath);
        NodeList nl=this.xpath(xpath);
        for (var i=0;i<nl.getLength();i++)
        {
            Node n=nl.item(i);
            books.add(this.get_book_from_node(n));
        }
        return books;
    }
}
