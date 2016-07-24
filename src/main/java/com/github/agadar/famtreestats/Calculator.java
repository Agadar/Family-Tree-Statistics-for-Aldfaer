package com.github.agadar.famtreestats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public final class Calculator 
{
    private final static String SplitSymbol = ";";
    private final static String ColumnMarriageDate = "TR_sortdatum";
    private final static String ColumnBirthDate = "Geboorte";
    private final static String ColumnRelationshipType = "huw.type";
    private final static String ColumnDeathDate = "Overlijden";
    private final static String ColumnId = "Intern_nummer";
    private final static String ColumnFatherId = "ID_vader";
    private final static String ColumnMotherId = "ID_moeder";
    private final static String CalculateFormat 
        = "Gem. leeftijd bij huwelijk: %s.%n"
        + "Gem. leeftijd bij overlijden: %s.%n"
        + "Gem. aantal kinderen per huwelijk: %s.";
    private final static DateTimeFormatter dayMonthYearFormat = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final static DateTimeFormatter yearMonthDayFormat = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static String calculate(String filepath) throws IOException
    {
        // Retrieve data from csv file.
        final List<Map<String, String>> results = readCsvFromFile(filepath);
        
        // Values for calculating the averages           
        long ageAtMarriageTotal = 0;
        int ageAtMarriageDivBy = 0;
        long ageAtDeathTotal = 0;
        int ageAtDeathDivBy = 0;
            
        // Iterate over retrieved data.
        for (Map<String, String> map : results)
        {
            // Retrieve and parse values
            final LocalDate marriageDate = toLocalDate(map.get(ColumnMarriageDate), yearMonthDayFormat);
            final LocalDate birthDate = toLocalDate(map.get(ColumnBirthDate), dayMonthYearFormat);
            final RelationshipType relationshipType = toRelationshipType(map.get(ColumnRelationshipType));
            final LocalDate deathDate = toLocalDate(map.get(ColumnDeathDate), dayMonthYearFormat);
            final int id = toInteger(map.get(ColumnId));
            final int fatherId = toInteger(map.get(ColumnFatherId));
            final int motherId = toInteger(map.get(ColumnMotherId));
            
            // Use entry for average age at marriage if birthDate and marriageDate are not null,
            // and relationshipType is 'Marriage'.
            if (birthDate != null && marriageDate != null && 
                relationshipType == RelationshipType.Marriage)
            {
                ageAtMarriageDivBy++;
                ageAtMarriageTotal += DAYS.between(birthDate, marriageDate);
            }
            
            // Use entry for average age at death if birthDate and deathDate are not null.
            if (birthDate != null && deathDate != null)
            {
                ageAtDeathDivBy++;
                ageAtDeathTotal += DAYS.between(birthDate, deathDate);
            }
        }
        
        // Format and return string.
        return String.format(CalculateFormat, ageAtMarriageTotal / ageAtMarriageDivBy / 365,
            ageAtDeathTotal / ageAtDeathDivBy / 365, -1);
    }
    
    private static LocalDate toLocalDate(String dateStr, DateTimeFormatter formatter)
    {
        try
        {
            if (dateStr == null)
            {
                return null;
            }

            dateStr = dateStr.trim().replaceAll("N-", "");

            if (dateStr.isEmpty())
            {
                return null;
            }
        
            return LocalDate.parse(dateStr, formatter);
        }
        catch (DateTimeParseException ex)
        {
            return null;
        }
    }
    
    private static int toInteger(String intStr)
    {
        try
        {
            if (intStr == null)
            {
                return -1;
            }
            
            intStr = intStr.trim();
            
            if (intStr.isEmpty())
            {
                return -1;
            }
            
            return Integer.parseInt(intStr);
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }
    
    private static RelationshipType toRelationshipType(String relationshipStr)
    {
        if (relationshipStr == null)
        {
            return RelationshipType.Unknown;
        }
        
        relationshipStr = relationshipStr.trim();
        
        if (relationshipStr.isEmpty())
        {
            return RelationshipType.Unknown;
        }
        
        RelationshipType type = RelationshipType.getByUnderlyingString(relationshipStr);
        
        if (type == null)
        {
            return RelationshipType.Unknown;
        }
        
        return type;
    }
    
    private static List<Map<String, String>> readCsvFromFile(String filepath) throws IOException
    {
        final List<Map<String, String>> results = new ArrayList<>();
        final File file = new File(filepath);
 
        try (final BufferedReader reader = new BufferedReader(new FileReader(file));)
        {
            String line;
            
            // Read column names.
            if ((line = reader.readLine()) == null)
            {
                throw new RuntimeException("File is empty!");
            }
            
            final String[] columns = line.split(SplitSymbol);
            
            // Read column values.          
            while ((line = reader.readLine()) != null)
            {
                final String[] values = line.split(SplitSymbol);
                final Map entry = new HashMap<>();
                
                for (int i = 0; i < columns.length && i < values.length; i++)
                {
                    entry.put(columns[i], values[i]);
                }
                
                results.add(entry);
            }
        }
        
        return results;
    }
}
