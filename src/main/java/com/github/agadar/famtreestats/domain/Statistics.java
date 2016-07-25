package com.github.agadar.famtreestats.domain;

/**
 * Container for the calculated statistics.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public final class Statistics 
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

    /**
     * Instantiates a new Statistics object.
     * 
     * @param AgeAtMarriageBoth average age at marriage, both sexes
     * @param AgeAtMarriageMale average age at marriage, males
     * @param AgeAtMarriageFemale average age at marriage, females
     * @param AgeAtDeathBoth average age at death, both sexes
     * @param AgeAtDeathMale average age at death, males
     * @param AgeAtDeathFemale average age at death, females
     * @param ChildenPerMarriage average number of children per marriage
     */
    public Statistics(int AgeAtMarriageBoth, int AgeAtMarriageMale,
            int AgeAtMarriageFemale, int AgeAtDeathBoth, int AgeAtDeathMale,
            int AgeAtDeathFemale, int ChildenPerMarriage)
    {
        this.AgeAtMarriageBoth = AgeAtMarriageBoth;
        this.AgeAtMarriageMale = AgeAtMarriageMale;
        this.AgeAtMarriageFemale = AgeAtMarriageFemale;
        this.AgeAtDeathBoth = AgeAtDeathBoth;
        this.AgeAtDeathMale = AgeAtDeathMale;
        this.AgeAtDeathFemale = AgeAtDeathFemale;
        this.ChildenPerMarriage = ChildenPerMarriage;
    }

    @Override
    public String toString()
    {
        return "Statistics{" + "AgeAtMarriageBoth=" + AgeAtMarriageBoth +
               ", AgeAtMarriageMale=" + AgeAtMarriageMale +
               ", AgeAtMarriageFemale=" + AgeAtMarriageFemale +
               ", AgeAtDeathBoth=" + AgeAtDeathBoth + ", AgeAtDeathMale=" +
               AgeAtDeathMale + ", AgeAtDeathFemale=" + AgeAtDeathFemale +
               ", ChildenPerMarriage=" + ChildenPerMarriage + '}';
    }
}
