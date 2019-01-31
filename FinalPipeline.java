package finalProject.fp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class FinalPipeline {
	public static void main(String[] args) {

		String pathOriginal = "src/main/resources/original/";
		String pathModified = "src/main/resources/modified/";
		String pathTerm = "src/main/resources/term/";

    	File file = new File(pathOriginal);
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
                modify(pathOriginal+filelist[i],filelist[i]);
        }

        File termFile = new File(pathTerm+"word_index");
        List<String> terms = new ArrayList<>();
        try {
        	BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(termFile)));
        	String line = "";
        	while((line=reader.readLine())!=null) {
        		terms.add(line.split("	")[1]);
        	}
        }
    	catch (IOException e)
        {
           e.printStackTrace();
         }

        List<List<List<String>>> textss = new ArrayList<>();

        for (int i = 0; i < filelist.length; i++) {
    		try {
        		File fileModified = new File(pathModified);
                String[] filelistModified = fileModified.list();

        		BufferedReader aReader=new BufferedReader(new InputStreamReader(new FileInputStream(pathModified+filelistModified[i])));
        		String line = "";
        		line = aReader.readLine();

        		List<List<String>> texts = new ArrayList<>();

        		while(line.length()>0) {
        			line = aReader.readLine();
        		}

    			List<String> paragraph = new ArrayList<>();
        		while((line = aReader.readLine())!=null) {
        			if(line.length()!=0) {
        				String[] s = line.split(" ");
        				for(int t = 0;t<s.length;t++) {
        	    			paragraph.add(s[t]);
        	    		}
            			texts.add(paragraph);
            			paragraph = new ArrayList<>();
            			paragraph.clear();
        			}
        			line = aReader.readLine();
        		}
        		textss.add(texts);
        		texts = new ArrayList<>();
    		}

        		catch (IOException e)
                {
                   e.printStackTrace();
                 }

    		}

        List<List<List<Double>>> tfIdfForAll = new ArrayList<>();
        for(List<List<String>> texts: textss) {
        	List<List<Double>> tfIdfForFile = new ArrayList<>();
        	for(String term: terms) {
        		List<Double> tfIdfForTerm = tfIdf(texts, term);
        		System.out.println(tfIdfForTerm);
        		tfIdfForFile.add(tfIdfForTerm);
        	}
        	tfIdfForAll.add(tfIdfForFile);
        	tfIdfForFile = new ArrayList<>();
        }
    }

	public static List<Double> tfIdf(List<List<String>> texts, String term)
	{
		double wordAppear = 0;
		double docAppear = 0;
		double wordSum = 0;
		List<Double> list = new ArrayList<>();

		for(int i=0;i<texts.size();i++) {
			for(String word: texts.get(i)) {
				if(word.equalsIgnoreCase(term)) {
					docAppear++;
					break;
				}
			}
		}

		for(int i = 0;i<texts.size();i++) {
			for(String word: texts.get(i)) {
				if(word.equalsIgnoreCase(term)) {
					wordAppear++;
				}
				wordSum++;
			}
			if(wordAppear>0) {
				list.add(1+Math.log10(wordAppear));
			}
			else {
				list.add(0.0);
			}
			wordAppear = 0;
			wordSum = 0;
		}

		for(int i = 0; i<list.size();i++) {
			list.set(i, list.get(i)*Math.log10(texts.size()/docAppear));
		}

		return list;



//		System.out.println(wordAppear+" "+wordSum+" "+docAppear+" "+texts.size());
//		if(wordAppear>0) {
//			return Math.log10(texts.size()/docAppear)*(1+Math.log10(wordAppear/wordSum));
//		}
//		return 0;
	}




	public static void modify(String path, String fileName)
    {

		try {
    		String overallRating = "";
    		String location = "";
    		String hotelName = "";
    		boolean noURL = false;

    		BufferedReader aReader=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
    		List<String[]> words = new ArrayList<String[]>();
    		String line = "";
    		line = aReader.readLine();
    		overallRating = line.split(">")[1];
    		while((line=aReader.readLine()).length()<6|!(line.contains("<URL>"))) {
    			if(line.contains("<Author>")) {
    				noURL = true;
    				break;
    			}
    		}
    		if(line.length()!=0&&!noURL) {
    			line = line+aReader.readLine();
        		String[] locationExtract = line.split("-");
        		location = locationExtract[locationExtract.length-1].split("\\.")[0];
        		hotelName = locationExtract[locationExtract.length-2];

        		if(locationExtract.length==1) {
        			System.out.println(fileName);
        		}
    		}


    		String path0 = "src/main/resources/modified/" + fileName;
    		File newFile = new File(path0);
        	newFile.createNewFile();
        	BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

        	writer.write("hotelName "+hotelName+"\n");
        	writer.write("location: "+location+"\n");
        	writer.write("overallRating "+overallRating+"\n");
        	writer.write("\n");

        	while((line = aReader.readLine())!=null) {
        		if(!(line.length()>2&&line.substring(0, 1).equals("<")&&!line.contains("<Content>"))) {
        			writer.write(line+"\n");
        		}
        	}
        	writer.flush();
        	writer.close();
        	aReader.close();
    	}
    	catch (IOException e)
         {
            e.printStackTrace();
          }

    }



}
