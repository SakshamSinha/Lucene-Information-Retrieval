import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class Indexing {

    static void startIndexing(Directory dir, Path docDir) throws Exception {
        Analyzer analyzer = Config.getAnalyzer();
        Similarity similarity = Config.getSimilarity();

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setSimilarity(similarity);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        iwc.setRAMBufferSizeMB(256.0);
        IndexWriter writer = new IndexWriter(dir, iwc);
        indexDocs(writer, docDir);
        System.out.println("Indexing completed");
        writer.close();
    }
    static void indexDocs(IndexWriter writer, Path path)
            throws IOException {
        // do not try to index files that cannot be read
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                } catch (IOException ignore) {
                // don't index files that can't be read.
                }
                return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

  static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        try {
              FileReader input = new FileReader(file.toString());
              BufferedReader bufRead = new BufferedReader(input);
              String myLine = bufRead.readLine();
              while ( myLine != null)
              {
                  Document doc = new Document();
                  String id = "";
                  if(myLine.startsWith(".I")){
                      id = myLine.substring(3);
                      System.out.println("Indexing doc with id "+id);
                      doc.add((new TextField("Id", id, Field.Store.YES)));
                      myLine=bufRead.readLine();
                  }
                  String nextLine="";
                  if(myLine.startsWith(".T")){
                      myLine = bufRead.readLine();
                      while(!myLine.startsWith(".A")){
                          nextLine = nextLine.concat(myLine);
                          myLine = bufRead.readLine();
                      }

                      doc.add((new TextField("Title", nextLine, Field.Store.YES)));
                  }
                  nextLine="";
                  if(myLine.startsWith(".A")){
                      myLine = bufRead.readLine();
                      while(!myLine.startsWith(".B")) {
                          nextLine = nextLine.concat(myLine).trim();
                          myLine = bufRead.readLine();
                      }
                      doc.add((new TextField("Author", nextLine, Field.Store.YES)));
                  }
                  nextLine="";
                  if(myLine.startsWith(".B")){
                      myLine = bufRead.readLine();
                      while(!myLine.startsWith(".W")){
                          nextLine = nextLine.concat(myLine);
                          myLine = bufRead.readLine();
                      }
                      doc.add((new TextField("Bibliography", nextLine, Field.Store.YES)));
                  }
                  nextLine="";
                  if(myLine.startsWith(".W")){
                      myLine = bufRead.readLine();
                      while(myLine!=null && !myLine.startsWith(".I")){
                          nextLine = nextLine.concat(myLine);
                          myLine = bufRead.readLine();
                      }
                      doc.add((new TextField("Abstract", nextLine, Field.Store.YES)));
                  }

                  if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                      System.out.println("adding " + file + " " + id);
                      writer.addDocument(doc);
                  }
              }
        }
        catch(IOException e){
            System.out.println("Error reading the file");
      }

  }
}
