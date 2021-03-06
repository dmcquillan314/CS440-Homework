import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.hw6.util.Pair;
import com.hw6.documentFrequency.InverseDocumentFrequencyService;
import com.hw6.documentFrequency.InverseDocumentFrequencyStatistics;
import com.hw6.termFrequency.GroupedTermFrequencyService;
import com.hw6.termFrequency.TermFrequencyStatistics;

import weka.core.Attribute;
import weka.core.Instances;

// Why do we normalize the raw counts of the terms?

// We normalize in order to get a ratio of the terms to the document or term length.


// 1.3 We normalize the counts of the terms so that we can gain a relative weighting of the
// term to the current document.  

// The advantage of the first equation is that no term should receive a normalized frequency of 1.0.  
// The disadvantage of the first equation is that the term frequency of rare but important words
// are assigned a very low value for their TF.

// The advantage of the second equation is that you can easily identify words with a TF = 1.0
// as a stopword in most cases.
// The disadvantage of the second equation is that there's no way to discern which terms with
// a term frequency of 1.0 should be labeled as a stopword or left alone because the term ended
// up being important in the classification but yet not a common term.

// 1.4 I'm assuming for this question it is referring to the -log(TF) since otherwise it
// would always be negative.  This could be useful since relatively rare terms would 
// have a higher term frequency due to the properties of the log(x) curve.  Also, as
// x approaches 1 log(x) approaches 0, therefore more frequent terms such as articles
// and conjunctions would be mostly ignored.

// 1.5 Stop words are helpful to remove terms that would appear to be of little value 
// in helping with the classification of a document.  They will be helpful to remove
// common terms from the documents so that words such as 'a' and 'the' will not affect
// the ultimate classification of a document

// 1.6 The following is a set of stop words that was generated by my application based
// on the inverse document frequency.

// Stop Word --> i: 0.0776692564631804
// Stop Word --> is: 0.07448622990305932
// Stop Word --> of: 0.03298649899630645
// Stop Word --> a: 0.029942356615078318
// Stop Word --> and: 0.027918064425847915
// Stop Word --> in: 0.02388173200338738
// Stop Word --> to: 0.016857117066422806
// Stop Word --> the: 0.00789737569495094
// Stop Word --> subject: 0.0
// Stop Word --> from: 0.0

// I used this method since I wanted to remove the most common terms from the document.
// I used the inverse document frequency for this since the document frequency of more
// common terms is lower using this method so that I could sort by the inverse document
// frequency and remove the lowest values.

// 2.2 The terms returned from 2.1 better described the group of which they belonged, 
// whereas the terms from 1.1 we more so terms that were common across all the groups

// TODO: finish this problem
// 2.4 As an example, we can use the parameter p to guarantee that the IDF will never equal 0 but will
// only approach it if we set p equal to 0 when a term is present in all the documents.

// 2.5 Limitations of term frequency?
// Term frequency does not marry up the term frequencies to the document set.  Instead it only compares the
// counts in the current document.  Therefore it is limited to the scope of the current document.  It does not
// take terms such as "Statue of Liberty" into considering when calculating term frequency and therefore the term
// as a whole loses it's meaning.

// Is tf-idf metric better than tf?
// On the whole the tf-idf metrix still has most of the same limitations as the tf metric.  We do however gain the ability
// to calculate the term frequency with the scope of the current document set.

// 2.6 Limitations of tf-idf?
// Two examples are that the order in which the terms appear in the document is not taken into consideration,
// it assumes that terms are statistically independent.

public class Main {

	public static void main(String[] args) {
		
		try {
			File file = new File("hw6data/train.arff");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			Instances instances = new Instances(bufferedReader);
			bufferedReader.close();
			
			instances.setClassIndex(instances.numAttributes() - 1);
			InverseDocumentFrequencyService documentFrequencies = new InverseDocumentFrequencyService(instances, 1.0);
			GroupedTermFrequencyService termFrequency = new GroupedTermFrequencyService(instances,documentFrequencies.getStatistics().getInverseDocumentFrequencies());
			
			System.out.println("Document Frequency Eq1:\n-----------------");
			for( Double key : termFrequency.getGroupedTermFrequencyStatistics().keySet() ) {
				TermFrequencyStatistics statistics = termFrequency.getGroupedTermFrequencyStatistics().get(key);
				
				System.out.println("Class id: [" + key + "]");
				
				for( Integer index = statistics.getSortedCalculatedTermFrequencies().size() - 1; index > statistics.getSortedCalculatedTermFrequencies().size() - 6; index-- ) {
					Pair<Integer,Double> pair = statistics.getSortedCalculatedTermFrequencies().get(index);
					
					Attribute attribute = instances.attribute(pair.getLeft());
					
					System.out.println(attribute.name().substring(1) + ": " + pair.getRight());
				}
				
				System.out.println("");
			}
			
			System.out.println("Document Frequency Eq2:\n----------------------");
			for( Double key : termFrequency.getGroupedTermFrequencyStatistics().keySet() ) {
				TermFrequencyStatistics statistics = termFrequency.getGroupedTermFrequencyStatistics().get(key);
				
				System.out.println("Class id: [" + key + "]");
				
				for( Integer index = statistics.getSortedNormalizedCalculatedTermFrequencies().size() - 1; index > statistics.getSortedNormalizedCalculatedTermFrequencies().size() - 6; index-- ) {
					Pair<Integer,Double> normalizedPair = statistics.getSortedNormalizedCalculatedTermFrequencies().get(index);
					
					Attribute normalizedAttribute = instances.attribute(normalizedPair.getLeft());
					
					System.out.println(normalizedAttribute.name().substring(1) + ": " + normalizedPair.getRight());
				}
				
				System.out.println("");
			}
			
			InverseDocumentFrequencyStatistics statistics = documentFrequencies.getStatistics();
			
			Integer i = 0;
			System.out.println("Inverse Document Frequencies");
			List<Pair<Integer,Double>> sortedDocumentFrequencies = statistics.getSortedInverseDocumentFrequencies();
			Collections.reverse(sortedDocumentFrequencies);
			
			for( Pair<Integer,Double> stat : sortedDocumentFrequencies ) {
				
				Attribute attribute = instances.attribute( stat.getLeft() );
				
				if( stat.getRight().compareTo(5.0) < 0 ) {
//					System.out.println("Stop Word --> " + attribute.name().substring(1) + ": " + stat.getRight());
				} else if( i < 10 ) {
					System.out.println(attribute.name().substring(1) + ": " + stat.getRight());
				}
				
				i++;
			}
			System.out.println("");
			
			for( Double key : termFrequency.getGroupedTermFrequencyStatistics().keySet() ) {
				TermFrequencyStatistics termFrequencyStatistics = termFrequency.getGroupedTermFrequencyStatistics().get(key);
				
				System.out.println("Class id: [" + key + "]");
				
				for( Integer index = termFrequencyStatistics.getSortedCalculatedIDFTermFrequencies().size() - 1; index > termFrequencyStatistics.getSortedCalculatedIDFTermFrequencies().size() - 6; index-- ) {
					Pair<Integer,Double> pair = termFrequencyStatistics.getSortedCalculatedIDFTermFrequencies().get(index);
					
					Attribute attribute = instances.attribute(pair.getLeft());
					
					System.out.println(attribute.name().substring(1) + ": " + pair.getRight());
				}
				
				System.out.println("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
