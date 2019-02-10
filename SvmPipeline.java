package org.dkpro.tc.ml.libsvm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

		try {
//        	File createTempFile = FileUtil.createTempFile("labelModified", ".tmp");
        	BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("src/main/resources/OutputCompare.dat")));
        	File output = new File("src/main/resources/WingOfVictoryOutput");
        	File test = new File("src/main/resources/WingOfVictoryTest");

        	List<Double> outputs = new ArrayList<>();
            for (String s : FileUtils.readLines(output)) {
                if (s.isEmpty()) {
                    continue;
                }
                outputs.add(Double.parseDouble(s));
            }
            List<Double> tests = new ArrayList<>();
            for (String s : FileUtils.readLines(test)) {
                if (s.isEmpty()) {
                    continue;
                }
                tests.add(Double.parseDouble(s.split("\t")[0]));
            }
            for (int i=0;i<outputs.size();i++) {
            	bw.write(String.valueOf(outputs.get(i)));
            	bw.write("("+String.valueOf(tests.get(i))+")");
            	bw.write("\n");
            }
            bw.close();
        }
    	catch (IOException e)
        {
           e.printStackTrace();
         }





	}

}
