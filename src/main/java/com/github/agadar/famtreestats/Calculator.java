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
 * The class responsible for calculating the family tree statistics.
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
    private final static DateTimeFormatter dayMonthYearFormat = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final static DateTimeFormatter yearMonthDayFormat = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Reads the persons CSV-file found on the specified path, and uses these
     * persons list to calculate several statistics, returned in a Statistics
     * object.
     * 
     * @param filepath the path to the persons CSV-file
     * @return the calculated statistics
     * @throws IOException IOException if something went wrong while 
     * finding/reading the file
     */
    public static Statistics calculate(String filepath) throws IOException
    {
        // Retrieve data from csv file.
        final List<Map<String, String>> results = readPersonsFromFile(filepath);
        
        // Values for calculating the averages           
        long ageAtMarriageTotal = 0;
        int ageAtMarriageDivBy = 0;
        long ageAtDeathTotal = 0;
        int ageAtDeathDivBy = 0;
            
        // Iterate over retrieved data.
        for (Map<String, String> map : results)
        {
            // Retrieve and parse values
            final LocalDate marriageDate = dateStringToDate(map.get(ColumnMarriageDate), yearMonthDayFormat);
            final LocalDate birthDate = dateStringToDate(map.get(ColumnBirthDate), dayMonthYearFormat);
            final RelationshipType relationshipType = relationshipStringToEnum(map.get(ColumnRelationshipType));
            final LocalDate deathDate = dateStringToDate(map.get(ColumnDeathDate), dayMonthYearFormat);
            final int id = idStringToInt(map.get(ColumnId));
            final int fatherId = idStringToInt(map.get(ColumnFatherId));
            final int motherId = idStringToInt(map.get(ColumnMotherId));
            
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
        final int ageAtMarriageBoth = (int) (ageAtMarriageTotal / ageAtMarriageDivBy / 365);
        final int ageAtDeathBoth = (int) (ageAtDeathTotal / ageAtDeathDivBy / 365);
        return new Statistics(ageAtMarriageBoth, -1, -1, ageAtDeathBoth, -1, -1, -1);
    }
    
    /**
     * Attempts to parse the given String to a LocalDate using the supplied
     * formatter, or null if parsing failed.
     * 
     * @param dateStr the String to parse to LocalDate
     * @param formatter the formatter to use
     * @return the parsed LocalDate, or null if parsing failed
     */
    private static LocalDate dateStringToDate(String dateStr, DateTimeFormatter formatter)
    {
        try
        {
            if (dateStr == null)
            {
                return null;
            }

            // Some dates are prefixed with 'N-', so that needs to be removed.
            dateStr = dateStr.trim().replaceAll("N-", "");
            return LocalDate.parse(dateStr, formatter);
        }
        catch (DateTimeParseException ex)
        {
            return null;
        }
    }
    
    /**
     * Attempts to parse the given id value from String to integer. If the given
     * id value is null, empty, or otherwise couldn't be parsed to an integer,
     * then '-1' is returned.
     * 
     * @param idStr the id String to parse to integer
     * @return the parsed id value, or -1 if parsing failed
     */
    private static int idStringToInt(String idStr)
    {
        try
        {
            if (idStr == null)
            {
                return -1;
            }

            return Integer.parseInt(idStr.trim());
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }
    
    /**
     * Returns the RelationshipType to which the given String is mapped, or
     * RelationshipType.Unknown if there exists no type to which the given String
     * is mapped.
     * 
     * @param relationshipStr the String to find the corresponding RelationshipType of
     * @return the corresponding RelationshipType, or RelationshipType.Unknown
     */
    private static RelationshipType relationshipStringToEnum(String relationshipStr)
    {
        if (relationshipStr == null)
        {
            return RelationshipType.Unknown;
        }

        return RelationshipType.getByUnderlyingString(relationshipStr.trim());
    }
    
    /**
     * Reads the persons CSV-file found on the specified path, and returns the
     * retrieved persons.
     * 
     * @param filepath the path to the persons CSV-file
     * @return list of maps, each map holding a person's data
     * @throws IOException if something went wrong while finding/reading the file
     */
    private static List<Map<String, String>> readPersonsFromFile(String filepath) throws IOException
    {
        final List<Map<String, String>> results = new ArrayList<>();
        final File file = new File(filepath);
 
        try (final BufferedReader reader = new BufferedReader(new FileReader(file));)
        {
            String line;
            
            // Read column names.
            if ((line = reader.readLine()) == null)
            {
                throw new IOException("File on path '" + filepath + "' is empty!");
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
