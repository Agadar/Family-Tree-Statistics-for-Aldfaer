package com.github.agadar.famtreestats.enums;

/**
 * Enumerator for mapping column names from the persons CSV files.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public enum Column 
{
    DateMarriage("datum"),//TR_sortdatum"),
    DateBirth("Geboorte"),
    DateDeath("Overlijden"),
    TypeRelationship("huw.type"),
    TypeSex("Geslacht"),
    IdSelf("Intern_nummer"),
    IdFather("ID_vader"),
    IdMother("ID_moeder"),
    IdRelationship("RelatieID"),
    IdPartner("PartnerID");
    
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
