package com.github.agadar.famtreestats.domain;

/**
 * Container for the calculated statistics.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public final class Statistics extends PeriodBound
{
    /** Average age at marriage, both sexes. */
    public final int AgeAtMarriageBoth;
    
    /** Average age at marriage, males. */
    public final int AgeAtMarriageMale;
    
    /** Average age at marriage, females. */
    public final int AgeAtMarriageFemale;
    
    /** Average age at death, both sexes. */
    public final int AgeAtDeathBoth;
    
    /** Average age at death, males. */
    public final int AgeAtDeathMale;
    
    /** Average age at death, females. */
    public final int AgeAtDeathFemale;
    
    /** Average number of children per marriage. */
    public final int ChildenPerMarriage;
    
    /** Number of births. */
    public final int Deaths;

    /** Number of deaths. */
    public final int Births;

    /**
     * Instantiates a new Statistics object.
     * 
     * @param YearFrom lower bound of the time period these stats cover (inclusive)
     * @param YearTo upper bound of the time period these stats cover (inclusive)
     * @param AgeAtMarriageBoth average age at marriage, both sexes
     * @param AgeAtMarriageMale average age at marriage, males
     * @param AgeAtMarriageFemale average age at marriage, females
     * @param AgeAtDeathBoth average age at death, both sexes
     * @param AgeAtDeathMale average age at death, males
     * @param AgeAtDeathFemale average age at death, females
     * @param ChildenPerMarriage average number of children per marriage
     * @param Deaths number of deaths
     * @param Births number of births
     */
    public Statistics(int YearFrom, int YearTo, int AgeAtMarriageBoth, int AgeAtMarriageMale,
            int AgeAtMarriageFemale, int AgeAtDeathBoth, int AgeAtDeathMale,
            int AgeAtDeathFemale, int ChildenPerMarriage, int Deaths, int Births)
    {
        this(new PeriodYears(YearFrom, YearTo), AgeAtMarriageBoth, AgeAtMarriageMale,
            AgeAtMarriageFemale, AgeAtDeathBoth, AgeAtDeathMale, AgeAtDeathFemale,
            ChildenPerMarriage, Deaths, Births);
    }
    
    /**
     * Instantiates a new Statistics object.
     * 
     * @param period the period this covers
     * @param AgeAtMarriageBoth average age at marriage, both sexes
     * @param AgeAtMarriageMale average age at marriage, males
     * @param AgeAtMarriageFemale average age at marriage, females
     * @param AgeAtDeathBoth average age at death, both sexes
     * @param AgeAtDeathMale average age at death, males
     * @param AgeAtDeathFemale average age at death, females
     * @param ChildenPerMarriage average number of children per marriage
     * @param Deaths number of deaths
     * @param Births number of births
     */
    public Statistics(PeriodYears period, int AgeAtMarriageBoth, int AgeAtMarriageMale,
            int AgeAtMarriageFemale, int AgeAtDeathBoth, int AgeAtDeathMale,
            int AgeAtDeathFemale, int ChildenPerMarriage, int Deaths, int Births)
    {
        super(period);
        this.AgeAtMarriageBoth = AgeAtMarriageBoth;
        this.AgeAtMarriageMale = AgeAtMarriageMale;
        this.AgeAtMarriageFemale = AgeAtMarriageFemale;
        this.AgeAtDeathBoth = AgeAtDeathBoth;
        this.AgeAtDeathMale = AgeAtDeathMale;
        this.AgeAtDeathFemale = AgeAtDeathFemale;
        this.ChildenPerMarriage = ChildenPerMarriage;
        this.Deaths = Deaths;
        this.Births = Births;
    }
}
