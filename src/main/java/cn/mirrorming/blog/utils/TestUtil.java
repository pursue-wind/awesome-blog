package cn.mirrorming.blog.utils;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/6/7 22:23
 */
public class TestUtil {
    public static void testGetClassName() {
        // 方法1：通过SecurityManager的保护方法getClassContext()
        String className = new SecurityManager() {
            public String getClassName() {
                Class<?>[] classContext = getClassContext();
                return classContext[4].getName();
            }
        }.getClassName();

        System.err.println(className);
        // 方法2：通过Throwable的方法getStackTrace()
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String clazzName2 = stackTrace[1].getClassName();
        System.err.println(clazzName2);
        // 方法3：通过分析匿名类名称()
        String clazzName3 = new Object() {
            public String getClassName() {

                String clazzName = this.getClass().getName();
                return clazzName.substring(0, clazzName.lastIndexOf('$'));
            }
        }.getClassName();
        System.err.println(clazzName3);
    }
}
