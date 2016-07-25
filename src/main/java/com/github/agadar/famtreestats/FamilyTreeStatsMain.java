package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Statistics;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public class FamilyTreeStatsMain
{
    
    private final static DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        Statistics stats = FamilyTreeStatsCalculator.calculate("C:\\Users\\Martin\\Desktop\\donkersloten.csv");
        System.out.println(stats);
        
    }
    
}
