package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Statistics;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main GUI frame for the program.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public class FamilyTreeStatsGUI extends javax.swing.JFrame
{
    /** Link to this software's repository. */
    private final static String repositoryLink = "https://github.com/Agadar/"
            + "Family-Tree-Statistics-for-Aldfaer";
    
    /** Format for the text printed to the text area. */
    private final static String statsTextFormat = "Average age when married (all): %s years%n"
            + "Average age when married (male): %s years%n"
            + "Average age when married (female): %s years%n%n"
            + "Average age when deceased (all): %s years%n"
            + "Average age when deceased (male): %s years%n"
            + "Average age when deceased (female): %s years%n%n"
            + "Average number of children (married couples): %s%n"
            + "Deaths: %s%n"
            + "Births: %s%n";
       
    /** File chooser filter for the open file dialog. */
    private final JFileChooser fileChooser = new JFileChooser();
    
    /**
     * Creates new form FamilyTreeStatsGUI.
     */
    public FamilyTreeStatsGUI()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        BtnReadFile.requestFocusInWindow();
        
        // Set file extension filter.
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV file "
                + "(*.csv;*.txt)", "csv", "txt");
        fileChooser.addChoosableFileFilter(csvFilter);
        fileChooser.setFileFilter(csvFilter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        BtnReadFile = new javax.swing.JButton();
        ScrollPaneResults = new javax.swing.JScrollPane();
        TextAreaResults = new javax.swing.JTextArea();
        LabelLink = new javax.swing.JLabel();
        TextFieldToDate = new javax.swing.JFormattedTextField();
        TextFieldFromDate = new javax.swing.JFormattedTextField();
        LabelDateAnd = new javax.swing.JLabel();
        ChkBxUseDates = new javax.swing.JCheckBox();
        ChkBxInterval = new javax.swing.JCheckBox();
        ComboBoxInterval = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Family Tree Statistics for Aldfaer 1.1.0");
        setIconImages(null);
        setName(""); // NOI18N

        BtnReadFile.setText("Open & read file");
        BtnReadFile.setFocusPainted(false);
        BtnReadFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnReadFileActionPerformed(evt);
            }
        });

        TextAreaResults.setColumns(20);
        TextAreaResults.setRows(5);
        ScrollPaneResults.setViewportView(TextAreaResults);

        LabelLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelLink.setText("<html>Source code and downloads can be found at the <a href='/'>GitHub repository</a>.</html>");
        LabelLink.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                LabelLinkMouseClicked(evt);
            }
        });

        TextFieldToDate.setEditable(false);
        try
        {
            TextFieldToDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        TextFieldToDate.setValue(2040);

        TextFieldFromDate.setEditable(false);
        try
        {
            TextFieldFromDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        TextFieldFromDate.setValue(1000);

        LabelDateAnd.setText("and");

        ChkBxUseDates.setText("Between the years:");
        ChkBxUseDates.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ChkBxUseDatesActionPerformed(evt);
            }
        });

        ChkBxInterval.setText("Interval (years):");
        ChkBxInterval.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ChkBxIntervalActionPerformed(evt);
            }
        });

        ComboBoxInterval.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "5", "10", "25", "50", "100", "250", "500", "1000" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelLink, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addComponent(ScrollPaneResults)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ChkBxInterval)
                                .addGap(18, 18, 18)
                                .addComponent(ComboBoxInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ChkBxUseDates)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextFieldFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LabelDateAnd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TextFieldToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BtnReadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TextFieldFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelDateAnd)
                            .addComponent(TextFieldToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ChkBxUseDates))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ChkBxInterval)
                            .addComponent(ComboBoxInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(BtnReadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScrollPaneResults, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnReadFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnReadFileActionPerformed
    {//GEN-HEADEREND:event_BtnReadFileActionPerformed

        // Show open file dialog.
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try 
            {
                // Read from the file and do calculations.
                final File file = fileChooser.getSelectedFile();
                int yearFrom = -1;
                int yearTo = -1;
                int interval = -1;
                
                // If we're using dates, assign yearFrom and yearTo.
                if (ChkBxUseDates.isSelected())
                {
                    yearFrom = Integer.valueOf(TextFieldFromDate.getText().trim());
                    yearTo = Integer.valueOf(TextFieldToDate.getText().trim());
                }
                
                // If we're using an interval, assign interval.
                if (ChkBxInterval.isSelected())
                {
                    interval = Integer.valueOf((String) ComboBoxInterval.getSelectedItem());
                }

                List<Statistics> stats = new FamilyTreeStatsCalculator()
                        .calculate(file, yearFrom, yearTo, interval);
                
                stats.forEach((stat ->  
                {
                    String statsText = "Period: " + stat.Period.YearFrom + " to " 
                                       + stat.Period.YearTo + "\n";
                    statsText += String.format(statsTextFormat, stat.AgeAtMarriageBoth,
                        stat.AgeAtMarriageMale, stat.AgeAtMarriageFemale,
                        stat.AgeAtDeathBoth, stat.AgeAtDeathMale, stat.AgeAtDeathFemale,
                        stat.ChildenPerMarriage, stat.Deaths, stat.Births);
                    statsText += "--------------------------------";
                    System.out.println(statsText);
                }));

                // Print the results in the text area.
//                final String statsText = String.format(statsTextFormat, stats.AgeAtMarriageBoth,
//                    stats.AgeAtMarriageMale, stats.AgeAtMarriageFemale,
//                    stats.AgeAtDeathBoth, stats.AgeAtDeathMale, stats.AgeAtDeathFemale,
//                    stats.ChildenPerMarriage, stats.Deaths, stats.Births);
//                TextAreaResults.setText(statsText);
            }
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(this, "Failed to read the selected file.",
                    "An error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_BtnReadFileActionPerformed

    private void LabelLinkMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_LabelLinkMouseClicked
    {//GEN-HEADEREND:event_LabelLinkMouseClicked
        tryOpenLink(repositoryLink);
    }//GEN-LAST:event_LabelLinkMouseClicked

    private void ChkBxUseDatesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ChkBxUseDatesActionPerformed
    {//GEN-HEADEREND:event_ChkBxUseDatesActionPerformed
        TextFieldFromDate.setEditable(ChkBxUseDates.isSelected());
        TextFieldToDate.setEditable(ChkBxUseDates.isSelected());
    }//GEN-LAST:event_ChkBxUseDatesActionPerformed

    private void ChkBxIntervalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ChkBxIntervalActionPerformed
    {//GEN-HEADEREND:event_ChkBxIntervalActionPerformed
        ComboBoxInterval.setEnabled(ChkBxInterval.isSelected());
    }//GEN-LAST:event_ChkBxIntervalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        try
        {
            // Just use the windows look and feel, because that's what we see in
            // the designer. This way, what we see is what we get.
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> 
            {
                new FamilyTreeStatsGUI().setVisible(true);
            });
        }
        catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(FamilyTreeStatsGUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Tries opening the given url in the desktop's default browser.
     * 
     * @param url the url to browse to
     */
    private static void tryOpenLink(String url)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(new URI(url));
            }
            catch (IOException | URISyntaxException ex)
            {
                Logger.getLogger(FamilyTreeStatsGUI.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton BtnReadFile;
    private javax.swing.JCheckBox ChkBxInterval;
    private javax.swing.JCheckBox ChkBxUseDates;
    private javax.swing.JComboBox<String> ComboBoxInterval;
    private javax.swing.JLabel LabelDateAnd;
    private javax.swing.JLabel LabelLink;
    private javax.swing.JScrollPane ScrollPaneResults;
    private javax.swing.JTextArea TextAreaResults;
    private javax.swing.JFormattedTextField TextFieldFromDate;
    private javax.swing.JFormattedTextField TextFieldToDate;
    // End of variables declaration//GEN-END:variables
}
