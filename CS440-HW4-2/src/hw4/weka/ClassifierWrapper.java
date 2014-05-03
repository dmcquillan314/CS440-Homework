package hw4.weka;

public class ClassifierWrapper {
	
	private Double trainingAccuracy;
	private Double testingAccuracy;
	private final MultilayerPerceptronClassifier classifier;
	
	public ClassifierWrapper( final Double trainingAccuracy, final Double testingAccuracy, final MultilayerPerceptronClassifier classifier ) {
		this.trainingAccuracy = trainingAccuracy;
		this.testingAccuracy = testingAccuracy;
		this.classifier = classifier;
	}

	public Double getTrainingAccuracy() {
		return trainingAccuracy;
	}

	public Double getTestingAccuracy() {
		return testingAccuracy;
	}

	public MultilayerPerceptronClassifier getClassifier() {
		return classifier;
	}
	
	public void setTrainingAccuracy(Double trainingAccuracy) {
		this.trainingAccuracy = trainingAccuracy;
	}

	public void setTestingAccuracy(Double testingAccuracy) {
		this.testingAccuracy = testingAccuracy;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Training Accuracy: " + trainingAccuracy );
		stringBuilder.append("\n");
		stringBuilder.append("Testing Accuracy: " + testingAccuracy );
		stringBuilder.append("\n");
		
		return stringBuilder.toString();
	}

}
