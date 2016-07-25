package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Statistics;
import com.github.agadar.famtreestats.enums.Column;
import com.github.agadar.famtreestats.enums.RelationType;
import com.github.agadar.famtreestats.enums.Sex;
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
public final class FamilyTreeStatsCalculator 
{
    /** Symbol used for splitting values in persons CSV file. */
    private final static String SplitSymbol = ";";

    /** Date formatter for dd-MM-yyyy. */
    private final static DateTimeFormatter DayMonthYearFormat = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    /** Date formatter for yyyy-MM-dd. */
    private final static DateTimeFormatter YearMonthDayFormat = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Reads the persons CSV-file found on the specified path, and uses these
     * persons list to calculate several statistics, returned in a Statistics
     * object.
     * 
     * @param file the persons CSV-file
     * @return the calculated statistics
     * @throws IOException IOException if something went wrong while 
     * finding/reading the file
     */
    public static Statistics calculate(File file) throws IOException
    {
        // Reset MarriedWithChildren and retrieve data from csv file.
        MarriedWithChildren.clear();
        final List<Map<String, String>> results = readPersonsFromFile(file);
        
        // Values for calculating the averages           
        long ageAtMarriageBothTotal = 0;
        int ageAtMarriageBothDivBy = 0;
        long ageAtMarriageMaleTotal = 0;
        int ageAtMarriageMaleDivBy = 0;
        long ageAtMarriageFemaleTotal = 0;
        int ageAtMarriageFemaleDivBy = 0;      
        long ageAtDeathBothTotal = 0;
        int ageAtDeathBothDivBy = 0;
        long ageAtDeathMaleTotal = 0;
        int ageAtDeathMaleDivBy = 0;
        long ageAtDeathFemaleTotal = 0;
        int ageAtDeathFemaleDivBy = 0;       
            
        // Iterate over retrieved data.
        for (Map<String, String> map : results)
        {
            // Retrieve and parse values
            final LocalDate marriageDate = dateStringToDate(map.get(
                    Column.DateMarriage.getColumnName()), YearMonthDayFormat);
            final LocalDate birthDate = dateStringToDate(map.get(
                    Column.DateBirth.getColumnName()), DayMonthYearFormat);
            final LocalDate deathDate = dateStringToDate(map.get(
                    Column.DateDeath.getColumnName()), DayMonthYearFormat);
            final RelationType relationshipType = relationshipStringToEnum(map.get(
                    Column.TypeRelationship.getColumnName()));
            final Sex sexType = Sex.getByUnderlyingString(map.get(Column.TypeSex.getColumnName()));
            final int id = idStringToInt(map.get(Column.IdSelf.getColumnName()));
            final int fatherId = idStringToInt(map.get(Column.IdFather.getColumnName()));
            final int motherId = idStringToInt(map.get(Column.IdMother.getColumnName()));
            final int relationId = idStringToInt(map.get(Column.IdRelationship.getColumnName()));
            final int partnerId = idStringToInt(map.get(Column.IdPartner.getColumnName()));
            
            // Use entry for average age at marriage if birthDate and marriageDate are not null,
            // and relationshipType is 'Marriage'.
            if (birthDate != null && marriageDate != null && 
                relationshipType == RelationType.Marriage)
            {
                ageAtMarriageBothDivBy++;
                long daysBetween = DAYS.between(birthDate, marriageDate);
                ageAtMarriageBothTotal += daysBetween;
                
                if (sexType == Sex.Male)
                {
                    ageAtMarriageMaleDivBy++;
                    ageAtMarriageMaleTotal += daysBetween;
                }
                else if (sexType == Sex.Female)
                {
                    ageAtMarriageFemaleDivBy++;
                    ageAtMarriageFemaleTotal += daysBetween;
                }
            }
            
            // Use entry for average age at death if birthDate and deathDate are not null.
            if (birthDate != null && deathDate != null)
            {
                ageAtDeathBothDivBy++;
                long daysBetween = DAYS.between(birthDate, deathDate);
                ageAtDeathBothTotal += daysBetween;
                
                if (sexType == Sex.Male)
                {
                    ageAtDeathMaleDivBy++;
                    ageAtDeathMaleTotal += daysBetween;
                }
                else if (sexType == Sex.Female)
                {
                    ageAtDeathFemaleDivBy++;
                    ageAtDeathFemaleTotal += daysBetween;
                }
            }
            
            // Register child if both parent id's are known.
            if (fatherId != -1 && motherId != -1 && id != -1)
            {
                MarriedWithChildren.registerChild(id, fatherId, motherId);
            }
            
            // Register couple if relationship id and partner id are known.
            if (relationId != -1 && partnerId != -1 && id != -1 && 
                (relationshipType == RelationType.Marriage ||
                 relationshipType == RelationType.RegisteredPartnership))
            {
                MarriedWithChildren.registerCouple(relationId, id, partnerId);
            }
        }

        // Format and return string.
        final int ageAtMarriageBothResult = averageYears(ageAtMarriageBothTotal, 
                                                         ageAtMarriageBothDivBy);
        final int ageAtMarriageMaleResult = averageYears(ageAtMarriageMaleTotal, 
                                                         ageAtMarriageMaleDivBy);
        final int ageAtMarriageFemaleResult = averageYears(ageAtMarriageFemaleTotal, 
                                                           ageAtMarriageFemaleDivBy);
        final int ageAtDeathBothResult = averageYears(ageAtDeathBothTotal, 
                                                      ageAtDeathBothDivBy);
        final int ageAtDeathMaleResult = averageYears(ageAtDeathMaleTotal, 
                                                      ageAtDeathMaleDivBy);
        final int ageAtDeathFemaleResult = averageYears(ageAtDeathFemaleTotal, 
                                                        ageAtDeathFemaleDivBy);
        final int avgChildrenPerMarriage = MarriedWithChildren.averageNumberOfChildren();
        return new Statistics(ageAtMarriageBothResult, ageAtMarriageMaleResult,
                ageAtMarriageFemaleResult, ageAtDeathBothResult, ageAtDeathMaleResult,
                ageAtDeathFemaleResult, avgChildrenPerMarriage);
    }
    
    /**
     * Convenience method for dividing the given days by the given divideBy
     * and translating it to rounded years.
     * 
     * @param total
     * @param quantity
     * @return 
     */
    private static int averageYears(long days, int divideBy)
    {
        return (int) Math.round((double) days / (float) divideBy / 365);
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
    private static RelationType relationshipStringToEnum(String relationshipStr)
    {
        if (relationshipStr == null)
        {
            return RelationType.Unknown;
        }

        return RelationType.getByUnderlyingString(relationshipStr.trim());
    }
    
    /**
     * Reads the persons CSV-file found on the specified path, and returns the
     * retrieved persons.
     * 
     * @param file the persons CSV-file
     * @return list of maps, each map holding a person's data
     * @throws IOException if something went wrong while finding/reading the file
     */
    private static List<Map<String, String>> readPersonsFromFile(File file) throws IOException
    {
        final List<Map<String, String>> results = new ArrayList<>();
 
        try (final BufferedReader reader = new BufferedReader(new FileReader(file));)
        {
            String line;
            
            // Read column names.
            if ((line = reader.readLine()) == null)
            {
                throw new IOException("Selected file is empty!");
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
