package com.github.agadar.famtreestats;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public class FamilyTreeStatisticsMain
{
    
    private final static DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        String results = Calculator.calculate("C:\\Users\\Martin\\Desktop\\donkersloten.csv");
        System.out.println(results);
        //String date = "18-10-1949";
        //LocalDate datetime = LocalDate.parse(date, formatter);
        //System.out.println(datetime);
    }
    
}
