package com.github.agadar.famtreestats;

/**
 * Enumerator for mapping column names from the persons CSV files.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public enum Column 
{
    DateMarriage("TR_sortdatum"),
    DateBirth("Geboorte"),
    TypeRelationship("huw.type"),
    DateDeath("Overlijden"),
    IdSelf("Intern_nummer"),
    IdFather("ID_vader"),
    IdMother("ID_moeder");
    
    /** The column name. */
    private final String ColumnName;
    
    /**
     * Constructor.
     * 
     * @param columnName the column name
     */
    private Column(String columnName)
    {
        this.ColumnName = columnName;
    }

    /**
     * Returns the corresponding column name.
     * 
     * @return the column name
     */
    public String getColumnName()
    {
        return ColumnName;
    }
}
