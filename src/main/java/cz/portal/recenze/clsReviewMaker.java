package cz.portal.recenze;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.*;
import jctib.helper.clsHelper;
import jctib.helper.clsRegEx;

/**
 *
 *  Tato třída vyrábí samotnou recenzi, tedy upravuje vstupní text a metadata 
 *  do výsledného HTML
 */
public class clsReviewMaker {
    
    public static clsBook book_selected;
    public static HashMap<String,String> review_info=new HashMap<>();    
    public static String source="";
    public static String source_dir="";
    
    public interface infDataChangedEventListener
    {
        void book_selected(clsBook b);
        void source_selected(String source);
    }
    private static ArrayList<infDataChangedEventListener> listeners=new ArrayList<>();
    public static void add_listener(infDataChangedEventListener listener)
    {
        listeners.add(listener);
    }
    private static void raise_event(String type)
    {
        for (infDataChangedEventListener l:listeners)
        {
            if (type=="book")
                l.book_selected(clsReviewMaker.book_selected);
            else
                l.source_selected(clsReviewMaker.source);
        }
    }
    
    private static ArrayList<String> get_excerpts(String str,int n,int l)
    {
        if (n<5 || n>20)
            n=5;
        if (l<5 || l>20)
            l=8;
        str=str.replaceAll("<.*?>","");
        String[] words=str.split("[\n\s]");
        ArrayList<String> excerpts=new ArrayList<>();
        
        for (int i=0;i<n;i++)
        {
            String excerpt="";
            int start=(int) ((Math.random()*(words.length-l)));
            for (int j=start;j<start+l;j++)
            {
                excerpt+=words[j]+" ";
            }
            excerpt=excerpt.replaceAll(" {2,}", " ");
            excerpts.add(excerpt.strip());
            
            clsHelper.print(excerpt);
        }
        return excerpts;
    }
    private static String download_book_html(String url)
    {
        String HTML="";
        try
        {
            URL page = new URL(url);
            InputStream io = page.openStream();
            //první stránka, na které si zjistíme, kolik stran recenzí dohromady je
            HTML=new BufferedReader(new InputStreamReader(io))
                    .lines()
                    .collect(Collectors.joining("\n")
            );
        }
        catch(Exception e){}
        
        return HTML;
        
    }
    public static void check_reviews(String new_review)
    {
        if (true)
            return;
        else 
            if (clsReviewMaker.book_selected!=null)
        {
            ArrayList<String> excerpts=get_excerpts(new_review,5, 8);
            String HTML="";
            //první stránka, na které si zjistíme, kolik stran recenzí dohromady je
            HTML=download_book_html(clsReviewMaker.book_selected.str_url);
            ArrayList<String> page_numbers=clsRegEx.matchAll(HTML,"data-page-number=\"([0-9]+)\"");
            int max_pn=0;
            for (String page_number:page_numbers)
            {
                Recenze.print(page_number+" "+clsRegEx.match(page_number, "[0-9]+"));
                String Str_nr=clsRegEx.match(page_number, "[0-9]+");
                int nr=Integer.parseInt(Str_nr);
                max_pn=Math.max(max_pn, nr);
            }
            Recenze.print("Stránky s recenzemi: "+String.valueOf(max_pn));
            for (int i=1;i<=max_pn;i++)
            {
                if (i>1)
                    HTML=download_book_html(clsReviewMaker.book_selected.str_url+"?pageNumber="+String.valueOf(i));
                Recenze.print("Stahuji stránku "+String.valueOf(i));
                int j=0;
                while (j!=-1)
                {
                    int sI=HTML.indexOf("<div class=\"pd-review__description\">", j);
                    int eI;
                    if (sI>-1)
                        eI=HTML.indexOf("</div>",sI);
                    else
                        break;
                    
                    String recenze = HTML.substring(sI, eI);
                    recenze=recenze
                            .replaceAll("<.*?>", " ")
                            .replaceAll("&nbsp;"," ")
                            .replaceAll("[ ]{2,}"," ");
                    
                    int hits=0;
                    for (String e:excerpts)
                    {
                        if (recenze.contains(e))
                            hits++;
                    }
                    Recenze.print(String.valueOf(hits));
                    if (hits>3)
                    {
                        clsHelper.infoBox("Tato recenze pravděpodobně již na e-shopu nahrána je.", "Recenze");
                        i=max_pn+1;
                        break;
                    }
                    j=eI;
                }
                
            }
        }
    }
    private static String author_part()
    {
        String a1=clsReviewMaker.review_info.get("autor");
        if (a1.equals(""))
            a1="?";
        String a2=clsReviewMaker.review_info.get("autor2");
        String rv="autor recenze: <strong>"+a1+"</strong>";
        if (!"".equals(a2))
            rv+=", "+a2;
        return rv;
    }
    
