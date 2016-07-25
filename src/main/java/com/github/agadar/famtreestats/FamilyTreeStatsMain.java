package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Statistics;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 * Test main, to be replaced by swing form.
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public class FamilyTreeStatsMain
{

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            Statistics stats = FamilyTreeStatsCalculator.calculate(file);
            System.out.println(stats);
        }
    }

}
