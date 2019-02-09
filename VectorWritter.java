package finalProject.fp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class VectorWritter extends JCasConsumer_ImplBase {

	List<String> terms;

	public VectorWritter() {
		File termFile = new File("src/main/resources/feature_word/word_index.dat");
        terms = new ArrayList<String>();
        try {
        	BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(termFile)));
        	String line = "";
        	while((line=reader.readLine())!=null) {
        		terms.add(line.split("	")[1]);
        	}
        	reader.close();
        }
    	catch (IOException e)
        {
           e.printStackTrace();
         }
	}


	@Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
		Compound c = JCasUtil.select(jcas, Compound.class).iterator().next();
		int document_size = JCasUtil.select(jcas, Paragraph.class).size();
		int[] df = new int[400];
		double[][] tf = new double[document_size][400];
		int p_index = 0;
		for(Paragraph p : JCasUtil.select(jcas, Paragraph.class)) {
			int term_index = 0;
			for(String term: this.terms) {
				int tf_times = 0;
				for(Lemma l : JCasUtil.selectCovered(Lemma.class, p)) {
					if(l.getCoveredText().toLowerCase().equals(term)) {
						tf_times++;
					}
				}
				if(tf_times>0)
					tf[p_index][term_index] = 1+ Math.log10(tf_times);
				else
					tf[p_index][term_index] = 0;
				if(tf_times>0) {
					df[term_index]++;
				}
				term_index++;
			}
			p_index++;
		}
		File vector_file = new File("src/main/resources/feature_word/vector.dat");
		if(!vector_file.exists()) {
			try {
				vector_file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter out = new FileWriter(vector_file,true);
			double[] vector = new double[400];
			out.write(c.getCoveredText()+"\t");
			for(int j=0;j<tf[0].length; j++) {
				for(int i=0;i<tf.length;i++) {
					vector[j] += tf[i][j];
				}
				if(df[j]>0)
					vector[j] = vector[j]* Math.log10((double)document_size/df[j]);
				else
					vector[j] =0;
				out.write(j+"\t"+String.valueOf(vector[j])+"\t");
			}
			out.write("\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}