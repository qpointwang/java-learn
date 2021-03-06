import java.lang.reflect.*;

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

        getClassInfo();

        invokeMethod();

        constructor();
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


    /**
     * 对任意的一个Object实例，只要我们获取了它的Class，就可以获取它的一切信息。
     */
    static public void getClassInfo() {
        Class stdClass = Student.class;

        try {

            Field[] fields = stdClass.getDeclaredFields();//获取这个类所有的成员变量

            for (Field field : fields) {
                System.out.println(field.getName() + " " + field.toString());
            }


            // getFields()	  获取所有public字段,包括父类字段
            // getDeclaredFields()	获取所有字段,public和protected和private,但是不包括父类字段
            // java 修饰符  默认什么都不加，就是default   public int score;去掉public stdClass.getField("score")就会出错了


            // 获取public字段"score":
            System.out.println(stdClass.getField("score"));
            // 获取继承的public字段"name":
            System.out.println(stdClass.getField("name")); // getDeclaredFields无法获取父类的字段，即使是public
            // 获取private字段"grade":
            System.out.println(stdClass.getDeclaredField("grade"));

            Field field = stdClass.getDeclaredField("grade");
            System.out.println(field.getName());
            System.out.println(field.getType());
            System.out.println(field.getModifiers());
            int m = field.getModifiers();
            System.out.println(Modifier.isPrivate(m));
            System.out.println(Modifier.isPublic(m));


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        try {
            // 获取public方法getScore，参数为String int
            System.out.println(stdClass.getMethod("getScore", String.class, int.class));
            // 获取继承的public方法getName，无参数:
            System.out.println(stdClass.getMethod("getName"));
            // 获取private方法getGrade，参数为int:
            System.out.println(stdClass.getDeclaredMethod("getGrade", int.class));


            // 一个Method对象包含一个方法的所有信息：
            //
            //getName()：返回方法名称，例如："getScore"；
            //getReturnType()：返回方法返回值类型，也是一个Class实例，例如：String.class；
            //getParameterTypes()：返回方法的参数类型，是一个Class数组，例如：{String.class, int.class}；
            //getModifiers()：返回方法的修饰符，它是一个int，不同的bit表示不同的含义。
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    static public void invokeMethod(){
        String s = "Hello world";
        String r = s.substring(6); // "world"
        System.out.println(r);

        Class cls = String.class;
        try {
            Method method = cls.getMethod("substring", int.class);
            // 对Method实例调用invoke就相当于调用该方法，invoke的第一个参数是对象实例，即在哪个实例上调用该方法，后面的可变参数要与方法参数一致，否则将报错。
            String t = (String) method.invoke(s,6);
            System.out.println(t);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用静态方法
        //如果获取到的Method表示一个静态方法，调用静态方法时，由于无需指定实例对象，所以invoke方法传入的第一个参数永远为null
        try {
            Method method = Integer.class.getMethod("parseInt", String.class);
            System.out.println(method.invoke(null, "666"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //通过Class实例的方法可以获取Method实例：getMethod()，getMethods()，getDeclaredMethod()，getDeclaredMethods()；
        //
        //通过Method实例可以获取方法信息：getName()，getReturnType()，getParameterTypes()，getModifiers()；
        //
        //通过Method实例可以调用某个对象的方法：Object invoke(Object instance, Object... parameters)；
        //
        //通过设置setAccessible(true)来访问非public方法；

    }


    static public void constructor(){

//        try {
//            Person p = Person.class.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        // 调用Class.newInstance()的局限是，它只能调用该类的public无参数构造方法。如果构造方法带有参数，或者不是public，就无法直接通过Class.newInstance()来调用。
        // 获取构造方法Integer(int):
        Constructor cons1 = null;
        try {
            cons1 = Integer.class.getConstructor(int.class);
            // 调用构造方法:
            Integer n1 = (Integer) cons1.newInstance(123);
            System.out.println(n1);

            // 获取构造方法Integer(String)
            Constructor cons2 = Integer.class.getConstructor(String.class);
            Integer n2 = (Integer) cons2.newInstance("456");
            System.out.println(n2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 通过Class实例获取Constructor的方法如下：
        //
        //getConstructor(Class...)：获取某个public的Constructor；
        //getDeclaredConstructor(Class...)：获取某个Constructor；
        //getConstructors()：获取所有public的Constructor；
        //getDeclaredConstructors()：获取所有Constructor。
        //注意Constructor总是当前类定义的构造方法，和父类无关，因此不存在多态的问题。
        //
        //调用非public的Constructor时，必须首先通过setAccessible(true)设置允许访问。setAccessible(true)可能会失败。
    }

}


class Student extends People {
    public int score;
    private int grade;


    public int getScore(String type, int grade) {
        return grade;
    }

    private int getGrade(int year) {
        return 1;
    }

    Student(String name) {
        super(name);
    }
}

class People {
    public String name;

    public People(String name) {
        this.name = name;
    }

    public String getName() {
        return "Person";
    }
}

// 小结
//Java的反射API提供的Field类封装了字段的所有信息：
//
//通过Class实例的方法可以获取Field实例：getField()，getFields()，getDeclaredField()，getDeclaredFields()；
//
//通过Field实例可以获取字段信息：getName()，getType()，getModifiers()；
//
//通过Field实例可以读取或设置某个对象的字段，如果存在访问限制，要首先调用setAccessible(true)来访问非public字段。
//
//通过反射读写字段是一种非常规方法，它会破坏对象的封装。