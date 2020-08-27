import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Person implements Comparable<Person> {
    String name;
    Integer age;

    Person(String name, Integer age) {
        this.age = age;
        this.name = name;
    }

    @Override
    public int compareTo(Person antherPerson) {
        return compare(this.age, antherPerson.age);
    }

    private int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y)) ? 0 : 1;
    }
//
//    private int compare(String x, String y){
//        return (x < y) ? -1 : ((x == y)) ? 0 : 1;
//    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        System.out.println(name + " " + age);
        return Objects.hash(name, age);
    }
}

public class NIODemo {

    public static void main(String[] args) {
//        List;
//        Queue;
//        Set;
//        Map;
//        ArrayList;
//        Vector;
//        LinkedList;
//        HashSet
        Set<Person> set = new TreeSet<>();
        // Set<Person> set = new HashSet<>();
        set = new LinkedHashSet<>();
        set.add(new Person("name1", 2));
        set.add(new Person("name2", 0));
        set.add(new Person("name3", 1));
        set.add(new Person("name4", 0));
        set.add(new Person("name5", 10));
        set.add(new Person("name6", 16));


        for (Person e : set) {
            System.out.println(e.toString());
        }

        String a = "a" + "b";
        String b = "ab";
        String c = new String("ab");
        String x = "a";
        String y = "b";
        String z = x + y;
        System.out.println(a == b);
        System.out.println(b == c);
        System.out.println(a == c);
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(x));
        System.out.println(System.identityHashCode(y));
        System.out.println(System.identityHashCode(z));

        Map<Person, Person> map = new ConcurrentHashMap<>();
    }
}


class Solution {
    public int compareVersion(String version1, String version2) {
        String[] nums1 = version1.split("\\.");
        String[] nums2 = version2.split("\\.");
        int n1 = nums1.length, n2 = nums2.length;
        System.out.println(n1 + " " + n2);
        // compare versions
        int i1, i2;
        for (int i = 0; i < Math.max(n1, n2); ++i) {
            i1 = i < n1 ? Integer.parseInt(nums1[i]) : 0;
            i2 = i < n2 ? Integer.parseInt(nums2[i]) : 0;
            if (i1 != i2) {
                return i1 > i2 ? 1 : -1;
            }
        }
        // the versions are equal
        return 0;
    }

    public void weiyiandhuanwei() {
        long[] num = new long[]{1, 2, 3};
        int n = num.length;
        String[] strs = new String[n];
        String[] str1 = new String[n];
        String[] str2 = new String[n];
        for (int i = 0; i < n; i++) {
            StringBuilder start = new StringBuilder(Long.toBinaryString(num[i]));
            int l = start.length();
            for (int j = 0; j < 32 - l; j++) {
                start = start.insert(0, '0');
            }
            // 交换
            for (int j = 0; j < 32; j = j + 2) {
                char temp = start.charAt(j);
                start.setCharAt(j, start.charAt(j + 1));
                start.setCharAt(j + 1, temp);
            }
            strs[i] = start.toString();
            str1[i] = start.substring(0, 30);
            str2[i] = start.substring(30, 32);
        }
        Long[] res = new Long[n];
        for (int i = 0; i < n; i++) {
            String s = null;
            if (i == 0) {
                s = str2[n - 1] + str1[i];
            } else if (i == n - 1) {
                s = str2[0] + str1[i];
            } else {
                s = str2[i + 1] + str1[i];
            }
            res[i] = Long.parseLong(s, 2);
            System.out.println(res[i]);
        }
    }
}

