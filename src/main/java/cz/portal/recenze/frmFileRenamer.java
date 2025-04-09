/*
pomocný nástroj pro recenze ne z netu, ale z docx a pdf souborů uložených na našem I. Ty mají většinou 
nesystematická jména, která se musí změnit (+přidat info, že je už zpracováno: _____), což ale samozřejmě 
nemohu udělat u sebe na počítači - změnit je to potřeba v Portále na I. Proto tento 
nástroj vytvoří soupis, co se má na co přejmenovat, a nějaký budoucí VBA nástroj 
to v Portále provede...
 */
package cz.portal.recenze;

import java.util.*;
import java.util.stream.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.DefaultListModel;

/**
 *
 * @author jctibor
 */
public class frmFileRenamer extends javax.swing.JDialog {

    DefaultListModel lm=new DefaultListModel();
    DefaultListModel lmDone=new DefaultListModel();
    
    String folderPath="";
            
    public frmFileRenamer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        javax.swing.text.Document d=this.txtFolderPath.getDocument();
        d.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                txtFolderPath_changed(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                txtFolderPath_changed(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                txtFolderPath_changed(de);
            }
        });
        this.lstFiles.setModel(this.lm);
        this.lst_filesDone.setModel(this.lmDone);
        if (Files.isDirectory(Path.of(Recenze.p1+"HO/recenze/")))
        {
            txtFolderPath.setText("/run/media/jctibor/D58B-CDB0/Portál/HO/recenze/");
        }
    }
    public void txtFolderPath_changed(DocumentEvent e)
    {
        String p=txtFolderPath.getText();    
        this.folderPath=p;
        if (Files.isDirectory(Path.of(p)))
        {
            this.lm.clear();
            this.lmDone.clear();
            try
            {
                List<String> rename=new ArrayList<>();
                List<String> renamed=new ArrayList<>();
                if (Files.exists(Path.of(p+"/rename.txt")))
                {
                    rename = Files.readAllLines(Path.of(p+"/rename.txt"));
                    renamed.addAll(rename);
                    for (int i=0;i<rename.size();i++)
                    {
                        rename.set(i, rename.get(i).split("\t")[0]);
                    }
                }
                final List<String> rename_fin=rename;
                Stream<Path> s=Files.list(Path.of(p));
                s.filter(path -> {return path.getFileName().toString()!="rename.txt";})
                 .filter(path -> {return !(rename_fin.contains(path.getFileName().toString()));})
                 .sorted()
                 .forEach(path -> {this.lm.addElement(path.getFileName().toString());});
                
                renamed.forEach(item -> {this.lmDone.addElement(item.replaceAll("\t", "   --->   "));});
                
            }catch(Exception ex){}
            
        }
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstFiles = new javax.swing.JList<>();
        txtFolderPath = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdSelect = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst_filesDone = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lstFiles.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstFilesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstFiles);

        txtFolderPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFolderPathActionPerformed(evt);
            }
        });

        jLabel1.setText("Složka:");

        cmdSelect.setText("vybrat");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });

        lst_filesDone.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lst_filesDone);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdSelect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFolderPath, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFolderPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cmdSelect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFolderPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFolderPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFolderPathActionPerformed

    private void lstFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstFilesMouseClicked
        if (evt.getClickCount()==2)
        {
            String fname=lstFiles.getSelectedValue();
            Runtime run = Runtime.getRuntime();
            String[] cmd;
            if (fname.endsWith(".docx") || fname.endsWith(".doc") || fname.endsWith(".odt"))
                cmd=new String[]{"libreoffice",this.folderPath+lstFiles.getSelectedValue()};
            else
                cmd=new String[]{"okular",this.folderPath+lstFiles.getSelectedValue()};
                
            try
            {
            run.exec(cmd,null);
            }catch (Exception ex){}
            clsReviewMaker.set_source(this.folderPath,lstFiles.getSelectedValue());
            this.setVisible(false);
        }
    }//GEN-LAST:event_lstFilesMouseClicked

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdSelectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstFiles;
    private javax.swing.JList<String> lst_filesDone;
    private javax.swing.JTextField txtFolderPath;
    // End of variables declaration//GEN-END:variables
}
