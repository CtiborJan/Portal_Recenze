package cz.portal.recenze;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import jctib.helper.clsHelper;
import java.time.format.DateTimeFormatter;


public class clsExport 
{/*
    třída zajišťující export do souboru a tak
    */

    public static String normalize_path(String p)
    {
        return p.replace('*', '_')
                .replace(" ","_")
                .replace("__","_");
    }
    public static String export(String text)
    {
         //exportujeme uloženou recenzi
        String log="";
        if (clsReviewMaker.book_selected==null || text.strip()=="" || clsReviewMaker.review_info.size()==0)
            return "";//pokud nejsou potřebná data, nemáme co dělat
        
        String p;//adresa,kam budeme ukládat
        if (Files.exists(Path.of(Recenze.p1), LinkOption.NOFOLLOW_LINKS))
            p=Recenze.p1;
        else if (Files.exists(Path.of(Recenze.p2), LinkOption.NOFOLLOW_LINKS))
            p=Recenze.p2;
        else
        {
            clsHelper.infoBox("Nenalezena žádná adresa k uložení.", "Recenze");
            return "";
        }
        
        LocalDate obj_date = LocalDate.now();
        DateTimeFormatter date_formatter=DateTimeFormatter.ofPattern("dd_MM_yyyy");
        String str_date=date_formatter.format(obj_date);
        
        p+="HO/Recenze-export/"+str_date;
        if (!Files.exists(Path.of(p), LinkOption.NOFOLLOW_LINKS))
        {
            try
            {
                Files.createDirectories(Path.of(p));
                Recenze.print("Vytvořena složka "+p);
            }
            catch(Exception e){Recenze.print(p+" "+e.getMessage());}
        }

        String fname=clsExport.normalize_path(clsReviewMaker.book_selected.name);//jméno knihy->jméno souboru
        String nr="";
        int i=1;
        while (Files.exists(Path.of(p+"/"+fname+nr), LinkOption.NOFOLLOW_LINKS))
        {//pořadové číslo, pokud je už soubor tohoto jména existuje
            i++;
            nr="_"+String.valueOf(i);
        }
        p=p+"/"+fname+nr;
        try
        {
            
            
            p=clsExport.normalize_path(p);
            log="Ukládám do "+p+"\n";
            
            String renamed_source=clsReviewMaker.rename_source_file();
            Files.createFile(Path.of(p));
            FileWriter fw=new FileWriter(p);
            fw.write("recenze "+clsReviewMaker.book_selected.name);
            fw.write(LocalDate.now().toString()+"\n");
            fw.write(clsReviewMaker.book_selected.ean+"\n");
            fw.write(clsReviewMaker.book_selected.kod+"\n");
            fw.write(clsReviewMaker.source +"\t=>\t"+renamed_source+"\n");
            fw.write("RECENZE:\n");
            fw.write(text);

            fw.close();
            
            if (renamed_source!="")
            {
                String source_dir=clsReviewMaker.source_dir;
                FileWriter fw2=new FileWriter(source_dir+"/rename.txt",true);
                fw2.write(clsReviewMaker.source +"\t"+ renamed_source);
                log+="Zaznamenána změna jména souboru "+clsReviewMaker.source +" => "+renamed_source;
                fw2.close();
            }
            clsHelper.infoBox(log, "Export recenze");
            return p;
        }catch(Exception e){Recenze.print(e.getMessage());}
        return "";
    }
}
