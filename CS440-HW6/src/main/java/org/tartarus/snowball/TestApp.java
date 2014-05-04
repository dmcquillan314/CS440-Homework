
package org.tartarus.snowball;

import org.tartarus.snowball.ext.EnglishStemmer;

public class TestApp {
	
    public static void main(String [] args) throws Throwable {
    	
    	SnowballStemmer stemmer = new EnglishStemmer();

		stemmer.setCurrent("an");
		stemmer.stem();

	    System.out.println("testing -> " + stemmer.getCurrent() );
    }
}
