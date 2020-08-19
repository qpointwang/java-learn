/**
 * @author qpointwang
 */
public class JvmStackoverflowtest {
    private int stackLength = -1;

    //通过递归调用造成StackOverFlowError
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JvmStackoverflowtest searChin = new JvmStackoverflowtest();
        try {
            searChin.stackLeak();
        } catch (Throwable e) {
            System.out.println("Stack length:" + searChin.stackLength);
            e.printStackTrace();
        }
    }
}
