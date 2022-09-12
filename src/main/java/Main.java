
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static Boolean OLYMPIC = true;

    public static void main( String[] args ) throws Exception {
        try{
            ServerSocket serverSocket = new ServerSocket(7001);
            while(OLYMPIC) {

                Socket client = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()).getBytes().length != 0) {
                    requestBuilder.append(line + "\r\n");
                }

                OutputStream clientOutput = client.getOutputStream();
                clientOutput.write(("HTTP/1.2 \r\n" + "200 Ok").getBytes());
                clientOutput.write(("Content-Type: " + guessContentType(getFilePath("index.html")) + "\r\n").getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write("ok".getBytes());
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();
                client.close();

                client = serverSocket.accept();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        return Paths.get("www", path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

}