    private static String url_part()
    {
        String url=clsReviewMaker.review_info.get("url");
        if ("".equals(url))
        {
            if (clsReviewMaker.source=="")
                clsHelper.infoBox("Není zadána ani URL recenze, ani soubor. Nechybí něco?", "Chybí zdroj recenze");
            return "";
        }
        String dom=clsRegEx.match(url,"[\\.:^](.*?)(?:\\.cz|\\.com|\\.net|\\.sk|\\.info|\\.edu|\\.blog|\\.wtf|\\.eu)");
        if (url.length()>100)
            return "<a href='url' target='_blank'>"+dom+"</a>";
        else
            return "<a href='url' target='_blank'>"+url+"</a>";
    }

    private static String title_part()
    {
        String t=clsReviewMaker.review_info.get("titulek");
        if (t.equals(""))
            return "<strong>"+clsReviewMaker.book_selected.autor+": "+clsReviewMaker.book_selected.name+"</strong>";
        else 
            return "<strong>"+t+"</strong>";
    }
    private static String for_part()
    {
        if (!clsReviewMaker.review_info.get("pro").equals(""))
            return "<br/><em>Recenze zpracována pro "+clsReviewMaker.review_info.get("pro")+"</em>";
        else
            return "";
    }
    private static String head()
    {
        String rv="<hr/>\n";
        rv+="<span style='font-size:133%'>"+title_part();
        rv+=" - "+author_part()+"</span>\n";
        rv+=url_part();
        rv+="\n\n";
        return rv;
    }
    private static String body(String orig)
    {
        String rv="";
        String[] lines=orig.split("\n");
        for (String l:lines)
        {
            if (l.strip().equals(""))
                continue;
            if (!l.strip().matches("^\s*(</?div>|</?p>|</?h[0-6]>|</?ul>|</?li>).*"))
                l="<p>"+l.strip()+"</p>";
            
            rv+=l+"\n\n";
        }
        return rv;
    }
    private static String tail()
    {
        return "<div style='text-align:right'>"+author_part()+"\n"+url_part()+for_part()+"\n</div>";
    }
            
    public static String make_review(String orig)
    {
        if (clsReviewMaker.book_selected!=null && clsReviewMaker.review_info!=null)
            return head()+body(orig)+tail();
        else
            return "";
    }
    public static String rename_source_file()
    {/*
        pokud je zadán source_file, tedy nejde o recenzi z internetu, ale ze souboru,
        tato metoda vyrobí nové jméno pro tento soubor (obvykle pojmenovaný všelijak
        bez zřejmého smyslu)
        */
        String new_name="";
        if (clsReviewMaker.source!="")
        {
            
            String source_ext="";
            try
            {
                source_ext=clsReviewMaker.source.substring(clsReviewMaker.source.lastIndexOf("."));
            }
            catch(Exception e){}
            
            String surname="";
            Stream<String> nameParts=Arrays.stream(clsReviewMaker.review_info.get("autor").split(" "));
            Object[] namePartsArr=nameParts
                    .filter(item -> {return !(item=="Mgr."  || item=="Bc.");})
                    .toArray();
            String n="";
            if (namePartsArr.length==1)
                n=namePartsArr[0].toString();
            else if (namePartsArr.length>1)
                n=namePartsArr[1].toString();
            else
                n="?";
            new_name=clsExport.normalize_path(clsReviewMaker.book_selected.name)+"_"+n+"_____"+source_ext;
        }
        return new_name;
    }
    public static void set_rev_info(HashMap<String,String> _rev_info)
    {
        clsReviewMaker.review_info=_rev_info;
    }
    public static void set_source(String _source_dir,String _source)
    {
        clsReviewMaker.source_dir=_source_dir;
        clsReviewMaker.source=_source;
        clsReviewMaker.raise_event("source");
    }
    public static void set_book(clsBook _book)
    {
        clsReviewMaker.book_selected=_book;
        clsReviewMaker.raise_event("book");
    }
}
