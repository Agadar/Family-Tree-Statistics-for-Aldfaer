package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Cache;
import com.github.agadar.famtreestats.domain.PeriodYears;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    
    /** Data retrieved from the csv file. */
    private final List<Map<String, String>> CsvData;
    
    /**
     * Constructs a new calculator using data read from the given file.
     * 
     * @param file
     * @throws IOException 
     */
    public FamilyTreeStatsCalculator(File file) throws IOException
    {
        this.CsvData = readPersonsFromFile(file);
    }
    
    /**
     * See calculate(...). Calls it with interval = 0, yearFrom = 0, yearTo = 0.
     * 
     * @return
     */
    public Statistics calculate()
    {      
        return calculate(0, 0);
    }
    
    /**
     * See calculate(...). Calls it with interval = 0.
     * 
     * @param yearFrom
     * @param yearTo
     * @return
     */
    public Statistics calculate(int yearFrom, int yearTo)
    {        
        final Cache cache = new Cache(yearFrom, yearTo);
        final boolean ignoreDates = yearFrom < 1 || yearTo < 1;
        final List<Statistics> stats = calculate(yearFrom, yearTo, 0, cache);
        
        if (ignoreDates)
        {
            return cache.calculateStatistics();
        }
        else
        {
            return stats.get(0);
        }
    }
    
    /**
     * See calculate(...). Calls it with yearFrom = 1 and yearTo = 3000.
     * 
     * @param interval
     * @return
     */
    public List<Statistics> calculate(int interval)
    { 
        return calculate(1, 3000, interval);
    }
    
    /**
     * Uses the persons list to calculate several statistics, returned in a list.
     * 
     * @param yearFrom lower bound
     * @param yearTo upper bound
     * @param interval the interval (in years)
     * @return the calculated statistics, mapped to years
     */
    public List<Statistics> calculate(int yearFrom, int yearTo, int interval)
    {
        return calculate(yearFrom, yearTo, interval, new Cache(null));
    }
    
    /**
     * Uses the persons list to calculate several statistics, returned in a list.
     * 
     * @param yearFrom
     * @param yearTo
     * @param interval
     * @param defaultCache
     * @return
     */
    private List<Statistics> calculate(int yearFrom, int yearTo, int interval, 
            Cache defaultCache)
    {
        // Create cache map.
        final Map<PeriodYears, Cache> cacheYears = new TreeMap<>();
        
        // Iterate over retrieved data.
        for (Map<String, String> map : CsvData)
        {
            // Retrieve and parse values.
            final LocalDate marriageDate = dateStringToDate(map.get(
                    Column.DateMarriage.getColumnName()), YearMonthDayFormat);
            final LocalDate birthDate = dateStringToDate(map.get(
                    Column.DateBirth.getColumnName()), DayMonthYearFormat);
            final LocalDate deathDate = dateStringToDate(map.get(
                    Column.DateDeath.getColumnName()), DayMonthYearFormat);
            final RelationType relationType = relationshipStringToEnum(map.get(
                    Column.TypeRelationship.getColumnName()));
            final Sex sexType = Sex.getByUnderlyingString(map.get(Column.TypeSex.getColumnName()));
            final int id = idStringToInt(map.get(Column.IdSelf.getColumnName()));
            final int fatherId = idStringToInt(map.get(Column.IdFather.getColumnName()));
            final int motherId = idStringToInt(map.get(Column.IdMother.getColumnName()));
            final int relationId = idStringToInt(map.get(Column.IdRelationship.getColumnName()));
            final int partnerId = idStringToInt(map.get(Column.IdPartner.getColumnName()));
            
            // Process avg children at marriate and avg age at marriage.
            Cache curCache = getOrCreateCache(marriageDate, cacheYears,
                yearFrom, yearTo, interval, defaultCache);
            curCache.processChildrenAtMarriage(id, fatherId, motherId, relationId, partnerId, 
                    relationType);
            curCache.processAgeAtMarriage(birthDate, marriageDate, relationType, sexType);
            
            // Process avg age at death and number of deaths.
            curCache = getOrCreateCache(deathDate, cacheYears,
                yearFrom, yearTo, interval, defaultCache);
            curCache.processAgeAtDeath(birthDate, deathDate, sexType);
            curCache.processDeath(deathDate);
            
            // Process births.
            curCache = getOrCreateCache(birthDate, cacheYears,
                yearFrom, yearTo, interval, defaultCache);
            curCache.processBirth(birthDate);
        }

        // Calculate statistics from caches and return them.
        List<Statistics> stats = new ArrayList<>();
        cacheYears.forEach((k, v) -> stats.add(v.calculateStatistics()));
        return stats;
    }
    
    /**
     * Gets or creates a cache from/in the given map, using a period calculated
     * from the given year, yearFrom, yearTo, and interval, and returns it.
     * 
     * @param year
     * @param cacheYears
     * @param yearFrom
     * @param yearTo
     * @param interval
     * @param defaultCache
     * @return 
     */
    private static Cache getOrCreateCache(LocalDate date, Map<PeriodYears, Cache> cacheYears, 
            int yearFrom, int yearTo, int interval, Cache defaultCache)
    {
        // If the given date is null, just return default cache.
        if (date == null)
        {
            return defaultCache;
        }
        
        final int year = date.getYear();
        
        // Just return the given default if year < yearFrom or year > yearTo or
        // yearFrom and yearTo aren't given.
        if (year < yearFrom || year > yearTo || yearFrom < 1 || yearTo < 1)
        {
            return defaultCache;
        }

        // Set the period bounds to the given yearFrom and yearTo.
        int periodStart = yearFrom;
        int periodEnd = yearTo;

        // If interval is given, calculate new values for periodStart and periodEnd.
        if (interval > 0)
        {
            periodStart = year - ((year - yearFrom) % interval);
            periodEnd = Math.min(periodStart + interval - 1, yearTo);
        }

        // Instantiate a period, attempt to retrieve existing cache.
        final PeriodYears period = new PeriodYears(periodStart, periodEnd);
        Cache cache = cacheYears.get(period);

        // If no cache exists mapped to the period, create and add it.
        if (cache == null)
        {
            cache = new Cache(period);
            cacheYears.put(period, cache);
        }
        
        return cache;   // Return retrieved/created cache.
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
