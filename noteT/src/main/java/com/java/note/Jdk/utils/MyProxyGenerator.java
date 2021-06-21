package com.java.note.Jdk.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class MyProxyGenerator {
    private static final int CLASSFILE_MAJOR_VERSION = 49;
    private static final int CLASSFILE_MINOR_VERSION = 0;
    private static final int CONSTANT_UTF8 = 1;
    private static final int CONSTANT_UNICODE = 2;
    private static final int CONSTANT_INTEGER = 3;
    private static final int CONSTANT_FLOAT = 4;
    private static final int CONSTANT_LONG = 5;
    private static final int CONSTANT_DOUBLE = 6;
    private static final int CONSTANT_CLASS = 7;
    private static final int CONSTANT_STRING = 8;
    private static final int CONSTANT_FIELD = 9;
    private static final int CONSTANT_METHOD = 10;
    private static final int CONSTANT_INTERFACEMETHOD = 11;
    private static final int CONSTANT_NAMEANDTYPE = 12;
    private static final int ACC_PUBLIC = 1;
    private static final int ACC_PRIVATE = 2;
    private static final int ACC_STATIC = 8;
    private static final int ACC_FINAL = 16;
    private static final int ACC_SUPER = 32;
    private static final int opc_aconst_null = 1;
    private static final int opc_iconst_0 = 3;
    private static final int opc_bipush = 16;
    private static final int opc_sipush = 17;
    private static final int opc_ldc = 18;
    private static final int opc_ldc_w = 19;
    private static final int opc_iload = 21;
    private static final int opc_lload = 22;
    private static final int opc_fload = 23;
    private static final int opc_dload = 24;
    private static final int opc_aload = 25;
    private static final int opc_iload_0 = 26;
    private static final int opc_lload_0 = 30;
    private static final int opc_fload_0 = 34;
    private static final int opc_dload_0 = 38;
    private static final int opc_aload_0 = 42;
    private static final int opc_astore = 58;
    private static final int opc_astore_0 = 75;
    private static final int opc_aastore = 83;
    private static final int opc_pop = 87;
    private static final int opc_dup = 89;
    private static final int opc_ireturn = 172;
    private static final int opc_lreturn = 173;
    private static final int opc_freturn = 174;
    private static final int opc_dreturn = 175;
    private static final int opc_areturn = 176;
    private static final int opc_return = 177;
    private static final int opc_getstatic = 178;
    private static final int opc_putstatic = 179;
    private static final int opc_getfield = 180;
    private static final int opc_invokevirtual = 182;
    private static final int opc_invokespecial = 183;
    private static final int opc_invokestatic = 184;
    private static final int opc_invokeinterface = 185;
    private static final int opc_new = 187;
    private static final int opc_anewarray = 189;
    private static final int opc_athrow = 191;
    private static final int opc_checkcast = 192;
    private static final int opc_wide = 196;
    private static final Strings superclassName = "java/lang/reflect/Proxy";
    private static final Strings handlerFieldName = "h";
    private static final boolean saveGeneratedFiles = (Boolean) AccessController.doPrivileged(new GetBooleanAction("MyProxyGenerator.saveGeneratedFiles"));
    private static Method hashCodeMethod;
    private static Method equalsMethod;
    private static Method toStringMethod;
    private Strings className;
    private Class<?>[] interfaces;
    private int accessFlags;
    private ConstantPool cp = new MyProxyGenerator.ConstantPool();
    private List<FieldInfo> fields = new ArrayList();
    private List<MethodInfo> methods = new ArrayList();
    private Map<Strings, List<ProxyMethod>> proxyMethods = new HashMap();
    private int proxyMethodCount = 0;

    public static byte[] generateProxyClass(Strings var0, Class<?>[] var1) {
        return generateProxyClass(var0, var1, 49);
    }

    public static byte[] generateProxyClass(final Strings var0, Class<?>[] var1, int var2) {
        MyProxyGenerator var3 = new MyProxyGenerator(var0, var1, var2);
        final byte[] var4 = var3.generateClassFile();
        if (saveGeneratedFiles) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    try {
                        int var1 = var0.lastIndexOf(46);
                        Path var2;
                        if (var1 > 0) {
                            Path var3 = Paths.get(var0.substring(0, var1).replace('.', File.separatorChar));
                            Files.createDirectories(var3);
                            var2 = var3.resolve(var0.substring(var1 + 1, var0.length()) + ".class");
                        } else {
                            var2 = Paths.get(var0 + ".class");
                        }

                        Files.write(var2, var4, new OpenOption[0]);
                        return null;
                    } catch (IOException var4x) {
                        throw new InternalError("I/O exception saving generated file: " + var4x);
                    }
                }
            });
        }

        return var4;
    }

    private MyProxyGenerator(Strings var1, Class<?>[] var2, int var3) {
        this.className = var1;
        this.interfaces = var2;
        this.accessFlags = var3;
    }

    private byte[] generateClassFile() {
        this.addProxyMethod(hashCodeMethod, Object.class);
        this.addProxyMethod(equalsMethod, Object.class);
        this.addProxyMethod(toStringMethod, Object.class);
        Class[] var1 = this.interfaces;
        int var2 = var1.length;

        int var3;
        Class var4;
        for (var3 = 0; var3 < var2; ++var3) {
            var4 = var1[var3];
            Method[] var5 = var4.getMethods();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Method var8 = var5[var7];
                this.addProxyMethod(var8, var4);
            }
        }

        Iterator var11 = this.proxyMethods.values().iterator();

        List var12;
        while (var11.hasNext()) {
            var12 = (List) var11.next();
            checkReturnTypes(var12);
        }

        Iterator var15;
        try {
            this.methods.add(this.generateConstructor());
            var11 = this.proxyMethods.values().iterator();

            while (var11.hasNext()) {
                var12 = (List) var11.next();
                var15 = var12.iterator();

                while (var15.hasNext()) {
                    MyProxyGenerator.ProxyMethod var16 = (MyProxyGenerator.ProxyMethod) var15.next();
                    this.fields.add(new MyProxyGenerator.FieldInfo(var16.methodFieldName, "Ljava/lang/reflect/Method;", 10));
                    this.methods.add(var16.generateMethod());
                }
            }

            this.methods.add(this.generateStaticInitializer());
        } catch (IOException var10) {
            throw new InternalError("unexpected I/O Exception", var10);
        }

        if (this.methods.size() > 65535) {
            throw new IllegalArgumentException("method limit exceeded");
        } else if (this.fields.size() > 65535) {
            throw new IllegalArgumentException("field limit exceeded");
        } else {
            this.cp.getClass(dotToSlash(this.className));
            this.cp.getClass("java/lang/reflect/Proxy");
            var1 = this.interfaces;
            var2 = var1.length;

            for (var3 = 0; var3 < var2; ++var3) {
                var4 = var1[var3];
                this.cp.getClass(dotToSlash(var4.getName()));
            }

            this.cp.setReadOnly();
            ByteArrayOutputStream var13 = new ByteArrayOutputStream();
            DataOutputStream var14 = new DataOutputStream(var13);

            try {
                var14.writeInt(-889275714);
                var14.writeShort(0);
                var14.writeShort(49);
                this.cp.write(var14);
                var14.writeShort(this.accessFlags);
                var14.writeShort(this.cp.getClass(dotToSlash(this.className)));
                var14.writeShort(this.cp.getClass("java/lang/reflect/Proxy"));
                var14.writeShort(this.interfaces.length);
                Class[] var17 = this.interfaces;
                int var18 = var17.length;

                for (int var19 = 0; var19 < var18; ++var19) {
                    Class var22 = var17[var19];
                    var14.writeShort(this.cp.getClass(dotToSlash(var22.getName())));
                }

                var14.writeShort(this.fields.size());
                var15 = this.fields.iterator();

                while (var15.hasNext()) {
                    MyProxyGenerator.FieldInfo var20 = (MyProxyGenerator.FieldInfo) var15.next();
                    var20.write(var14);
                }

                var14.writeShort(this.methods.size());
                var15 = this.methods.iterator();

                while (var15.hasNext()) {
                    MyProxyGenerator.MethodInfo var21 = (MyProxyGenerator.MethodInfo) var15.next();
                    var21.write(var14);
                }

                var14.writeShort(0);
                return var13.toByteArray();
            } catch (IOException var9) {
                throw new InternalError("unexpected I/O Exception", var9);
            }
        }
    }

    private void addProxyMethod(Method var1, Class<?> var2) {
        Strings var3 = var1.getName();
        Class[] var4 = var1.getParameterTypes();
        Class var5 = var1.getReturnType();
        Class[] var6 = var1.getExceptionTypes();
        Strings var7 = var3 + getParameterDescriptors(var4);
        Object var8 = (List) this.proxyMethods.get(var7);
        if (var8 != null) {
            Iterator var9 = ((List) var8).iterator();

            while (var9.hasNext()) {
                MyProxyGenerator.ProxyMethod var10 = (MyProxyGenerator.ProxyMethod) var9.next();
                if (var5 == var10.returnType) {
                    ArrayList var11 = new ArrayList();
                    collectCompatibleTypes(var6, var10.exceptionTypes, var11);
                    collectCompatibleTypes(var10.exceptionTypes, var6, var11);
                    var10.exceptionTypes = new Class[var11.size()];
                    var10.exceptionTypes = (Class[]) var11.toArray(var10.exceptionTypes);
                    return;
                }
            }
        } else {
            var8 = new ArrayList(3);
            this.proxyMethods.put(var7, (List<ProxyMethod>) var8);
        }

        ((List) var8).add(new MyProxyGenerator.ProxyMethod(var3, var4, var5, var6, var2));
    }

    private static void checkReturnTypes(List<ProxyMethod> paramList) {
        if (paramList.size() < 2)
            return;
        LinkedList<Class<?>> linkedList = new LinkedList();
        label30:
        for (ProxyMethod proxyMethod : paramList) {
            Class<?> clazz = proxyMethod.returnType;
            if (clazz.isPrimitive())
                throw new IllegalArgumentException("methods with same signature " +

                        getFriendlyMethodSignature(proxyMethod.methodName, proxyMethod.parameterTypes) + " but incompatible return types: " + clazz

                        .getName() + " and others");
            boolean bool = false;
            ListIterator<Class<?>> listIterator = linkedList.listIterator();
            while (listIterator.hasNext()) {
                Class<?> clazz1 = listIterator.next();
                if (clazz.isAssignableFrom(clazz1)) {
                    assert false;
                    continue label30;
                }
                if (clazz1.isAssignableFrom(clazz)) {
                    if (!bool) {
                        listIterator.set(clazz);
                        bool = true;
                        continue;
                    }
                    listIterator.remove();
                }
            }
            if (!bool)
                linkedList.add(clazz);
        }
        if (linkedList.size() > 1) {
            ProxyMethod proxyMethod = paramList.get(0);
            throw new IllegalArgumentException("methods with same signature " +

                    getFriendlyMethodSignature(proxyMethod.methodName, proxyMethod.parameterTypes) + " but incompatible return types: " + linkedList);
        }
    }

    private MyProxyGenerator.MethodInfo generateConstructor() throws IOException {
        MyProxyGenerator.MethodInfo var1 = new MyProxyGenerator.MethodInfo("<init>", "(Ljava/lang/reflect/InvocationHandler;)V", 1);
        DataOutputStream var2 = new DataOutputStream(var1.code);
        this.code_aload(0, var2);
        this.code_aload(1, var2);
        var2.writeByte(183);
        var2.writeShort(this.cp.getMethodRef("java/lang/reflect/Proxy", "<init>", "(Ljava/lang/reflect/InvocationHandler;)V"));
        var2.writeByte(177);
        var1.maxStack = 10;
        var1.maxLocals = 2;
        var1.declaredExceptions = new short[0];
        return var1;
    }

    private MyProxyGenerator.MethodInfo generateStaticInitializer() throws IOException {
        MyProxyGenerator.MethodInfo var1 = new MyProxyGenerator.MethodInfo("<clinit>", "()V", 8);
        byte var2 = 1;
        byte var4 = 0;
        DataOutputStream var6 = new DataOutputStream(var1.code);
        Iterator var7 = this.proxyMethods.values().iterator();

        while (var7.hasNext()) {
            List var8 = (List) var7.next();
            Iterator var9 = var8.iterator();

            while (var9.hasNext()) {
                MyProxyGenerator.ProxyMethod var10 = (MyProxyGenerator.ProxyMethod) var9.next();
                var10.codeFieldInitialization(var6);
            }
        }

        var6.writeByte(177);
        short var3;
        short var5 = var3 = (short) var1.code.size();
        var1.exceptionTable.add(new MyProxyGenerator.ExceptionTableEntry(var4, var5, var3, this.cp.getClass("java/lang/NoSuchMethodException")));
        this.code_astore(var2, var6);
        var6.writeByte(187);
        var6.writeShort(this.cp.getClass("java/lang/NoSuchMethodError"));
        var6.writeByte(89);
        this.code_aload(var2, var6);
        var6.writeByte(182);
        var6.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
        var6.writeByte(183);
        var6.writeShort(this.cp.getMethodRef("java/lang/NoSuchMethodError", "<init>", "(Ljava/lang/String;)V"));
        var6.writeByte(191);
        var3 = (short) var1.code.size();
        var1.exceptionTable.add(new MyProxyGenerator.ExceptionTableEntry(var4, var5, var3, this.cp.getClass("java/lang/ClassNotFoundException")));
        this.code_astore(var2, var6);
        var6.writeByte(187);
        var6.writeShort(this.cp.getClass("java/lang/NoClassDefFoundError"));
        var6.writeByte(89);
        this.code_aload(var2, var6);
        var6.writeByte(182);
        var6.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
        var6.writeByte(183);
        var6.writeShort(this.cp.getMethodRef("java/lang/NoClassDefFoundError", "<init>", "(Ljava/lang/String;)V"));
        var6.writeByte(191);
        if (var1.code.size() > 65535) {
            throw new IllegalArgumentException("code size limit exceeded");
        } else {
            var1.maxStack = 10;
            var1.maxLocals = (short) (var2 + 1);
            var1.declaredExceptions = new short[0];
            return var1;
        }
    }

    private void code_iload(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 21, 26, var2);
    }

    private void code_lload(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 22, 30, var2);
    }

    private void code_fload(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 23, 34, var2);
    }

    private void code_dload(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 24, 38, var2);
    }

    private void code_aload(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 25, 42, var2);
    }

    private void code_astore(int var1, DataOutputStream var2) throws IOException {
        this.codeLocalLoadStore(var1, 58, 75, var2);
    }

    private void codeLocalLoadStore(int var1, int var2, int var3, DataOutputStream var4) throws IOException {
        assert var1 >= 0 && var1 <= 65535;

        if (var1 <= 3) {
            var4.writeByte(var3 + var1);
        } else if (var1 <= 255) {
            var4.writeByte(var2);
            var4.writeByte(var1 & 255);
        } else {
            var4.writeByte(196);
            var4.writeByte(var2);
            var4.writeShort(var1 & '\uffff');
        }

    }

    private void code_ldc(int var1, DataOutputStream var2) throws IOException {
        assert var1 >= 0 && var1 <= 65535;

        if (var1 <= 255) {
            var2.writeByte(18);
            var2.writeByte(var1 & 255);
        } else {
            var2.writeByte(19);
            var2.writeShort(var1 & '\uffff');
        }

    }

    private void code_ipush(int var1, DataOutputStream var2) throws IOException {
        if (var1 >= -1 && var1 <= 5) {
            var2.writeByte(3 + var1);
        } else if (var1 >= -128 && var1 <= 127) {
            var2.writeByte(16);
            var2.writeByte(var1 & 255);
        } else {
            if (var1 < -32768 || var1 > 32767) {
                throw new AssertionError();
            }

            var2.writeByte(17);
            var2.writeShort(var1 & '\uffff');
        }

    }

    private void codeClassForName(Class<?> var1, DataOutputStream var2) throws IOException {
        this.code_ldc(this.cp.getString(var1.getName()), var2);
        var2.writeByte(184);
        var2.writeShort(this.cp.getMethodRef("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;"));
    }

    private static Strings dotToSlash(Strings var0) {
        return var0.replace('.', '/');
    }

    private static Strings getMethodDescriptor(Class<?>[] var0, Class<?> var1) {
        return getParameterDescriptors(var0) + (var1 == Void.TYPE ? "V" : getFieldType(var1));
    }

    private static Strings getParameterDescriptors(Class<?>[] var0) {
        StringBuilder var1 = new StringBuilder("(");

        for (int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(getFieldType(var0[var2]));
        }

        var1.append(')');
        return var1.toString();
    }

    private static Strings getFieldType(Class<?> var0) {
        if (var0.isPrimitive()) {
            return MyProxyGenerator.PrimitiveTypeInfo.get(var0).baseTypeStrings;
        } else {
            return var0.isArray() ? var0.getName().replace('.', '/') : "L" + dotToSlash(var0.getName()) + ";";
        }
    }

    private static Strings getFriendlyMethodSignature(Strings var0, Class<?>[] var1) {
        StringBuilder var2 = new StringBuilder(var0);
        var2.append('(');

        for (int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 > 0) {
                var2.append(',');
            }

            Class var4 = var1[var3];

            int var5;
            for (var5 = 0; var4.isArray(); ++var5) {
                var4 = var4.getComponentType();
            }

            var2.append(var4.getName());

            while (var5-- > 0) {
                var2.append("[]");
            }
        }

        var2.append(')');
        return var2.toString();
    }

    private static int getWordsPerType(Class<?> var0) {
        return var0 != Long.TYPE && var0 != Double.TYPE ? 1 : 2;
    }

    private static void collectCompatibleTypes(Class<?>[] var0, Class<?>[] var1, List<Class<?>> var2) {
        Class[] var3 = var0;
        int var4 = var0.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            if (!var2.contains(var6)) {
                Class[] var7 = var1;
                int var8 = var1.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    Class var10 = var7[var9];
                    if (var10.isAssignableFrom(var6)) {
                        var2.add(var6);
                        break;
                    }
                }
            }
        }

    }

    private static List<Class<?>> computeUniqueCatchList(Class<?>[] var0) {
        ArrayList var1 = new ArrayList();
        var1.add(Error.class);
        var1.add(RuntimeException.class);
        Class[] var2 = var0;
        int var3 = var0.length;

        label36:
        for (int var4 = 0; var4 < var3; ++var4) {
            Class var5 = var2[var4];
            if (var5.isAssignableFrom(Throwable.class)) {
                var1.clear();
                break;
            }

            if (Throwable.class.isAssignableFrom(var5)) {
                int var6 = 0;

                while (var6 < var1.size()) {
                    Class var7 = (Class) var1.get(var6);
                    if (var7.isAssignableFrom(var5)) {
                        continue label36;
                    }

                    if (var5.isAssignableFrom(var7)) {
                        var1.remove(var6);
                    } else {
                        ++var6;
                    }
                }

                var1.add(var5);
            }
        }

        return var1;
    }

    static {
        try {
            hashCodeMethod = Object.class.getMethod("hashCode");
            equalsMethod = Object.class.getMethod("equals", Object.class);
            toStringMethod = Object.class.getMethod("toString");
        } catch (NoSuchMethodException var1) {
            throw new NoSuchMethodError(var1.getMessage());
        }
    }

    private static class ConstantPool {
        private List<MyProxyGenerator.ConstantPool.Entry> pool;
        private Map<Object, Short> map;
        private boolean readOnly;

        private ConstantPool() {
            this.pool = new ArrayList(32);
            this.map = new HashMap(16);
            this.readOnly = false;
        }

        public short getUtf8(Strings var1) {
            if (var1 == null) {
                throw new NullPointerException();
            } else {
                return this.getValue(var1);
            }
        }

        public short getInteger(int var1) {
            return this.getValue(new Integer(var1));
        }

        public short getFloat(float var1) {
            return this.getValue(new Float(var1));
        }

        public short getClass(Strings var1) {
            short var2 = this.getUtf8(var1);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(7, var2));
        }

        public short getString(Strings var1) {
            short var2 = this.getUtf8(var1);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(8, var2));
        }

        public short getFieldRef(Strings var1, Strings var2, Strings var3) {
            short var4 = this.getClass(var1);
            short var5 = this.getNameAndType(var2, var3);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(9, var4, var5));
        }

        public short getMethodRef(Strings var1, Strings var2, Strings var3) {
            short var4 = this.getClass(var1);
            short var5 = this.getNameAndType(var2, var3);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(10, var4, var5));
        }

        public short getInterfaceMethodRef(Strings var1, Strings var2, Strings var3) {
            short var4 = this.getClass(var1);
            short var5 = this.getNameAndType(var2, var3);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(11, var4, var5));
        }

        public short getNameAndType(Strings var1, Strings var2) {
            short var3 = this.getUtf8(var1);
            short var4 = this.getUtf8(var2);
            return this.getIndirect(new MyProxyGenerator.ConstantPool.IndirectEntry(12, var3, var4));
        }

        public void setReadOnly() {
            this.readOnly = true;
        }

        public void write(OutputStream var1) throws IOException {
            DataOutputStream var2 = new DataOutputStream(var1);
            var2.writeShort(this.pool.size() + 1);
            Iterator var3 = this.pool.iterator();

            while (var3.hasNext()) {
                MyProxyGenerator.ConstantPool.Entry var4 = (MyProxyGenerator.ConstantPool.Entry) var3.next();
                var4.write(var2);
            }

        }

        private short addEntry(MyProxyGenerator.ConstantPool.Entry var1) {
            this.pool.add(var1);
            if (this.pool.size() >= 65535) {
                throw new IllegalArgumentException("constant pool size limit exceeded");
            } else {
                return (short) this.pool.size();
            }
        }

        private short getValue(Object var1) {
            Short var2 = (Short) this.map.get(var1);
            if (var2 != null) {
                return var2;
            } else if (this.readOnly) {
                throw new InternalError("late constant pool addition: " + var1);
            } else {
                short var3 = this.addEntry(new MyProxyGenerator.ConstantPool.ValueEntry(var1));
                this.map.put(var1, new Short(var3));
                return var3;
            }
        }

        private short getIndirect(MyProxyGenerator.ConstantPool.IndirectEntry var1) {
            Short var2 = (Short) this.map.get(var1);
            if (var2 != null) {
                return var2;
            } else if (this.readOnly) {
                throw new InternalError("late constant pool addition");
            } else {
                short var3 = this.addEntry(var1);
                this.map.put(var1, new Short(var3));
                return var3;
            }
        }

        private abstract static class Entry {
            private Entry() {
            }

            public abstract void write(DataOutputStream var1) throws IOException;
        }

        private static class IndirectEntry extends Entry {
            private int tag;
            private short index0;
            private short index1;

            public IndirectEntry(int var1, short var2) {
                this.tag = var1;
                this.index0 = var2;
                this.index1 = 0;
            }

            public IndirectEntry(int var1, short var2, short var3) {
                this.tag = var1;
                this.index0 = var2;
                this.index1 = var3;
            }

            public void write(DataOutputStream var1) throws IOException {
                var1.writeByte(this.tag);
                var1.writeShort(this.index0);
                if (this.tag == 9 || this.tag == 10 || this.tag == 11 || this.tag == 12) {
                    var1.writeShort(this.index1);
                }

            }

            public int hashCode() {
                return this.tag + this.index0 + this.index1;
            }

            public boolean equals(Object var1) {
                if (var1 instanceof MyProxyGenerator.ConstantPool.IndirectEntry) {
                    MyProxyGenerator.ConstantPool.IndirectEntry var2 = (MyProxyGenerator.ConstantPool.IndirectEntry) var1;
                    if (this.tag == var2.tag && this.index0 == var2.index0 && this.index1 == var2.index1) {
                        return true;
                    }
                }

                return false;
            }
        }

        private static class ValueEntry extends ConstantPool.Entry {
            private Object value;

            public ValueEntry(Object var1) {
                this.value = var1;
            }

            public void write(DataOutputStream var1) throws IOException {
                if (this.value instanceof Strings) {
                    var1.writeByte(1);
                    var1.writeUTF((Strings) this.value);
                } else if (this.value instanceof Integer) {
                    var1.writeByte(3);
                    var1.writeInt((Integer) this.value);
                } else if (this.value instanceof Float) {
                    var1.writeByte(4);
                    var1.writeFloat((Float) this.value);
                } else if (this.value instanceof Long) {
                    var1.writeByte(5);
                    var1.writeLong((Long) this.value);
                } else {
                    if (!(this.value instanceof Double)) {
                        throw new InternalError("bogus value entry: " + this.value);
                    }

                    var1.writeDouble(6.0D);
                    var1.writeDouble((Double) this.value);
                }

            }
        }
    }

    private static class ExceptionTableEntry {
        public short startPc;
        public short endPc;
        public short handlerPc;
        public short catchType;

        public ExceptionTableEntry(short var1, short var2, short var3, short var4) {
            this.startPc = var1;
            this.endPc = var2;
            this.handlerPc = var3;
            this.catchType = var4;
        }
    }

    private class FieldInfo {
        public int accessFlags;
        public Strings name;
        public Strings descriptor;

        public FieldInfo(Strings var2, Strings var3, int var4) {
            this.name = var2;
            this.descriptor = var3;
            this.accessFlags = var4;
            MyProxyGenerator.this.cp.getUtf8(var2);
            MyProxyGenerator.this.cp.getUtf8(var3);
        }

        public void write(DataOutputStream var1) throws IOException {
            var1.writeShort(this.accessFlags);
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8(this.name));
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8(this.descriptor));
            var1.writeShort(0);
        }
    }

    private class MethodInfo {
        public int accessFlags;
        public Strings name;
        public Strings descriptor;
        public short maxStack;
        public short maxLocals;
        public ByteArrayOutputStream code = new ByteArrayOutputStream();
        public List<MyProxyGenerator.ExceptionTableEntry> exceptionTable = new ArrayList();
        public short[] declaredExceptions;

        public MethodInfo(Strings var2, Strings var3, int var4) {
            this.name = var2;
            this.descriptor = var3;
            this.accessFlags = var4;
            MyProxyGenerator.this.cp.getUtf8(var2);
            MyProxyGenerator.this.cp.getUtf8(var3);
            MyProxyGenerator.this.cp.getUtf8("Code");
            MyProxyGenerator.this.cp.getUtf8("Exceptions");
        }

        public void write(DataOutputStream var1) throws IOException {
            var1.writeShort(this.accessFlags);
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8(this.name));
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8(this.descriptor));
            var1.writeShort(2);
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8("Code"));
            var1.writeInt(12 + this.code.size() + 8 * this.exceptionTable.size());
            var1.writeShort(this.maxStack);
            var1.writeShort(this.maxLocals);
            var1.writeInt(this.code.size());
            this.code.writeTo(var1);
            var1.writeShort(this.exceptionTable.size());
            Iterator var2 = this.exceptionTable.iterator();

            while (var2.hasNext()) {
                MyProxyGenerator.ExceptionTableEntry var3 = (MyProxyGenerator.ExceptionTableEntry) var2.next();
                var1.writeShort(var3.startPc);
                var1.writeShort(var3.endPc);
                var1.writeShort(var3.handlerPc);
                var1.writeShort(var3.catchType);
            }

            var1.writeShort(0);
            var1.writeShort(MyProxyGenerator.this.cp.getUtf8("Exceptions"));
            var1.writeInt(2 + 2 * this.declaredExceptions.length);
            var1.writeShort(this.declaredExceptions.length);
            short[] var6 = this.declaredExceptions;
            int var7 = var6.length;

            for (int var4 = 0; var4 < var7; ++var4) {
                short var5 = var6[var4];
                var1.writeShort(var5);
            }

        }
    }

    private static class PrimitiveTypeInfo {
        public Strings baseTypeStrings;
        public Strings wrapperClassName;
        public Strings wrapperValueOfDesc;
        public Strings unwrapMethodName;
        public Strings unwrapMethodDesc;
        private static Map<Class<?>, MyProxyGenerator.PrimitiveTypeInfo> table = new HashMap();

        private static void add(Class<?> var0, Class<?> var1) {
            table.put(var0, new MyProxyGenerator.PrimitiveTypeInfo(var0, var1));
        }

        private PrimitiveTypeInfo(Class<?> var1, Class<?> var2) {
            assert var1.isPrimitive();

            this.baseTypeStrings = Array.newInstance(var1, 0).getClass().getName().substring(1);
            this.wrapperClassName = MyProxyGenerator.dotToSlash(var2.getName());
            this.wrapperValueOfDesc = "(" + this.baseTypeStrings + ")L" + this.wrapperClassName + ";";
            this.unwrapMethodName = var1.getName() + "Value";
            this.unwrapMethodDesc = "()" + this.baseTypeStrings;
        }

        public static MyProxyGenerator.PrimitiveTypeInfo get(Class<?> var0) {
            return (MyProxyGenerator.PrimitiveTypeInfo) table.get(var0);
        }

        static {
            add(Byte.TYPE, Byte.class);
            add(Character.TYPE, Character.class);
            add(Double.TYPE, Double.class);
            add(Float.TYPE, Float.class);
            add(Integer.TYPE, Integer.class);
            add(Long.TYPE, Long.class);
            add(Short.TYPE, Short.class);
            add(Boolean.TYPE, Boolean.class);
        }
    }

    private class ProxyMethod {
        public Strings methodName;
        public Class<?>[] parameterTypes;
        public Class<?> returnType;
        public Class<?>[] exceptionTypes;
        public Class<?> fromClass;
        public Strings methodFieldName;

        private ProxyMethod(Strings var2, Class<?>[] var3, Class<?> var4, Class<?>[] var5, Class<?> var6) {
            this.methodName = var2;
            this.parameterTypes = var3;
            this.returnType = var4;
            this.exceptionTypes = var5;
            this.fromClass = var6;
            this.methodFieldName = "m" + MyProxyGenerator.this.proxyMethodCount++;
        }

        private MyProxyGenerator.MethodInfo generateMethod() throws IOException {
            Strings var1 = MyProxyGenerator.getMethodDescriptor(this.parameterTypes, this.returnType);
            MyProxyGenerator.MethodInfo var2 = MyProxyGenerator.this.new MethodInfo(this.methodName, var1, 17);
            int[] var3 = new int[this.parameterTypes.length];
            int var4 = 1;

            for (int var5 = 0; var5 < var3.length; ++var5) {
                var3[var5] = var4;
                var4 += MyProxyGenerator.getWordsPerType(this.parameterTypes[var5]);
            }

            byte var7 = 0;
            DataOutputStream var9 = new DataOutputStream(var2.code);
            MyProxyGenerator.this.code_aload(0, var9);
            var9.writeByte(180);
            var9.writeShort(MyProxyGenerator.this.cp.getFieldRef("java/lang/reflect/Proxy", "h", "Ljava/lang/reflect/InvocationHandler;"));
            MyProxyGenerator.this.code_aload(0, var9);
            var9.writeByte(178);
            var9.writeShort(MyProxyGenerator.this.cp.getFieldRef(MyProxyGenerator.dotToSlash(MyProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
            if (this.parameterTypes.length > 0) {
                MyProxyGenerator.this.code_ipush(this.parameterTypes.length, var9);
                var9.writeByte(189);
                var9.writeShort(MyProxyGenerator.this.cp.getClass("java/lang/Object"));

                for (int var10 = 0; var10 < this.parameterTypes.length; ++var10) {
                    var9.writeByte(89);
                    MyProxyGenerator.this.code_ipush(var10, var9);
                    this.codeWrapArgument(this.parameterTypes[var10], var3[var10], var9);
                    var9.writeByte(83);
                }
            } else {
                var9.writeByte(1);
            }

            var9.writeByte(185);
            var9.writeShort(MyProxyGenerator.this.cp.getInterfaceMethodRef("java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;"));
            var9.writeByte(4);
            var9.writeByte(0);
            if (this.returnType == Void.TYPE) {
                var9.writeByte(87);
                var9.writeByte(177);
            } else {
                this.codeUnwrapReturnValue(this.returnType, var9);
            }

            short var6;
            short var8 = var6 = (short) var2.code.size();
            List var13 = MyProxyGenerator.computeUniqueCatchList(this.exceptionTypes);
            if (var13.size() > 0) {
                Iterator var11 = var13.iterator();

                while (var11.hasNext()) {
                    Class var12 = (Class) var11.next();
                    var2.exceptionTable.add(new MyProxyGenerator.ExceptionTableEntry(var7, var8, var6, MyProxyGenerator.this.cp.getClass(MyProxyGenerator.dotToSlash(var12.getName()))));
                }

                var9.writeByte(191);
                var6 = (short) var2.code.size();
                var2.exceptionTable.add(new MyProxyGenerator.ExceptionTableEntry(var7, var8, var6, MyProxyGenerator.this.cp.getClass("java/lang/Throwable")));
                MyProxyGenerator.this.code_astore(var4, var9);
                var9.writeByte(187);
                var9.writeShort(MyProxyGenerator.this.cp.getClass("java/lang/reflect/UndeclaredThrowableException"));
                var9.writeByte(89);
                MyProxyGenerator.this.code_aload(var4, var9);
                var9.writeByte(183);
                var9.writeShort(MyProxyGenerator.this.cp.getMethodRef("java/lang/reflect/UndeclaredThrowableException", "<init>", "(Ljava/lang/Throwable;)V"));
                var9.writeByte(191);
            }

            if (var2.code.size() > 65535) {
                throw new IllegalArgumentException("code size limit exceeded");
            } else {
                var2.maxStack = 10;
                var2.maxLocals = (short) (var4 + 1);
                var2.declaredExceptions = new short[this.exceptionTypes.length];

                for (int var14 = 0; var14 < this.exceptionTypes.length; ++var14) {
                    var2.declaredExceptions[var14] = MyProxyGenerator.this.cp.getClass(MyProxyGenerator.dotToSlash(this.exceptionTypes[var14].getName()));
                }

                return var2;
            }
        }

        private void codeWrapArgument(Class<?> var1, int var2, DataOutputStream var3) throws IOException {
            if (var1.isPrimitive()) {
                MyProxyGenerator.PrimitiveTypeInfo var4 = MyProxyGenerator.PrimitiveTypeInfo.get(var1);
                if (var1 != Integer.TYPE && var1 != Boolean.TYPE && var1 != Byte.TYPE && var1 != Character.TYPE && var1 != Short.TYPE) {
                    if (var1 == Long.TYPE) {
                        MyProxyGenerator.this.code_lload(var2, var3);
                    } else if (var1 == Float.TYPE) {
                        MyProxyGenerator.this.code_fload(var2, var3);
                    } else {
                        if (var1 != Double.TYPE) {
                            throw new AssertionError();
                        }

                        MyProxyGenerator.this.code_dload(var2, var3);
                    }
                } else {
                    MyProxyGenerator.this.code_iload(var2, var3);
                }

                var3.writeByte(184);
                var3.writeShort(MyProxyGenerator.this.cp.getMethodRef(var4.wrapperClassName, "valueOf", var4.wrapperValueOfDesc));
            } else {
                MyProxyGenerator.this.code_aload(var2, var3);
            }

        }

        private void codeUnwrapReturnValue(Class<?> var1, DataOutputStream var2) throws IOException {
            if (var1.isPrimitive()) {
                MyProxyGenerator.PrimitiveTypeInfo var3 = MyProxyGenerator.PrimitiveTypeInfo.get(var1);
                var2.writeByte(192);
                var2.writeShort(MyProxyGenerator.this.cp.getClass(var3.wrapperClassName));
                var2.writeByte(182);
                var2.writeShort(MyProxyGenerator.this.cp.getMethodRef(var3.wrapperClassName, var3.unwrapMethodName, var3.unwrapMethodDesc));
                if (var1 != Integer.TYPE && var1 != Boolean.TYPE && var1 != Byte.TYPE && var1 != Character.TYPE && var1 != Short.TYPE) {
                    if (var1 == Long.TYPE) {
                        var2.writeByte(173);
                    } else if (var1 == Float.TYPE) {
                        var2.writeByte(174);
                    } else {
                        if (var1 != Double.TYPE) {
                            throw new AssertionError();
                        }

                        var2.writeByte(175);
                    }
                } else {
                    var2.writeByte(172);
                }
            } else {
                var2.writeByte(192);
                var2.writeShort(MyProxyGenerator.this.cp.getClass(MyProxyGenerator.dotToSlash(var1.getName())));
                var2.writeByte(176);
            }

        }

        private void codeFieldInitialization(DataOutputStream var1) throws IOException {
            MyProxyGenerator.this.codeClassForName(this.fromClass, var1);
            MyProxyGenerator.this.code_ldc(MyProxyGenerator.this.cp.getString(this.methodName), var1);
            MyProxyGenerator.this.code_ipush(this.parameterTypes.length, var1);
            var1.writeByte(189);
            var1.writeShort(MyProxyGenerator.this.cp.getClass("java/lang/Class"));

            for (int var2 = 0; var2 < this.parameterTypes.length; ++var2) {
                var1.writeByte(89);
                MyProxyGenerator.this.code_ipush(var2, var1);
                if (this.parameterTypes[var2].isPrimitive()) {
                    MyProxyGenerator.PrimitiveTypeInfo var3 = MyProxyGenerator.PrimitiveTypeInfo.get(this.parameterTypes[var2]);
                    var1.writeByte(178);
                    var1.writeShort(MyProxyGenerator.this.cp.getFieldRef(var3.wrapperClassName, "TYPE", "Ljava/lang/Class;"));
                } else {
                    MyProxyGenerator.this.codeClassForName(this.parameterTypes[var2], var1);
                }

                var1.writeByte(83);
            }

            var1.writeByte(182);
            var1.writeShort(MyProxyGenerator.this.cp.getMethodRef("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"));
            var1.writeByte(179);
            var1.writeShort(MyProxyGenerator.this.cp.getFieldRef(MyProxyGenerator.dotToSlash(MyProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
        }
    }
}

