import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class TrecConv {
    public static void main(String[] args) throws Exception{
        String input_path = "input";
        String output_path = "output";

        for (int i = 0; i < args.length; i++) {
            if ("-input".equals(args[i])) {
                input_path = args[i + 1];
                i++;
            }
            else if ("-output".equals(args[i])) {
                output_path = args[i + 1];
                i++;
            }
        }

        FileReader input = new FileReader(input_path);
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = bufRead.readLine();

        FileWriter output = new FileWriter(output_path);
        BufferedWriter bw = new BufferedWriter(output);
//        bw.write(content);
        String[] test;
        int iter=0;
        while(myLine != null){
              test = myLine.trim().split(" ");
              if (test.length==3)
                  if(Integer.parseInt(test[2])==-1)
                      bw.write(test[0]+" 0 "+test[1]+" "+"5");
                  else if(Integer.parseInt(test[2])==1)
                      bw.write(test[0]+" 0 "+test[1]+" "+"4");
                  else if(Integer.parseInt(test[2])==2)
                      bw.write(test[0]+" 0 "+test[1]+" "+"3");
                  else if(Integer.parseInt(test[2])==3)
                      bw.write(test[0]+" 0 "+test[1]+" "+"2");
                  else
                      bw.write(test[0]+" 0 "+test[1]+" "+"1");
              else
                  if(Integer.parseInt(test[3])==-1)
                      bw.write(test[0]+" 0 "+test[1]+" "+"5");
                  else if(Integer.parseInt(test[3])==1)
                      bw.write(test[0]+" 0 "+test[1]+" "+"4");
                  else if(Integer.parseInt(test[3])==2)
                      bw.write(test[0]+" 0 "+test[1]+" "+"3");
                  else if(Integer.parseInt(test[3])==3)
                      bw.write(test[0]+" 0 "+test[1]+" "+"2");
                  else
                      bw.write(test[0]+" 0 "+test[1]+" "+"1");

              bw.newLine();
              myLine=bufRead.readLine();
              iter+=1;
              if(iter==315)
              {
                  continue;
              }
//              myLine=bufRead.readLine();
        }
        bufRead.close();
        bw.close();
    }
}
