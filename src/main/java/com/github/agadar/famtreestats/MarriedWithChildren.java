package com.github.agadar.famtreestats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public final class MarriedWithChildren 
{
    private final static Map<Integer, MarriedWithChildren> map = new HashMap<>();
    
    public static void register(int relationshipId, int partnerId)
    {
        MarriedWithChildren mwc = map.get(relationshipId);
        
        if (mwc == null)
        {
            map.put(relationshipId, new MarriedWithChildren(relationshipId, partnerId));
        }
        else
        {
            mwc.setPartner2Id(partnerId);
        }
    }
    
    public final int RelationshipId;
    public final int Partner1Id;
    private int Partner2Id = -1;

    private MarriedWithChildren(int relationshipId, int partner1Id)
    {
        this.RelationshipId = relationshipId;
        this.Partner1Id = partner1Id;
    }

    private void setPartner2Id(int partner2Id)
    {
        if (Partner1Id != partner2Id && Partner2Id == -1)
        {
            Partner2Id = partner2Id;
        }
    }

    public int getPartner2Id()
    {
        return Partner2Id;
    }
    
    public static List<MarriedWithChildren> allAsList()
    {
        return new ArrayList(map.values());
    }
    
    
}
