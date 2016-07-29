package com.github.agadar.famtreestats;

import com.github.agadar.famtreestats.domain.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper for calculating the number of children per marriage.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public final class MarriedWithChildren 
{
    /** Partners id's mapped to relation id's. */
    private final Map<Integer, Tuple<Integer, Integer>> Couples = new HashMap<>();
    
    /** Children id's mapped to parents' id's. */
    private final Map<Tuple<Integer, Integer>, List<Integer>> ParentsWithChildren = new HashMap<>();
    
    /**
     * Registers a child id to two parent id's, but only if the child id isn't
     * already registered to the parents id's.
     * 
     * @param childId id of the child
     * @param parent1Id id of one of the parents
     * @param parent2Id id of the other parent
     */
    public void registerChild(int childId, int parent1Id, int parent2Id)
    {
        final Tuple<Integer, Integer> parents = new Tuple(parent1Id, parent2Id);
        List<Integer> children = ParentsWithChildren.get(parents);
        
        if (children == null)
        {
            children = new ArrayList<>();
            children.add(childId);
            ParentsWithChildren.put(parents, children);
        }
        else
        {
            if (!children.contains(childId))
            {
                children.add(childId);
            }
        }
    }
    
    /**
     * Registers a married couple, but only if the relation id isn't already registered.
     * 
     * @param relationId id of the relationship
     * @param partner1Id id of one of the partners
     * @param partner2Id id of the other partner
     */
    public void registerCouple(int relationId, int partner1Id, int partner2Id)
    {
        if (!Couples.containsKey(relationId))
        {
            Couples.put(relationId, new Tuple(partner1Id, partner2Id));
        }
    }
    
    /**
     * Gives the average number of children per marriage.
     * 
     * @return the average number of children per marriage
     */
    public int averageNumberOfChildren()
    {
        int totalChildren = 0;
        
        for (Map.Entry<Integer, Tuple<Integer, Integer>> coupleEntry : Couples.entrySet())
        {
            final Tuple<Integer, Integer> couple = coupleEntry.getValue();
            final List<Integer> children = ParentsWithChildren.getOrDefault(couple, new ArrayList<>());
            totalChildren += children.size();
        }
        
        return Math.round((float) totalChildren / (float) Couples.size());
    }
}
