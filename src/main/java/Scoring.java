import java.io.*;

public class Scoring {

    public static void scoreit() throws Exception {
        try {
            String target = new String("trec_eval.9.0/trec_eval -l 3 src/main/resources/Query/trecrel src/main/resources/Query/output");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(target);
            proc.waitFor();
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            System.out.println("------Scores------\n" + output);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

