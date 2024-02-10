package cujae.map.j2g.dgraphm.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HopcroftKarp 
{
    public static class Result
    {
        public boolean         perfect_matching;
        public Map<Integer, Integer>  matching   = new HashMap<Integer, Integer>();
        public Map<Integer, Integer>  unmatched  = new HashMap<Integer, Integer>();
        
        // Strangely enough, the SparseIntArray.clone() method was not supported on my Motorola Milestone (Android API 7)
//        private static Map<Integer, Integer> clone(Map<Integer, Integer> arr)
//        {
//        	Map<Integer, Integer> out = new HashMap<Integer, Integer>();
//            for(int idx = 0; idx < arr.size(); idx++)
//            {
//                out.put(arr.keyAt(idx), arr.valueAt(idx));
//            }
//            return out;
//        }
        
        public Result clone()
        {
            Result copy = new Result();
            
            copy.perfect_matching = perfect_matching;
            copy.matching         = matching;
            copy.unmatched        = unmatched;
            
            return copy;
        }
    };
    
    // Utility function used to manipulate a hash map of type HashMap<Integer, ArrayList<Integer>>
    // This function will return the ArrayList matching the input key, initializing that array in the
    // case the key was not present in the associative array.
    private static ArrayList<Integer> getValueOrDefault(HashMap<Integer, ArrayList<Integer>> map, Integer key)
    {
        ArrayList<Integer> val = map.get(key);
        if(val == null)
        {
            // Key is not present in the map, create it with the empty list as the default value
            map.put(key, new ArrayList<Integer>());
        }
        
        return map.get(key);
    }
    
    //
    // The Hopcroft-Karp algorithm
    //
    public  static Result findMaximumMatching(HashMap<Integer, ArrayList<Integer>> graph, 
                                              ArrayList<Integer>                   in_vertices_v, 
                                              boolean                              randomize)
    {
        // Local variables:
        // The first step of the Hopcroft-Karp algorithm consists in building a list alternating
        // U-layers and V-layers. The current U/V-layer being processed by the algorithm is stored in
        // hash maps current_layer_u and current_layer_v. All U-layers (respectively V-layers) shall 
        // be disjoint from each other. Yet there is no need to store all the layers as they are built,
        // so the algorithm only keeps track of the union of the previous U-layers and V-layers in hash
        // maps all_layers_u and all_layers_v.
        // Finally, hash map matched_v contains the temporary matching built by the algorithm. Upon
        // completion of the algorithm, it is a maximum matching.
        HashMap<Integer, Integer>            current_layer_u     = new HashMap<Integer, Integer>();                 // u --> v
        HashMap<Integer, ArrayList<Integer>> current_layer_v     = new HashMap<Integer, ArrayList<Integer>>();      // v --> list of u
        HashMap<Integer, Integer>            all_layers_u        = new HashMap<Integer, Integer>();                 // u --> v    
        HashMap<Integer, ArrayList<Integer>> all_layers_v        = new HashMap<Integer, ArrayList<Integer>>();      // v --> list of u
        HashMap<Integer, Integer>            matched_v           = new HashMap<Integer, Integer>();                 // v --> u
        ArrayList<Integer>                   unmatched_v         = new ArrayList<Integer>();                        // list of v
        
        //Log.d("HopcroftKarp.Algo", "graph: " +          graph.toString());
        //Log.d("HopcroftKarp.Algo", "in_vertices_v: " +  in_vertices_v.toString());
        
        // Loop as long as we can find at least one minimal augmenting path
        while(true)
        {
            int k = 0;  // U-layers have indexes n = 2*k ; V-layers have indexes n = 2*k+1.
            
            //Log.d("HopcroftKarp.Algo", "matched_v: " +  matched_v.toString());
            
            // The initial layer of vertices of U is equal to the set of u not in the current matching
            all_layers_u.clear();
            current_layer_u.clear();
            for(Integer u : graph.keySet())
            {
                if(!matched_v.containsValue(u))
                {
                    current_layer_u.put(u, 0);
                    all_layers_u.put(u, 0);
                }
            }

            all_layers_v.clear();
            unmatched_v.clear();

            // Use BFS to build alternating U and V layers, in which:
            //  - The edges between U-layer 2*k   and V-layer 2*k+1 are unmatched ones.
            //  - The edges between V-layer 2*k+1 and U-layer 2*k+2 are matched ones.
            
            // While the current layer U is not empty and no unmatched V is encountered
            while(!current_layer_u.isEmpty() && unmatched_v.isEmpty())
            {
                //Log.d("HopcroftKarp.Algo", "current_layer_u: " + current_layer_u.toString());
                
                // Build the layer of vertices of V with index n = 2*k+1                
                current_layer_v.clear();
                for(Integer u : current_layer_u.keySet())
                {
                    for(Integer v : graph.get(u))
                    {
                        if(!all_layers_v.containsKey(v))     // If not already in the previous partitions for V
                        {
                            getValueOrDefault(current_layer_v, v).add(u);
                            // Expand of all_layers_v is done in the next step, building the U-layer
                        }
                    }
                }
                
                //Log.d("HopcroftKarp.Algo", "current_layer_v: " + current_layer_v.toString());
                
                k++;
                // Build the layer of vertices of U with index n = 2*k
                current_layer_u.clear();
                for(Integer v : current_layer_v.keySet())
                {
                    all_layers_v.put(v, current_layer_v.get(v));  // Expand the union of all V-layers to include current_v_layer
                    
                    // Is it a matched vertex in V?
                    if(matched_v.containsKey(v))
                    {
                        Integer u = matched_v.get(v);
                        current_layer_u.put(u, v);
                        all_layers_u.put(u, v);                   // Expand the union of all U-layers to include current_u_layer
                    }
                    else
                    {
                        // Found one unmatched vertex v. The algorithm will finish the current layer,
                        // then exit the while loop since it has found at least one augmenting path.
                        unmatched_v.add(v);
                    }
                }
            }
            
            // After the inner while loop has completed, either we found at least one augmenting path...
            if(!unmatched_v.isEmpty())
            {
                if(randomize)
                {
                    Collections.shuffle(unmatched_v);       // Important to randomize the list here
                                                            // especially in the case where |V| > |U|
                }
                for(Integer v : unmatched_v)
                {
                    // Use DFS to find one augmenting path ending with vertex V. The vertices from that path, if it 
                    // exists, are removed from the all_layers_u and all_layers_v maps.
                    if(k >= 1)
                    { 
                        recFindAugmentingPath(v, all_layers_u, all_layers_v, matched_v, randomize, (k-1));       // Ignore return status
                    }
                    else
                    {
                        throw new ArithmeticException("k should not be equal to zero here.");
                    }                   
                }
            }            
            // ... or we didn't, in which case we already got a maximum matching for that graph
            else
            {
                 break;
            }
        } // end while(true)
        
        
        // Create output class
        Result result = new Result();
        
        result.perfect_matching = (graph.size() == in_vertices_v.size() && graph.size() == matched_v.size());
        result.matching         = get_reverse_mapping(matched_v);
        result.unmatched        = build_unmatched_set(graph, matched_v, in_vertices_v, randomize);       
        
        return result;       
    }
    
    // Recursive function used to build an augmenting path starting from the end node v. 
    // It relies on a DFS on the U and V layers built during the first phase of the algorithm.
    // This is by the way this function which is responsible for most of the randomization
    // of the output.
    // Returns true if an augmenting path is found.
    private static boolean recFindAugmentingPath(Integer v, 
                                                 HashMap<Integer, Integer>            all_layers_u, 
                                                 HashMap<Integer, ArrayList<Integer>> all_layers_v,
                                                 HashMap<Integer, Integer>            matched_v, 
                                                 boolean randomize,
                                                 int k)
    {
        if(all_layers_v.containsKey(v))
        {
            ArrayList<Integer> list_u = all_layers_v.get(v);
            
            // If random output is requested
            if(randomize)
            {
                Collections.shuffle(list_u);
            }
            
            for(Integer u: list_u)
            {
                if(all_layers_u.containsKey(u))
                {
                    Integer prev_v = all_layers_u.get(u);
                    
                    // If the path ending with "prev_v -> u -> v" is an augmenting path
                    if(k == 0 || recFindAugmentingPath(prev_v, all_layers_u, all_layers_v, matched_v, randomize, (k-1)))
                    {
                        matched_v.put(v, u);                        // Edge u -> v replaces the previous matched edge connected to v.
                        all_layers_v.remove(v);                     // Remove vertex v from all_layers_v
                        all_layers_u.remove(u);                     // Remove vertex u from all_layers_u
                        return true;
                    }
                }
            }
        }
        
        return false;   // No augmenting path found
    }
    
    // Given an input associative array that stores (key, value) pairs, and assuming that all values are unique,
    // the following function return the reverse mapping: i.e. the map of (value, key) pairs.
//    public static SparseIntArray get_reverse_mapping(HashMap<Integer, Integer> input_map)
//    {
//        SparseIntArray reversed_map = new SparseIntArray(); 
//        
//        for(Integer v: input_map.keySet())
//        {
//            Integer u = input_map.get(v);
//            reversed_map.put(u, v);
//        }
//        
//        return reversed_map;
//    }
    
    public static Map<Integer, Integer> get_reverse_mapping(HashMap<Integer, Integer> input_map)
    {
    	Map<Integer, Integer> reversed_map = new HashMap<Integer, Integer>(); 
        
        for(Integer v: input_map.keySet())
        {
            Integer u = input_map.get(v);
            reversed_map.put(u, v);
        }
        
        return reversed_map;
    }
    
    // Associates all unmatched vertices of U with remaining vertices of from V. Shuffle the result if required
    private static Map<Integer, Integer> build_unmatched_set(HashMap<Integer, ArrayList<Integer>> graph,
                                                      HashMap<Integer, Integer>            matched_v,
                                                      ArrayList<Integer>                   in_vertices_v,
                                                      boolean                              randomize)
    {
        ArrayList<Integer> remaining_v  = new ArrayList<Integer>();
        Map<Integer, Integer>     unmatched    = new HashMap<Integer, Integer>();     
        
        for(Integer v : in_vertices_v)
        {
            if(!matched_v.containsKey(v))
            {
                remaining_v.add(v);
            }
        }
        
        // Randomize if requested
        if(randomize)
        {
            Collections.shuffle(remaining_v);
        }
        
        // Associates the unmatched vertices from U with the remaining ones from V until one of those two sets is exhausted
        for(Integer u: graph.keySet())
        {
            if(!matched_v.containsValue(u))      // If u is not a matched vertex
            {
                if(!remaining_v.isEmpty())
                {
                    unmatched.put(u, remaining_v.get(0));
                    remaining_v.remove(0);
                }
                else
                {
                    break;
                }
            }
        }
        
        return unmatched;
    }
    
    
}
