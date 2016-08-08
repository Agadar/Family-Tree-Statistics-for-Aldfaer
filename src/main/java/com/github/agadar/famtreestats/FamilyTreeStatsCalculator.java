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
    
    public Statistics calculate(File file) throws IOException
    {      
        return calculate(file, 0, 0);
    }
    
    public Statistics calculate(File file, int yearFrom, int yearTo) throws IOException
    { 
        final List<Map<String, String>> csvData = readPersonsFromFile(file);        
        final Cache cache = new Cache(new PeriodYears(yearFrom, yearTo));
        final boolean ignoreDates = yearFrom < 1 || yearTo < 1;
        
        // Iterate over retrieved data.
        for (Map<String, String> map : csvData)
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
            if (ignoreDates || (marriageDate != null && marriageDate.getYear() >= yearFrom &&
                marriageDate.getYear() <= yearTo))
            {
                cache.processChildrenAtMarriage(id, fatherId, motherId, relationId, 
                    partnerId, relationType);
                cache.processAgeAtMarriage(birthDate, marriageDate, relationType, sexType);
            }
            
            // Process avg age at death and number of deaths.
            if (ignoreDates || (deathDate != null && deathDate.getYear() >= yearFrom &&
                deathDate.getYear() <= yearTo))
            {
                cache.processAgeAtDeath(birthDate, deathDate, sexType);
                cache.processDeath(deathDate);
            }
            
            // Process births.
            if (ignoreDates || (birthDate != null && birthDate.getYear() >= yearFrom &&
                birthDate.getYear() <= yearTo))
            {
                cache.processBirth(birthDate);
            }
        }
        
        return cache.calculateStatistics();
    }
    
    public List<Statistics> calculate(File file, int interval) throws IOException
    { 
        return calculate(file, 1, 2999, interval);
    }
    
    /**
     * Reads the persons CSV-file found on the specified path, and uses this
     * persons list to calculate several statistics, returned in a set. If either 
     * yearFrom or yearTo == -1 then those values are ignored. If interval == -1 
     * then the value is ignored.
     * 
     * @param file the persons CSV-file
     * @param yearFrom lower bound
     * @param yearTo upper bound
     * @param interval the interval (in years)
     * @return the calculated statistics, mapped to years
     * @throws IOException IOException if something went wrong while 
     * finding/reading the file
     */
    public List<Statistics> calculate(File file, int yearFrom, int yearTo, int interval) throws IOException
    {
        // Retrieve data from csv file.       
        final List<Map<String, String>> csvData = readPersonsFromFile(file);

        // Create cache maps.    
        final List<Cache> cacheSet = new ArrayList<>();
        final Map<Integer, Cache> cacheYears = new TreeMap<>(); 
        final Cache defaultCache = new Cache(null);
//        int curStart = yearFrom;
//        
//        // Fill in for the intervals, but only if an interval is set.
//        if (interval != -1)
//        {
//            int curEnd = yearFrom + interval - (yearFrom % interval) - 1;
//            
//            while (curEnd < yearTo)
//            {
//                // Fill cacheSet.
//                final PeriodYears curPeriod = new PeriodYears(curStart, curEnd);
//                final Cache cache = new Cache(curPeriod);
//                cacheSet.add(cache);
//
//                // Fill cacheYears.
//                for (int i = curStart; i <= curEnd; i++)
//                {
//                    cacheYears.put(i, cache);
//                }
//
//                // Increment values for next loop.
//                curStart = curEnd + 1;
//                curEnd += interval;
//            }
//        }
//        
//        // Fill cacheSet.
//        final PeriodYears curPeriod = new PeriodYears(curStart, yearTo);
//        final Cache cache = new Cache(curPeriod);
//        cacheSet.add(cache);
//        
//        // Fill cacheYears.
//        for (int i = curStart; i <= yearTo; i++)
//        {
//            cacheYears.put(i, cache);
//        }
        
        // Iterate over retrieved data.
        for (Map<String, String> map : csvData)
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
            if (marriageDate != null)
            {
                Cache curCache = getOrCreateCache(marriageDate.getYear(), cacheYears,
                    cacheSet, yearFrom, yearTo, interval, defaultCache);
                //Cache curCache = cacheYears.get(marriageDate.getYear());
                //if (curCache != null)
                //{
                    curCache.processChildrenAtMarriage(id, fatherId, motherId, relationId, partnerId, 
                        relationType);
                    curCache.processAgeAtMarriage(birthDate, marriageDate, relationType, sexType);
                //}
            }
            
            // Process avg age at death and number of deaths.
            if (deathDate != null)
            {
                Cache curCache = getOrCreateCache(deathDate.getYear(), cacheYears,
                    cacheSet, yearFrom, yearTo, interval, defaultCache);
                
                //Cache curCache = cacheYears.get(deathDate.getYear());
                //if (curCache != null)
                //{
                    curCache.processAgeAtDeath(birthDate, deathDate, sexType);
                    curCache.processDeath(deathDate);
                //}
            }
            
            // Process births.
            if (birthDate != null)
            {
                Cache curCache = getOrCreateCache(birthDate.getYear(), cacheYears,
                    cacheSet, yearFrom, yearTo, interval, defaultCache);
                
                //Cache curCache = cacheYears.get(birthDate.getYear());
                //if (curCache != null)
                //{
                    curCache.processBirth(birthDate);
                //}
            }
        }

        // Calculate statistics from caches and return them.
        List<Statistics> stats = new ArrayList<>();
        cacheSet.forEach((curcache) -> stats.add(curcache.calculateStatistics()));
        return stats;
    }
    
    private static Cache getOrCreateCache(int year, Map<Integer, Cache> cacheYears, 
            List<Cache> cacheSet, int yearFrom, int yearTo, int interval, Cache defaultCache)
    {
        Cache cache = cacheYears.get(year); // Try get cache from cacheYears.
        
        // If the cache isn't in cacheYears, create and add it to cacheYears.
        if (cache == null)
        {
            // If year is not between yearFrom and yearTo, return default cache.
            if (year < yearFrom || year > yearTo)
            {
                return defaultCache;
            }
            
            int periodStart = yearFrom;
            int periodEnd = yearTo;
            
            if (interval > 0)
            {
                periodStart = year - ((year - yearFrom) % interval);
                periodEnd = Math.min(periodStart + interval - 1, yearTo);
            }
            
            cache = new Cache(new PeriodYears(periodStart, periodEnd));
            cacheYears.put(year, cache);
        }
        
        // If the cache isn't in cacheSet yet, add it.
        if (!cacheSet.contains(cache))
        {
            System.out.println("added! " + cache.Period.YearFrom + " - " + cache.Period.YearTo);
            cacheSet.add(cache);
        }
        
        return cache;   // Return the retrieved/created cache.
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
