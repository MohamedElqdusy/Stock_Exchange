import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Log {

    // Write Logs to "log.txt" file
    public void writeFile(String str) throws IOException {
        FileWriter fw = new FileWriter("log.txt", true);
        // fw.write(str);
        fw.append(str);
        fw.close();
    }

    // Log Screenshoot
    public void logScreenshoot() throws IOException {
        FileInputStream fis = new FileInputStream("log.txt");

        // Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
    }
}
