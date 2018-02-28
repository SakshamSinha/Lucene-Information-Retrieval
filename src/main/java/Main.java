import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public  class Main {
    public static void main(String[] args) throws Exception{
            String usage = "java "
                    + " [-index INDEX_PATH] [-docs DOCS_PATH] [-queries file] [-numdocs hitsPerPage]\n\n"
                    + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                    + "in INDEX_PATH that can be searched with SearchFiles";
            String index = "index";
            String docsPath = null;
            boolean create = true;
            String queries = null;
            String queryString = null;
            int numdocs = 10;
            FileWriter output = new FileWriter("src/main/resources/Query/output");
            BufferedWriter bw = new BufferedWriter(output);
            for (int i = 0; i < args.length; i++) {
                if ("-index".equals(args[i])) {
                    index = args[i + 1];
                    i++;
                } else if ("-docs".equals(args[i])) {
                    docsPath = args[i + 1];
                    i++;
                }
                else if ("-queries".equals(args[i])) {
                    queries = args[i + 1];
                    i++;
                } else if ("-numdocs".equals(args[i])) {
                    numdocs = Integer.parseInt(args[i + 1]);
                    if (numdocs <= 0) {
                        System.err.println("There must be at least 1 hit per page.");
                        System.exit(1);
                    }
                    i++;
                }
            }

            if (docsPath == null) {
                System.err.println("Usage: " + usage);
                System.exit(1);
            }

//        Path p = Paths.get("./logfile.txt");
            final Path docDir = Paths.get(docsPath);
            if (!Files.isReadable(docDir)) {
                System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
                System.exit(1);
            }
            Date start = new Date();
            try {
                System.out.println("Indexing to directory '" + index + "'...");
                Directory dir = FSDirectory.open(Paths.get(index));
                Indexing.startIndexing(dir,docDir);
                Searching.startSearching(index, queries, numdocs);
                Scoring.scoreit();
            } catch (IOException e) {
                System.out.println(" caught a " + e.getClass() +
                        "\n with message: " + e.getMessage());
            }
    }
}
