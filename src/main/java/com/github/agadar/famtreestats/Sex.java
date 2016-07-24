package com.github.agadar.famtreestats;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerator for the sexes.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public enum Sex 
{
    Male("M"),
    Female("V"),
    Unknown("?");
        
    /** The underlying string. */
    private final String UnderlyingString;
    
    /** Map for reverse look-up via the string. */
    private final static Map<String, Sex> Reverse = new HashMap<>();
    
    /** Static 'constructor' for filling the reverse map. */
    static
    {
        for (Sex sex : values())
        {
            Reverse.put(sex.UnderlyingString, sex);
        }
    }
    
    /**
     * Instantiate a new entry with the given string.
     * 
     * @param underlyingString the string
     */
    private Sex(String underlyingString) 
    {
        this.UnderlyingString = underlyingString;
    }

    /**
     * Returns the string
     * 
     * @return the string
     */
    public String getUnderlyingString() 
    {
        return UnderlyingString;
    }
    
    /**
     * Returns the Sex mapped to the given string. If no type is
     * mapped to the given string, then Sex.Unknown is returned.
     * 
     * @param string the string
     * @return the Sex mapped to the given string
     */
    public static Sex getByUnderlyingString(String string)
    {
        return Reverse.getOrDefault(string, Sex.Unknown);
    }
}
