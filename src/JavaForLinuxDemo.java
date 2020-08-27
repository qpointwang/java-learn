import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @anthor qpointwang
 * @date 2020/8/27 14:40
 */
public class JavaForLinuxDemo {

    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("ifconfig");
            //取得命令结果的输出流
            InputStream inputStream = process.getInputStream();
            //用一个读输出流类去读
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            //用缓冲器读行
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            //直到读完为止
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
