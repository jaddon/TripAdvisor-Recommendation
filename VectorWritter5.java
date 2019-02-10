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

public class VectorWritter5 extends JCasConsumer_ImplBase {

	List<List<String>> terms;

	public VectorWritter5() {
		File termFile = new File("src/main/resources/feature_word/compress words with similar meaning.dat");
        terms = new ArrayList<>();
        try {
        	BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(termFile)));
        	String line = "";
        	while((line=reader.readLine())!=null) {
        		List<String> list = new ArrayList<>();
        		for(String s : line.split(" ")) {
        			list.add(s);
        		}
        		terms.add(list);
        		System.out.println(line);
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
		int[] df = new int[6];
		double[][] tf = new double[document_size][6];
		double[] posRatio = new double[document_size];
		int p_index = 0;
		double positive = 0;
		double negative = 0;
		for(Paragraph p : JCasUtil.select(jcas, Paragraph.class)) {
			int term_index = 0;
			for(List<String> term: this.terms) {
				int tf_times = 0;
				String lastWord = null;
				for(Lemma l : JCasUtil.selectCovered(Lemma.class, p)) {
					for(String s : term) {
						if((l.getCoveredText().toLowerCase()).contains(s)){
							if(lastWord!=null&&(lastWord.equals("not")||lastWord.endsWith("n't")||lastWord.equals("only")||lastWord.equals("no")||lastWord.equals("without")||lastWord.equals("dont"))) {
								if(tf_times>0) {
									tf_times--;
									lastWord = l.getCoveredText().toLowerCase();
									break;
								}
							}
							tf_times++;
							if(term_index==0||term_index==1||term_index==2||term_index==3) negative++;
							else if(term_index==4||term_index==5) positive++;
							lastWord = l.getCoveredText().toLowerCase();
							break;
						}
					}
					lastWord = l.getCoveredText().toLowerCase();
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
			if(positive>0||negative>0) posRatio[p_index]=positive/(positive+negative);
			else posRatio[p_index] = 0.5;
//			System.out.println("posRatio= "+posRatio[p_index]);
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
			double[] vector = new double[7];
			out.write(c.getCoveredText()+"\t");
			double posRatioAvg = 0.0;
			for(int j=0;j<tf[0].length; j++) {
				for(int i=0;i<tf.length;i++) {
					vector[j] += tf[i][j];
				}
				if(df[j]>0)
					vector[j] = vector[j]* Math.log10((double)document_size/df[j]);
				else
					vector[j] =0;
				out.write(j+"\t"+String.valueOf(vector[j]*100/document_size)+"\t");
			}
			for(int i=0;i<document_size;i++) {
				posRatioAvg += posRatio[i];
			}
			posRatioAvg = posRatioAvg/document_size;
			vector[6] = posRatioAvg;
			System.out.println("posRatioAvg= "+posRatioAvg);
			out.write(6+"\t"+String.valueOf(vector[6]*10)+"\t");
			out.write("\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}