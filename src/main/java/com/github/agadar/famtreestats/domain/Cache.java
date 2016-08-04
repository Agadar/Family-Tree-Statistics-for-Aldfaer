package com.github.agadar.famtreestats.domain;

import com.github.agadar.famtreestats.MarriedWithChildren;
import com.github.agadar.famtreestats.enums.RelationType;
import com.github.agadar.famtreestats.enums.Sex;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Cache holding variables for the family tree statistics calculations.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public class Cache extends PeriodBound
{
    /** This calculator's MarriedWithChildren helper. */
    private final MarriedWithChildren Mwc = new MarriedWithChildren();
    
    // Values for calculating the averages           
    public long ageAtMarriageBothTotal = 0;
    public int ageAtMarriageBothDivBy = 0;
    public long ageAtMarriageMaleTotal = 0;
    public int ageAtMarriageMaleDivBy = 0;
    public long ageAtMarriageFemaleTotal = 0;
    public int ageAtMarriageFemaleDivBy = 0;      
    public long ageAtDeathBothTotal = 0;
    public int ageAtDeathBothDivBy = 0;
    public long ageAtDeathMaleTotal = 0;
    public int ageAtDeathMaleDivBy = 0;
    public long ageAtDeathFemaleTotal = 0;
    public int ageAtDeathFemaleDivBy = 0;
    public int deaths = 0;
    public int births = 0;
    
    public Cache(int yearFrom, int yearTo)
    {
        super(yearFrom, yearTo);
    }
    
    public Cache(PeriodYears period)
    {
        super(period);
    }
    
    /**
     * Processes a person's data, using it to calculate the average ages at marriage.
     * 
     * @param birthDate person's birth date
     * @param marriageDate person's marriage date
     * @param relationType person's relationship type
     * @param sexType person's sex type
     */
    public void processAgeAtMarriage(LocalDate birthDate, LocalDate marriageDate, 
            RelationType relationType, Sex sexType)
    {
        if (birthDate == null || marriageDate == null || 
                !(relationType == RelationType.Marriage || 
                    relationType == RelationType.RegisteredPartnership))
        {
            return;
        }
        
        ageAtMarriageBothDivBy++;
        final long daysBetween = DAYS.between(birthDate, marriageDate);
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
    
    /**
     * Processes a person's data, using it to calculate the average ages at death.
     * 
     * @param birthDate person's birth date
     * @param deathDate person's death date
     * @param sexType person's sex type
     */
    public void processAgeAtDeath(LocalDate birthDate, LocalDate deathDate, Sex sexType)
    {
        if (birthDate == null || deathDate == null)
        {
            return;
        }
            
        ageAtDeathBothDivBy++;
        final long daysBetween = DAYS.between(birthDate, deathDate);
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

    /**
     * Processes a death date.
     * 
     * @param deathDate the death date to process
     */
    public void processDeath(LocalDate deathDate)
    {
        if (deathDate != null)
        {
            deaths++;
        }
    }
    
    /**
     * Processes a birth date.
     * 
     * @param birthDate the birth date to process
     */
    public void processBirth(LocalDate birthDate)
    {
        if (birthDate != null)
        {
            births++;
        }
    }
    
    /**
     * Processes a person's data, using it to calculate the average number of
     * children per marriage.
     * 
     * @param id person's id
     * @param fatherId person's father's id
     * @param motherId person's mother's id
     * @param relationId person's relation id
     * @param partnerId person's partner's id
     * @param relationType type of the relation
     */
    public void processChildrenAtMarriage(int id, int fatherId, int motherId,
            int relationId, int partnerId, RelationType relationType)
    {
        // Register child if both parent id's are known.
        if (fatherId != -1 && motherId != -1 && id != -1)
        {
            Mwc.registerChild(id, fatherId, motherId);
        }

        // Register couple if relationship id and partner id are known.
        if (relationId != -1 && partnerId != -1 && id != -1 && 
            (relationType == RelationType.Marriage ||
             relationType == RelationType.RegisteredPartnership))
        {
            Mwc.registerCouple(relationId, id, partnerId);
        }
    }
    
    /**
     * Produces statistics based on this cache's values.
     * 
     * @return 
     */
    public Statistics calculateStatistics()
    {
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
        final int avgChildrenPerMarriage = Mwc.averageNumberOfChildren();
        return new Statistics(this.Period, ageAtMarriageBothResult, ageAtMarriageMaleResult,
                ageAtMarriageFemaleResult, ageAtDeathBothResult, ageAtDeathMaleResult,
                ageAtDeathFemaleResult, avgChildrenPerMarriage, deaths, births);
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
}
