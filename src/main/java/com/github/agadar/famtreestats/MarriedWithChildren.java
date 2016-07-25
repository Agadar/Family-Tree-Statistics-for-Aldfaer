package com.github.agadar.famtreestats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a married couple with children.
 * 
 * @author Agadar <https://github.com/Agadar/>
 */
public final class MarriedWithChildren 
{
    private final static Map<Integer, Tuple<Integer, Integer>> Couples = new HashMap<>();
    private final static Map<Tuple<Integer, Integer>, List<Integer>> ParentsWithChildren = new HashMap<>();
    
    public static void registerChild(int childId, int parent1Id, int parent2Id)
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
    
    public static void registerCouple(int relationId, int partner1Id, int partner2Id)
    {
        if (!Couples.containsKey(relationId))
        {
            Couples.put(relationId, new Tuple(partner1Id, partner2Id));
        }
    }
    
    public static float averageNumberOfChildren()
    {
        int totalChildren = 0;
        
        for (Map.Entry<Integer, Tuple<Integer, Integer>> coupleEntry : Couples.entrySet())
        {
            final Tuple<Integer, Integer> couple = coupleEntry.getValue();
            final List<Integer> children = ParentsWithChildren.getOrDefault(couple, new ArrayList<>());
            totalChildren += children.size();
            if (children.size() > 0)
                System.out.println(children.size());
        }
        
        System.out.println(ParentsWithChildren.size());
        System.out.println(Couples.size());
        return (float) totalChildren / (float) Couples.size();
    }
    
    public static void clear()
    {
        Couples.clear();
        ParentsWithChildren.clear();
    }
}
