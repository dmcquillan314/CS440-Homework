package hw4.weka;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class MultilayerPerceptronClassifier {

	private Instances instances = null;
	
	private Double N = 0.0;
	
	private MultilayerPerceptron percep;
	
	public MultilayerPerceptronClassifier( final Instances instances, final Double N ) {
		this.instances = instances;
		this.N = N;
	}
	
	public Double classify() throws Exception {
		int numInstances = instances.numInstances();

		instances.setClassIndex(instances.numAttributes() - 1);
		
		percep = new MultilayerPerceptron();
		Instances trainingData = new Instances(instances, 0, (int) (numInstances * (N  / 100.0 )) );
		
		percep.buildClassifier(trainingData);
		
		return test(instances, N );

	}
	
	public Double test(Instances testingInstances, Double N ) throws Exception {
		int numInstances = testingInstances.numInstances();
		
		Instances testingData = new Instances(
				instances,
				0,
				(int) (numInstances * (N  / 100.0 ))
			);

		double correct = 0.0;
		double incorrect = 0.0;
		
		for( int i = 0; i < testingData.numInstances(); i++) {
			double assignedClass = percep.classifyInstance(testingData.instance(i));
			double originalClass = testingData.instance(i).classValue();
			
			if( assignedClass == originalClass ) {
				correct++;
			} else {
				incorrect++;
			}
		}
		
		Double accuracy = 100.0 * correct / (correct + incorrect);
		
		return accuracy;
	}
}
