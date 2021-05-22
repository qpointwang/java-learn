

/**
 * 由于JVM为每个加载的class创建了对应的Class实例（类实例），并在实例中保存了该class的所有信息，包括类名、包名、父类、实现的接口、所有方法、字段等，
 * 因此，如果获取了某个Class实例，我们就可以通过这个Class实例获取到该实例对应的class的所有信息。
 * 这种通过Class实例获取class信息的方法称为反射（Reflection）。
 */
public class ReflectionDemo {
    public static void main(String[] args) {
        printClassInfo("".getClass());
        printClassInfo(Runnable.class);
        printClassInfo(java.time.Month.class);
        printClassInfo(String[].class);
        printClassInfo(int.class);

        getClassInstance();
    }

    static void printClassInfo(Class cls) {
        System.out.println("Class name: " + cls.getName());
        System.out.println("Simple name: " + cls.getSimpleName());
        if (cls.getPackage() != null) {
            System.out.println("Package name: " + cls.getPackage().getName());
        }
        System.out.println("is interface: " + cls.isInterface());
        System.out.println("is enum: " + cls.isEnum());
        System.out.println("is array: " + cls.isArray());
        System.out.println("is primitive: " + cls.isPrimitive());
    }

    /**
     * 获取Class实例的三种方法
     */
    static void getClassInstance() {
        // 方法一：直接通过一个class的静态变量class获取：
        Class cls1 = String.class;
        System.out.println(cls1);
        // 方法二：如果我们有一个实例变量，可以通过该实例变量提供的getClass()方法获取：
        String s = "wangqi";
        Class cls2 = s.getClass();
        System.out.println(cls2);
        // 方法三：如果知道一个class的完整类名，可以通过静态方法Class.forName()获取：
        try {
            Class cls3 = Class.forName("java.lang.String");
            System.out.println(cls3);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // 创建一个String实例:
        try {
            String str = (String) cls1.newInstance();
            System.out.println("这里" + str);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 上述代码相当于new String()。通过Class.newInstance()可以创建类实例，它的局限是：只能调用public的无参数构造方法。
        // 带参数的构造方法，或者非public的构造方法都无法通过Class.newInstance()被调用。
    }
    /**
     * 小结：
     * 1. JVM为每个加载的class及interface创建了对应的Class实例来保存class及interface的所有信息；
     *
     * 2. 获取一个class对应的Class实例后，就可以获取该class的所有信息；
     *
     * 3. 通过Class实例获取class信息的方法称为反射（Reflection）；
     *
     * 4. JVM总是动态加载class，可以在运行期根据条件来控制加载class。
     * */
}
