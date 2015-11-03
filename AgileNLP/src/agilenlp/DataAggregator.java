/*
PURPOSE:    DataAggregator is going to load all the data and then merge the data together into a single
            Entity that can be used for searching.
            Thinking about the idea of hashing at this juncture as well, but need to give that some thought.

            The idea behind it is movie will be merged with plot summary, and perhaps a list of character names for the movie, genres, etc.  
            So one object will contain ALL data merged together.
            
            The dataagregator will be responsible for this.
            The entity MovieComposite will be the object that contains all this data together.

            Lastly, the idea is that our main program will call into DataAggregator, return an array of MovieComposite objects.
            DataAggregator will call each loader function from DataLoader, and do it's magic munging together.
*/


package agilenlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;


public class DataAggregator {
    public static Hashtable GetCompositeMovies()
    {
        
        System.out.println("Starting Data Aggregator: " + new Date());

        //get movies
        Hashtable movies = (Hashtable)DataLoader.GetObjectData(DataLoader.DataType.hash_table, DataLoader.ObjectType.movie);
        System.out.println("Loaded Movies Hashtable: " + new Date());

        //get plot summaries
        Hashtable plot_summaries = (Hashtable)DataLoader.GetObjectData(DataLoader.DataType.hash_table, DataLoader.ObjectType.plot_summary);
        System.out.println("Loaded Plot Summaries: " + new Date());

        Hashtable composite_movies = new Hashtable();

        //get character loader
        CharacterLoader cl = DataLoader.GetCharacterLoader();
        System.out.println("Loaded Character Loader: " + new Date());


        Set<String> movie_keys = movies.keySet();        
        Set<String> character_keys;
        
        for(String movie_key: movie_keys)
        {
            MovieComposite mc = new MovieComposite();

            Movie m = (Movie)movies.get(movie_key);
            mc.movie = m;
            

            if(plot_summaries.containsKey(m.wikipedia_movie_id))
            {
                PlotSummary ps = (PlotSummary)plot_summaries.get(m.wikipedia_movie_id);
                if(ps != null)
                {
                    mc.plot_summary = ps;
                }
            }
            
            composite_movies.put(movie_key, mc);
        }
        System.out.println("Completed Composite Movie Movie and Plot Summary Assignment: " + new Date());

        
        //loop over all the characters
        //determine if movie exists in our movie array
        //if so, go ahead and add into composite movies
        //get all the characters for the movie and put into an array list
        //loop over all characters array
        //do we have the movie in movie hashtable
        //get character object out of character hashtable
        //add to movie charachters array
        
        for(Character c: cl.CharactersArrayList)
        {
            MovieComposite mc1 = (MovieComposite)composite_movies.get(c.wikipedia_movie_id);

            if(mc1 != null)
            {
                mc1.characters.add(c);
            }

            //Boolean b = composite_movies.containsValue(c.wikipedia_movie_id);
            //if(b)
            //{
            //    Character c1 = (Character)cl.CharactersHashtable.get(c.freebase_character_id);
            //    cl.CharactersArrayList.add(c1);
           //}
        }
        System.out.println("Completed Composite Movie Character Assignment: " + new Date());
        
        return composite_movies;
    }
}
