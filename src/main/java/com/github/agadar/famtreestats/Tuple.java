package com.github.agadar.famtreestats;

import java.util.Objects;

/**
 * Custom tuple class, holding two variables. Two tuples are equal if their
 * containing variables are equal, regardless of order.
 * 
 * @author Agadar <https://github.com/Agadar/>
 * 
 * @param <X> the first variable
 * @param <Y> the second variable
 */
public class Tuple<X, Y>
{
    /** First variable. */
    public final X x;
    
    /** Second variable. */
    public final Y y;

    /**
     * Instantiates a new tuple.
     * 
     * @param x first variable
     * @param y second variable
     */
    public Tuple(X x, Y y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode()
    {
        // hash = 5 * 59 = 295
        int hash = 295 + Objects.hashCode(this.x) + Objects.hashCode(this.y);
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
        final Tuple<?, ?> other = (Tuple<?, ?>) obj;
        return (Objects.equals(this.x, other.x) && Objects.equals(this.y, other.y)
                || (Objects.equals(this.x, other.y) && Objects.equals(this.y, other.x)));
    }
}
