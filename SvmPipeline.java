package org.dkpro.tc.ml.libsvm;

import java.io.IOException;

import org.dkpro.tc.ml.libsvm.api.LibsvmPredict;
import org.dkpro.tc.ml.libsvm.api.LibsvmTrainModel;

public class SvmPipeline {

	public static void main(String[] args) throws IOException {

		LibsvmTrainModel trainModel = new LibsvmTrainModel();

		String[] argv = {"-s","0","-t","2","-d","3","-g","0","-r","0","-n","0.5","-m","100","-c","1","-e","1e-3","-p","0.1","-h","1","-b","0","src/main/resources/WingOfVictory"};
		try {
			trainModel.run(argv);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LibsvmPredict predict =  new LibsvmPredict();
		String[] arg = {"-b","0","src/main/resources/WingOfVictoryTest","WingOfVictory.model","src/main/resources/WingOfVictoryOutput"};
		try {
			predict.main(arg);
		} catch (IOException e) {
			e.printStackTrace();
		}




	}

}
