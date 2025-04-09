/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cz.portal.recenze;

import jctib.helper.clsHelper;

/**
 *
 * @author jctibor
 */
public class clsBook {
    public String name="";
    public String ean="";
    public String kod="";
    public String str_url="";
    public String autor="";
    public clsBook()
    {
    }
    public clsBook(String name, String autor,String ean,String kod, String str_url)
    {
        this.name=name;
        this.autor=autor;
        this.ean=ean;
        this.kod=kod;
        this.str_url=str_url;
    }
    public void dump()
    {
        clsHelper.print("AUtor: "+this.autor);
        clsHelper.print("Název: "+this.name);
        clsHelper.print("EAN: "+this.ean);
        clsHelper.print("Kód: "+this.kod);
        clsHelper.print("URL: "+this.str_url);
    }
}
