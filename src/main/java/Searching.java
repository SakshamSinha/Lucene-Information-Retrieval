import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;


public class Searching {

    public static void startSearching(String index, String queries, int numdocs) throws Exception{
        FileWriter output = new FileWriter("src/main/resources/Query/output");
        BufferedWriter bw = new BufferedWriter(output);

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = Config.getAnalyzer();
        Similarity similarity = Config.getSimilarity();

        searcher.setSimilarity(similarity);
        BufferedReader in = null;
        in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        Map<String, Float> boostFields = new HashMap<String, Float>();
        boostFields.put("Abstract",10f);
        boostFields.put("Title",5f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"Title","Abstract","Author"}, analyzer, boostFields);

        parser.setAllowLeadingWildcard(true);
        int iter=0;
        String line = "";

        while (true) {
            if (queries == null) {             // prompt the user
                System.out.println("Please specify the query path.");
            }
            iter+=1;
            line = in.readLine();
            String id = "";
            if(line.startsWith(".I")){
                id = line.substring(3);
                System.out.println("Query with id "+id);
                line=in.readLine();
            }

            String queryLine="";

            if(line.startsWith(".W")){
                line = in.readLine();
                while(line!=null && !line.startsWith(".I")){
                    queryLine = queryLine.concat(line);
                    line = in.readLine();
                }
            }


            queryLine = queryLine.trim();

            Query query = parser.parse(queryLine);
            System.out.println("Searching for: " + query.toString());

            doPagingSearch(bw, in, iter, searcher, query, numdocs, false);

            if (line == null || line.length() == -1) {
                break;
            }

            if (line.length() == 0) {
                break;
            }

        }
        bw.close();
        in.close();
        System.out.println("Searching completed");
    }

    public static void doPagingSearch(BufferedWriter bw,BufferedReader in,int qid, IndexSearcher searcher, Query query,
                                      int hitsPerPage, boolean interactive) throws IOException {

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits);
        System.out.println(numTotalHits + " total matching documents");

        int start = 0;
        int end = Math.min(hits.length, start + hitsPerPage);

        for (int i = start; i < end; i++) {
            System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
            bw.write(String.valueOf(qid) + " Q0 " + String.valueOf(Integer.valueOf(hits[i].doc) + 1) + " " + String.valueOf(i + 1) + " " + String.valueOf(hits[i].score) + " Exp\n");
        }
    }
}






























