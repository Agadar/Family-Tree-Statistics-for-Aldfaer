package com.github.agadar.famtreestats.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerator for the marriage type.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public enum Relationship 
{
    PreMarriage("Ondertrouw"),
    Marriage("Huwelijk"),
    RegisteredPartnership("Notarieel"),
    LivingTogether("Samenleven"),
    Relationship("Relatie"),
    UnknownRelationship("Onbekende relatievorm"),
    Single("Geen relatie"),
    Unknown("Onbekend");
        
    /** The underlying string. */
    private final String UnderlyingString;
    
    /** Map for reverse look-up via the string. */
    private final static Map<String, Relationship> Reverse = new HashMap<>();
    
    /** Static 'constructor' for filling the reverse map. */
    static
    {
        for (Relationship relationshipType : values())
        {
            Reverse.put(relationshipType.UnderlyingString, relationshipType);
        }
    }
    
    /**
     * Instantiate a new entry with the given string.
     * 
     * @param underlyingString the string
     */
    private Relationship(String underlyingString) 
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
     * Returns the RelationshipType mapped to the given string. If no type is
     * mapped to the given string, then RelationshipType.Unknown is returned.
     * 
     * @param string the string
     * @return the RelationshipType mapped to the given string
     */
    public static Relationship getByUnderlyingString(String string)
    {
        return Reverse.getOrDefault(string, Relationship.Unknown);
    }
}
