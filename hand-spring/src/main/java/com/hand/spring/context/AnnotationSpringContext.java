package com.hand.spring.context;

import cn.hutool.core.util.URLUtil;
import com.hand.spring.annoation.Component;
import com.hand.spring.annoation.ComponentScan;
import com.hand.spring.annoation.Scope;
import com.hand.spring.core.BeanDefinition;
import com.sun.deploy.util.StringUtils;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:07
 * @description:
 * @question:
 * @link:
 **/
public class AnnotationSpringContext extends AbstractContext {

    private Class aClass;

    /**
     * 包名
     */
    private String packageName;
    /**
     * 包名，最后跟一个点，表示包名，避免在检查前缀时的歧义
     */
    private String packageNameWithDot;

    private String packageDirName;

    private ClassLoader classLoader;

    /**
     * 编码
     */
    private final String charset = "UTF-8";

    /**
     * 是否初始化类
     */
    private boolean initialize;

    public static final String separator = "" + "\\";

    /**
     * 字符串常量：空字符串 {@code ""}
     */
    public static final String EMPTY = "";
    /**
     * 字符串常量：点 {@code "."}
     */
    public static final String DOT = ".";

    public static final int INDEX_NOT_FOUND = -1;

    public AnnotationSpringContext(Class aClass) {
        this.aClass = aClass;
        componentScan();
    }

    public void componentScan() {
        ComponentScan declaredAnnotation = (ComponentScan) aClass.getDeclaredAnnotation(ComponentScan.class);
        String path = declaredAnnotation.value();//com.hand.spring
        this.packageName = path;
        this.packageNameWithDot = addSuffixIfNot(this.packageName, DOT);
        String replace = path.replace(".", separator);//com/hand/spring
        this.packageDirName = replace;
        ClassLoader classLoader = AnnotationSpringContext.class.getClassLoader();
        this.classLoader = classLoader;
        URL resource = classLoader.getResource(replace);
        //这里需要转码
        //E:\Projects\Git-Repostiys\note\hand-spring\target\classes\com%5chand%5cspring
        // File file = new File(resource.getFile());
        //E:\Projects\Git-Repostiys\note\hand-spring\target\classes\com\hand\spring
        final File file = new File(URLUtil.decode(resource.getFile(), this.charset));
        // if (file.isDirectory()) {
        //     final File[] files = file.listFiles();
        //     for (File f : files) {
        scanBeanDefinition(file, null);
        // }
        // }
    }

    public void addIfAccept(String className) {
        if (null == className) {
            return;
        }
        final int classLen = className.length();
        final int packageLen = this.packageName.length();
        if (classLen == packageLen) {
            //类名和包名长度一致，用户可能传入的包名是类名
            if (className.equals(this.packageName)) {
                addIfAccept(loadClass(className));
            }
        } else if (classLen > packageLen) {
            //检查类名是否以指定包名为前缀，包名后加.（避免类似于cn.hutool.A和cn.hutool.ATest这类类名引起的歧义）
            if (className.startsWith(this.packageNameWithDot)) {
                addIfAccept(loadClass(className));
            }
        }
    }

    public void addIfAccept(Class aClass) {
        if (aClass == null) {
            return;
        }
        if (aClass.isAnnotationPresent(Component.class)) {
            //判断是单例 还是 原型 bean
            //解析 bean -> BeanDefinition
            final Component annotation = (Component) aClass.getAnnotation(Component.class);
            final String beanName = annotation.value();
            final BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setaClass(aClass);
            if (aClass.isAnnotationPresent(Scope.class)) {
                final Scope scope = (Scope) aClass.getAnnotation(Scope.class);
                final String scopeValue = scope.value();
                beanDefinition.setScope(scopeValue);
            } else {
                //没有这个注解说明是 单例 bean
                beanDefinition.setScope("singleton");
            }
            beanDefinitionsMap.put(beanName, beanDefinition);
        }
    }


    public void scanBeanDefinition(File file, String rootDir) {
        if (file.isFile()) {
            final String fileAbsolutePath = file.getAbsolutePath();
            if (fileAbsolutePath.endsWith(".class")) {
                final String className = fileAbsolutePath.substring(rootDir.length(), fileAbsolutePath.length() - 6).replace(separator, ".");
                addIfAccept(className);
            } else {
                if (fileAbsolutePath.endsWith(".jar")) {
                    System.out.println("Jar包扫描 再说啦！");
                }
            }
        } else if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (null != listFiles) {
                for (File f : listFiles) {
                    scanBeanDefinition(f, rootDir == null ? subPathBeforePackage(file) : rootDir);
                }
            }
        }
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @return 加载的类
     */
    private Class<?> loadClass(String className) {
        ClassLoader loader = this.classLoader;
        if (null == loader) {
            loader = Thread.currentThread().getContextClassLoader();
            this.classLoader = loader;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, this.initialize, loader);
        } catch (NoClassDefFoundError e) {
            // 由于依赖库导致的类无法加载，直接跳过此类
        } catch (UnsupportedClassVersionError e) {
            // 版本导致的不兼容的类，跳过
        } catch (Exception e) {
            throw new RuntimeException(e);
            // Console.error(e);
        }
        return clazz;
    }

    /**
     * 截取文件绝对路径中包名之前的部分
     *
     * @param file 文件
     * @return 包名之前的部分
     */
    public String subPathBeforePackage(File file) {
        String filePath = file.getAbsolutePath();
        if (this.packageDirName != null) {
            filePath = subBefore(filePath, this.packageDirName, true);
        }
        return addSuffixIfNot(filePath, separator);
    }

    /**
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     * @since 3.1.1
     */
    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string) || separator == null) {
            return null == string ? null : string.toString();
        }

        final String str = string.toString();
        final String sep = separator.toString();
        if (sep.isEmpty()) {
            return EMPTY;
        }
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (INDEX_NOT_FOUND == pos) {
            return str;
        }
        if (0 == pos) {
            return EMPTY;
        }
        return str.substring(0, pos);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }


    /**
     * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 补充后的字符串
     */
    public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str(str);
        }

        final String str2 = str.toString();
        final String suffix2 = suffix.toString();
        if (false == str2.endsWith(suffix2)) {
            return str2.concat(suffix2);
        }
        return str2;
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    @Override
    public Object getBean(String beanName) {
        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        } else {
            throw new NullPointerException();
        }
    }

}
 
