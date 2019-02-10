package finalProject.fp;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class Pipeline {

	public static void main(String[] args) throws UIMAException, IOException {
		CollectionReader reader = createReader(Comments_reader.class,Comments_reader.FILE_ADDRESS,"src/main/resources/data/");

		AnalysisEngine seg = createEngine(StanfordSegmenter.class);
		AnalysisEngine c = createEngine(Comments_split.class);
		AnalysisEngine pos = createEngine(StanfordPosTagger.class);
		AnalysisEngine lemma = createEngine(StanfordLemmatizer.class);

//		AnalysisEngine writer = createEngine(VectorWritter.class);
//		AnalysisEngine writer = createEngine(VectorWritter2.class);
//		AnalysisEngine writer = createEngine(VectorWritter3.class);
//		AnalysisEngine writer = createEngine(VectorWritter4.class);
		AnalysisEngine writer = createEngine(VectorWritter5.class);

		SimplePipeline.runPipeline(reader,seg,pos,c,lemma, writer);
	}
}
