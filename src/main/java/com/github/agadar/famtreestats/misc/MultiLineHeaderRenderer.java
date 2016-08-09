package com.github.agadar.famtreestats.misc;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Used for multi-line column headers in Swing tables. Original code can be found
 * at http://www.java2s.com/Code/Java/Swing-Components/MultiLineHeaderExample.htm.
 */
public class MultiLineHeaderRenderer extends JList implements TableCellRenderer
{
    public MultiLineHeaderRenderer()
    {
        setOpaque(true);
        setForeground(UIManager.getColor("TableHeader.foreground"));
        setBackground(UIManager.getColor("TableHeader.background"));
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        final ListCellRenderer renderer = getCellRenderer();
        ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
        setCellRenderer(renderer);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        setFont(table.getFont());
        final String str = (value == null) ? "" : value.toString();
        final BufferedReader br = new BufferedReader(new StringReader(str));
        final List<Object> v = new ArrayList<>();
        String line;

        try
        {
            while ((line = br.readLine()) != null)
            {
                v.add(line);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(MultiLineHeaderRenderer.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        setListData(v.toArray());
        return this;
    }
}
