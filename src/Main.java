
import com.hzhan.util.HttpUtil;

import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        int port = 8080;

        try {
            HttpUtil.startServer(port, 0);
            System.out.println("server is listening to port " + port + "...");
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}