import java.io.IOException;

/**
 * @anthor qpointwang
 * @date 2020/8/27 14:40
 */
public class JavaForLinuxDemo {

    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("ls ./");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
