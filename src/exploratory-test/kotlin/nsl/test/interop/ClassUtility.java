package nsl.test.interop;

public class ClassUtility {

    public static void printClass(Class<?> clazz) {
        System.out.println("clazz argument: " + clazz);
    }

    public static void printClasses(Class<?>[] classes) {
        for(Class clazz: classes) {
            System.out.println("class: " + clazz);
        }
    }

}
