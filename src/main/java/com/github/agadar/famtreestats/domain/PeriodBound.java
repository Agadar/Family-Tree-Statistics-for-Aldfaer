package com.github.agadar.famtreestats.domain;

/**
 * Parent class for classes that are identified by a PeriodYears.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public abstract class PeriodBound implements Comparable<PeriodBound>
{
    /** The period between two year dates. */
    public final PeriodYears Period;
    
    public PeriodBound(int yearFrom, int yearTo)
    {
        this.Period = new PeriodYears(yearFrom, yearTo);
    }
    
    public PeriodBound(PeriodYears period)
    {
        this.Period = period;
    }
    
    @Override
    public int compareTo(PeriodBound other)
    {
        return this.Period.compareTo(Period);
    }

    @Override
    public int hashCode()
    {
        return this.Period.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        final PeriodBound other = (PeriodBound) obj;
        return this.Period.equals(other.Period);
    }
}
