package com.github.agadar.famtreestats.domain;

/**
 * Represents a period between two year dates (inclusive).
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public class PeriodYears implements Comparable<PeriodYears>
{
    /** Lower bound of the period. */
    public final int YearFrom;
    
    /** Upper bound of the period. */
    public final int YearTo;
    
    public PeriodYears(int yearFrom, int yearTo)
    {
        this.YearFrom = yearFrom;
        this.YearTo = yearTo;
    }

    @Override
    public int compareTo(PeriodYears other)
    {
        if (this.YearFrom > other.YearFrom)
        {
            return 1;
        }
        else if (this.YearFrom < other.YearFrom)
        {
            return -1;
        }
        else
        {
            return Integer.compare(this.YearTo, other.YearTo);
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + this.YearFrom;
        hash = 37 * hash + this.YearTo;
        return hash;
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
        
        final PeriodYears other = (PeriodYears) obj;
        
        if (this.YearFrom != other.YearFrom)
        {
            return false;
        }
        
        return this.YearTo == other.YearTo;
    } 
}
