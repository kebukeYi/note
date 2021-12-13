package com.hand.spring.context;

import cn.hutool.core.util.URLUtil;
import com.hand.spring.annoation.Autowried;
import com.hand.spring.annoation.Component;
import com.hand.spring.annoation.ComponentScan;
import com.hand.spring.annoation.Scope;
import com.hand.spring.aware.BeanNameAware;
import com.hand.spring.bean.BeanPostProcessor;
import com.hand.spring.core.BeanDefinition;
import com.hand.spring.init.InitializingBean;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:07
 * @description:
 * @question:
 * @link:
 **/
public class AnnotationSpringContext extends AbstractContext {

    private Class<?> aClass;

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

    private final String singleton = "singleton";
    private final String _class = ".class";
    private final String _jar = ".jar";

    /**
     * 是否初始化类
     */
    private boolean initialize;

    public static final String SEPARATOR = "" + "\\";

    /**
     * 字符串常量：空字符串 {@code ""}
     */
    public static final String EMPTY = "";

    /**
     * 字符串常量：点 {@code "."}
     */
    public static final String DOT = ".";

    public static final int INDEX_NOT_FOUND = -1;

    public AnnotationSpringContext(Class<?> aClass) {
        this.aClass = aClass;
        //解析 @Component() 注解上的值 并且注册 BD
        scan();
        //逐一创建 单例类型的 Bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionsMap.entrySet()) {
            final BeanDefinition beanDefinition = entry.getValue();
            //如果是单例那么就创建
            if (beanDefinition.getScope().equals(singleton)) {
                final String beanName = entry.getKey();
                if (singletonObjects.containsKey(beanName)) continue;
                final Object bean = creatBean(beanName, beanDefinition);
                // singletonObjects.put(beanName, bean);
            }
        }
    }

    @Override
    public Object getBean(String beanName) {
        if (beanDefinitionsMap.containsKey(beanName)) {
            final BeanDefinition beanDefinition = beanDefinitionsMap.get(beanName);
            if (singleton.equals(beanDefinition.getScope())) {
                Object o = singletonObjects.get(beanName);
                return o;
            } else {
                //说明是 原型bean 需要每次都要创建 Bean
                return creatBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }

    public Object creatBean(String beanName, BeanDefinition beanDefinition) {
        final Class<?> beanClass = beanDefinition.getBeanClass();
        final Constructor<?> declaredConstructor;
        try {
            //无参构造方法执行 属性值都是 null
            declaredConstructor = beanClass.getDeclaredConstructor();
            //Todo 1 实例化 bean
            Object newInstance = declaredConstructor.newInstance();
            noInitializingObjects.put(beanName, newInstance);

            //Todo 2 填充属性
            for (Field declaredField : beanClass.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowried.class)) {
                    //ByName 去查找属性
                    String fieldName = declaredField.getName();
                    //不好
                    Object bean = getBean(fieldName);
                    //单例池中没有找到
                    if (bean == null) {
                        //是否实例化过 否则就先用实例化后的
                        bean = noInitializingObjects.get(fieldName);
                        //没有实例化 才去实例化
                        if (bean == null) {
                            bean = creatBean(fieldName, beanDefinitionsMap.get(fieldName));
                        }
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(newInstance, bean);
                }
            }

            //Todo 3 Aware 回调
            if (newInstance instanceof BeanNameAware) {
                ((BeanNameAware) newInstance).setBeanName(beanName);
            }

            //Todo 3.5 普通 Bean 初始化之前操作
            //假如其他 Bean 过来 不能执行这个
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                //交给程序员去加工 但是 这里的入参 跟返回参数不一定是同一个 bean 对象
                newInstance = beanPostProcessor.postProcessBeforeInitialization(newInstance, beanName);
            }

            //Todo 4 是否实现了相关初始化接口
            if (newInstance instanceof InitializingBean) {
                ((InitializingBean) newInstance).afterPropertiesSet();
            }

            //Todo 4.5 初始化之后操作 生命周期的最后一步 AOP 代理对象生成
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                //交给程序员去加工 但是 这里的入参 跟返回参数不一定是同一个 bean 对象
                newInstance = beanPostProcessor.postProcessAfterInitialization(newInstance, beanName);
            }

            singletonObjects.put(beanName, newInstance);
            return newInstance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void scan() {
        ComponentScan declaredAnnotation = (ComponentScan) aClass.getDeclaredAnnotation(ComponentScan.class);
        //com.hand.spring
        String path = declaredAnnotation.value();
        this.packageName = path;
        this.packageNameWithDot = addSuffixIfNot(this.packageName, DOT);
        //com/hand/spring
        String replace = path.replace(".", SEPARATOR);
        this.packageDirName = replace;
        ClassLoader classLoader = AnnotationSpringContext.class.getClassLoader();
        this.classLoader = classLoader;
        URL resource = classLoader.getResource(replace);
        //这里需要转码
        //E:\Projects\Git-Repostiys\note\hand-spring\target\classes\com%5chand%5cspring
        // File file = new File(resource.getFile());
        //E:\Projects\Git-Repostiys\note\hand-spring\target\classes\com\hand\spring
        assert resource != null;
        final File file = new File(URLUtil.decode(resource.getFile(), this.charset));
        scanBeanDefinition(file, null);
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

    public void addIfAccept(Class<?> aClass) {
        if (aClass == null) {
            return;
        }
        if (aClass.isAnnotationPresent(Component.class)) {
            final BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClass(aClass);
            //判断是单例 还是 原型 bean
            //解析 bean -> BeanDefinition
            String beanName = null;
            final Component annotation = aClass.getDeclaredAnnotation(Component.class);
            beanName = annotation.value();
            if ("".equals(beanName)) {
                beanName = generateBeanName(aClass);
            }

            if (aClass.isAnnotationPresent(Scope.class)) {
                final Scope scope = aClass.getAnnotation(Scope.class);
                final String scopeValue = scope.value();
                beanDefinition.setScope(scopeValue);
            } else {
                //没有这个注解说明是 单例 bean
                beanDefinition.setScope(singleton);
            }

            //当前类是否实现了这接口
            //todo 不太优雅
            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                try {
                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                    beanPostProcessors.add(beanPostProcessor);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            beanDefinitionsMap.put(beanName, beanDefinition);
        }
    }

    private String generateBeanName(Class<?> aClass) {
        //DefaultListableBeanFactory -> defaultListableBeanFactory
        final String name = aClass.getName();
        final String[] split = name.split("\\.");
        final String className = split[split.length - 1];
        final char c = className.charAt(0);
        final String substring = className.substring(1);
        //大写转小写
        final String s = ((char) (c + 32)) + substring;
        System.out.println("beanName生成 : " + s);
        return s;
    }

    public void scanBeanDefinition(File file, String rootDir) {
        if (file.isFile()) {
            final String fileAbsolutePath = file.getAbsolutePath();
            if (fileAbsolutePath.endsWith(_class)) {
                final String className = fileAbsolutePath.substring(rootDir.length(), fileAbsolutePath.length() - 6).replace(SEPARATOR, DOT);
                addIfAccept(className);
            } else {
                if (fileAbsolutePath.endsWith(_jar)) {
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
        return addSuffixIfNot(filePath, SEPARATOR);
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
        if (!str2.endsWith(suffix2)) {
            return str2.concat(suffix2);
        }
        return str2;
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }


}
 
