package util;

import sun.util.spi.XmlPropertiesProvider;

import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class Properties extends Hashtable<Object, Object> {
    /**
     * use serialVersionUID from JDK 1.1.X for interoperability
     */
    private static final long serialVersionUID = 4112578634029874840L;

    /**
     * A property list that contains default values for any keys not
     * found in this property list.
     *
     * @serial
     */
    protected Properties defaults;

    /**
     * Creates an empty property list with no default values.
     */
    public Properties() {
        this(null);
    }

    /**
     * Creates an empty property list with the specified defaults.
     *
     * @param defaults the defaults.
     */
    public Properties(Properties defaults) {
        this.defaults = defaults;
    }

    /**
     * Calls the <tt>Hashtable</tt> method {@code put}. Provided for
     * parallelism with the <tt>getProperty</tt> method. Enforces use of
     * strings for property keys and values. The value returned is the
     * result of the <tt>Hashtable</tt> call to {@code put}.
     *
     * @param key   the key to be placed into this property list.
     * @param value the value corresponding to <tt>key</tt>.
     * @return the previous value of the specified key in this property
     * list, or {@code null} if it did not have one.
     * @see #getProperty
     * @since 1.2
     */
    public synchronized Object setProperty(String key, String value) {
        return put(key, value);
    }


    public synchronized void load(Reader reader) throws IOException {
        load0(new LineReader(reader));
    }


    public synchronized void load(InputStream inStream) throws IOException {
        load0(new LineReader(inStream));
    }

    private void load0(LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];
        int limit;
        int keyLen;
        int valueStart;
        char c;
        boolean hasSep;
        boolean precedingBackslash;

        while ((limit = lr.readLine()) >= 0) {
            c = 0;
            keyLen = 0;
            valueStart = limit;
            hasSep = false;

            //System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
            precedingBackslash = false;
            while (keyLen < limit) {
                c = lr.lineBuf[keyLen];
                //need check if escaped.
                if ((c == '=' || c == ':') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                } else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }
                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
                keyLen++;
            }
            while (valueStart < limit) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' && c != '\f') {
                    if (!hasSep && (c == '=' || c == ':')) {
                        hasSep = true;
                    } else {
                        break;
                    }
                }
                valueStart++;
            }
            String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            put(key, value);
        }
    }

    /* Read in a "logical line" from an InputStream/Reader, skip all comment
     * and blank lines and filter out those leading whitespace characters
     * (\u0020, \u0009 and \u000c) from the beginning of a "natural line".
     * Method returns the char length of the "logical line" and stores
     * the line in "lineBuf".
     */
    class LineReader {
        public LineReader(InputStream inStream) {
            this.inStream = inStream;
            inByteBuf = new byte[8192];
        }

        public LineReader(Reader reader) {
            this.reader = reader;
            inCharBuf = new char[8192];
        }

        byte[] inByteBuf;
        char[] inCharBuf;
        char[] lineBuf = new char[1024];
        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        Reader reader;

        int readLine() throws IOException {
            int len = 0;
            char c = 0;

            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;

            while (true) {
                if (inOff >= inLimit) {
                    inLimit = (inStream == null) ? reader.read(inCharBuf)
                            : inStream.read(inByteBuf);
                    inOff = 0;
                    if (inLimit <= 0) {
                        if (len == 0 || isCommentLine) {
                            return -1;
                        }
                        if (precedingBackslash) {
                            len--;
                        }
                        return len;
                    }
                }
                if (inStream != null) {
                    //The line below is equivalent to calling a
                    //ISO8859-1 decoder.
                    c = (char) (0xff & inByteBuf[inOff++]);
                } else {
                    c = inCharBuf[inOff++];
                }
                if (skipLF) {
                    skipLF = false;
                    if (c == '\n') {
                        continue;
                    }
                }
                if (skipWhiteSpace) {
                    if (c == ' ' || c == '\t' || c == '\f') {
                        continue;
                    }
                    if (!appendedLineBegin && (c == '\r' || c == '\n')) {
                        continue;
                    }
                    skipWhiteSpace = false;
                    appendedLineBegin = false;
                }
                if (isNewLine) {
                    isNewLine = false;
                    if (c == '#' || c == '!') {
                        isCommentLine = true;
                        continue;
                    }
                }

                if (c != '\n' && c != '\r') {
                    lineBuf[len++] = c;
                    if (len == lineBuf.length) {
                        int newLength = lineBuf.length * 2;
                        if (newLength < 0) {
                            newLength = Integer.MAX_VALUE;
                        }
                        char[] buf = new char[newLength];
                        System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
                        lineBuf = buf;
                    }
                    //flip the preceding backslash flag
                    if (c == '\\') {
                        precedingBackslash = !precedingBackslash;
                    } else {
                        precedingBackslash = false;
                    }
                } else {
                    // reached EOL
                    if (isCommentLine || len == 0) {
                        isCommentLine = false;
                        isNewLine = true;
                        skipWhiteSpace = true;
                        len = 0;
                        continue;
                    }
                    if (inOff >= inLimit) {
                        inLimit = (inStream == null)
                                ? reader.read(inCharBuf)
                                : inStream.read(inByteBuf);
                        inOff = 0;
                        if (inLimit <= 0) {
                            if (precedingBackslash) {
                                len--;
                            }
                            return len;
                        }
                    }
                    if (precedingBackslash) {
                        len -= 1;
                        //skip the leading whitespace characters in following line
                        skipWhiteSpace = true;
                        appendedLineBegin = true;
                        precedingBackslash = false;
                        if (c == '\r') {
                            skipLF = true;
                        }
                    } else {
                        return len;
                    }
                }
            }
        }
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     */
    private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx and escapes
     * special characters with a preceding slash
     */
    private String saveConvert(String theString,
                               boolean escapeSpace,
                               boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    private static void writeComments(BufferedWriter bw, String comments)
            throws IOException {
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        char[] uu = new char[6];
        uu[0] = '\\';
        uu[1] = 'u';
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));
                if (c > '\u00ff') {
                    uu[2] = toHex((c >> 12) & 0xf);
                    uu[3] = toHex((c >> 8) & 0xf);
                    uu[4] = toHex((c >> 4) & 0xf);
                    uu[5] = toHex(c & 0xf);
                    bw.write(new String(uu));
                } else {
                    bw.newLine();
                    if (c == '\r' &&
                            current != len - 1 &&
                            comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1 ||
                            (comments.charAt(current + 1) != '#' &&
                                    comments.charAt(current + 1) != '!'))
                        bw.write("#");
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current)
            bw.write(comments.substring(last, current));
        bw.newLine();
    }

    /**
     * Calls the {@code store(OutputStream out, String comments)} method
     * and suppresses IOExceptions that were thrown.
     *
     * @param out      an output stream.
     * @param comments a description of the property list.
     * @throws ClassCastException if this {@code Properties} object
     *                            contains any keys or values that are not
     *                            {@code Strings}.
     * @deprecated This method does not throw an IOException if an I/O error
     * occurs while saving the property list.  The preferred way to save a
     * properties list is via the {@code store(OutputStream out,
     * String comments)} method or the
     * {@code storeToXML(OutputStream os, String comment)} method.
     */
    @Deprecated
    public void save(OutputStream out, String comments) {
        try {
            store(out, comments);
        } catch (IOException e) {
        }
    }

    /**
     * Writes this property list (key and element pairs) in this
     * {@code Properties} table to the output character stream in a
     * format suitable for using the {@link #load(java.io.Reader) load(Reader)}
     * method.
     * <p>
     * Properties from the defaults table of this {@code Properties}
     * table (if any) are <i>not</i> written out by this method.
     * <p>
     * If the comments argument is not null, then an ASCII {@code #}
     * character, the comments string, and a line separator are first written
     * to the output stream. Thus, the {@code comments} can serve as an
     * identifying comment. Any one of a line feed ('\n'), a carriage
     * return ('\r'), or a carriage return followed immediately by a line feed
     * in comments is replaced by a line separator generated by the {@code Writer}
     * and if the next character in comments is not character {@code #} or
     * character {@code !} then an ASCII {@code #} is written out
     * after that line separator.
     * <p>
     * Next, a comment line is always written, consisting of an ASCII
     * {@code #} character, the current date and time (as if produced
     * by the {@code toString} method of {@code Date} for the
     * current time), and a line separator as generated by the {@code Writer}.
     * <p>
     * Then every entry in this {@code Properties} table is
     * written out, one per line. For each entry the key string is
     * written, then an ASCII {@code =}, then the associated
     * element string. For the key, all space characters are
     * written with a preceding {@code \} character.  For the
     * element, leading space characters, but not embedded or trailing
     * space characters, are written with a preceding {@code \}
     * character. The key and element characters {@code #},
     * {@code !}, {@code =}, and {@code :} are written
     * with a preceding backslash to ensure that they are properly loaded.
     * <p>
     * After the entries have been written, the output stream is flushed.
     * The output stream remains open after this method returns.
     * <p>
     *
     * @param writer   an output character stream writer.
     * @param comments a description of the property list.
     * @throws IOException          if writing this property list to the specified
     *                              output stream throws an <tt>IOException</tt>.
     * @throws ClassCastException   if this {@code Properties} object
     *                              contains any keys or values that are not {@code Strings}.
     * @throws NullPointerException if {@code writer} is null.
     * @since 1.6
     */
    public void store(Writer writer, String comments)
            throws IOException {
        store0((writer instanceof BufferedWriter) ? (BufferedWriter) writer
                        : new BufferedWriter(writer),
                comments,
                false);
    }

    /**
     * Writes this property list (key and element pairs) in this
     * {@code Properties} table to the output stream in a format suitable
     * for loading into a {@code Properties} table using the
     * {@link #load(InputStream) load(InputStream)} method.
     * <p>
     * Properties from the defaults table of this {@code Properties}
     * table (if any) are <i>not</i> written out by this method.
     * <p>
     * This method outputs the comments, properties keys and values in
     * the same format as specified in
     * {@link #store(java.io.Writer, java.lang.String) store(Writer)},
     * with the following differences:
     * <ul>
     * <li>The stream is written using the ISO 8859-1 character encoding.
     *
     * <li>Characters not in Latin-1 in the comments are written as
     * {@code \u005Cu}<i>xxxx</i> for their appropriate unicode
     * hexadecimal value <i>xxxx</i>.
     *
     * <li>Characters less than {@code \u005Cu0020} and characters greater
     * than {@code \u005Cu007E} in property keys or values are written
     * as {@code \u005Cu}<i>xxxx</i> for the appropriate hexadecimal
     * value <i>xxxx</i>.
     * </ul>
     * <p>
     * After the entries have been written, the output stream is flushed.
     * The output stream remains open after this method returns.
     * <p>
     *
     * @param out      an output stream.
     * @param comments a description of the property list.
     * @throws IOException          if writing this property list to the specified
     *                              output stream throws an <tt>IOException</tt>.
     * @throws ClassCastException   if this {@code Properties} object
     *                              contains any keys or values that are not {@code Strings}.
     * @throws NullPointerException if {@code out} is null.
     * @since 1.2
     */
    public void store(OutputStream out, String comments)
            throws IOException {
        store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
                comments,
                true);
    }

    private void store0(BufferedWriter bw, String comments, boolean escUnicode)
            throws IOException {
        if (comments != null) {
            writeComments(bw, comments);
        }
        bw.write("#" + new Date().toString());
        bw.newLine();
        synchronized (this) {
            for (Enumeration<?> e = keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                String val = (String) get(key);
                key = saveConvert(key, true, escUnicode);
                /* No need to escape embedded and trailing spaces for value, hence
                 * pass false to flag.
                 */
                val = saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }


    public synchronized void loadFromXML(InputStream in)
            throws IOException, InvalidPropertiesFormatException {
        XmlSupport.load(this, Objects.requireNonNull(in));
        in.close();
    }


    public void storeToXML(OutputStream os, String comment)
            throws IOException {
        storeToXML(os, comment, "UTF-8");
    }


    public void storeToXML(OutputStream os, String comment, String encoding)
            throws IOException {
        XmlSupport.save(this, Objects.requireNonNull(os), comment,
                Objects.requireNonNull(encoding));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * {@code null} if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     * @see #setProperty
     * @see #defaults
     */
    public String getProperty(String key) {
        Object oval = super.get(key);
        String sval = (oval instanceof String) ? (String) oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see #setProperty
     * @see #defaults
     */
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    /**
     * Returns an enumeration of all the keys in this property list,
     * including distinct keys in the default property list if a key
     * of the same name has not already been found from the main
     * properties list.
     *
     * @return an enumeration of all the keys in this property list, including
     * the keys in the default property list.
     * @throws ClassCastException if any key in this property list
     *                            is not a string.
     * @see java.util.Enumeration
     * @see java.util.Properties#defaults
     * @see #stringPropertyNames
     */
    public Enumeration<?> propertyNames() {
        Hashtable<String, Object> h = new Hashtable<>();
        enumerate(h);
        return h.keys();
    }

    /**
     * Returns a set of keys in this property list where
     * the key and its corresponding value are strings,
     * including distinct keys in the default property list if a key
     * of the same name has not already been found from the main
     * properties list.  Properties whose key or value is not
     * of type <tt>String</tt> are omitted.
     * <p>
     * The returned set is not backed by the <tt>Properties</tt> object.
     * Changes to this <tt>Properties</tt> are not reflected in the set,
     * or vice versa.
     *
     * @return a set of keys in this property list where
     * the key and its corresponding value are strings,
     * including the keys in the default property list.
     * @see java.util.Properties#defaults
     * @since 1.6
     */
    public Set<String> stringPropertyNames() {
        Hashtable<String, String> h = new Hashtable<>();
        enumerateStringProperties(h);
        return h.keySet();
    }

    /**
     * Prints this property list out to the specified output stream.
     * This method is useful for debugging.
     *
     * @param out an output stream.
     * @throws ClassCastException if any key in this property list
     *                            is not a string.
     */
    public void list(PrintStream out) {
        out.println("-- listing properties --");
        Hashtable<String, Object> h = new Hashtable<>();
        enumerate(h);
        for (Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            String val = (String) h.get(key);
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            out.println(key + "=" + val);
        }
    }

    /**
     * Prints this property list out to the specified output stream.
     * This method is useful for debugging.
     *
     * @param out an output stream.
     * @throws ClassCastException if any key in this property list
     *                            is not a string.
     * @since JDK1.1
     */
    /*
     * Rather than use an anonymous inner class to share common code, this
     * method is duplicated in order to ensure that a non-1.1 compiler can
     * compile this file.
     */
    public void list(PrintWriter out) {
        out.println("-- listing properties --");
        Hashtable<String, Object> h = new Hashtable<>();
        enumerate(h);
        for (Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            String val = (String) h.get(key);
            if (val.length() > 40) {
                val = val.substring(0, 37) + "...";
            }
            out.println(key + "=" + val);
        }
    }

    /**
     * Enumerates all key/value pairs in the specified hashtable.
     *
     * @param h the hashtable
     * @throws ClassCastException if any of the property keys
     *                            is not of String type.
     */
    private synchronized void enumerate(Hashtable<String, Object> h) {
        if (defaults != null) {
            defaults.enumerate(h);
        }
        for (Enumeration<?> e = keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            h.put(key, get(key));
        }
    }

    /**
     * Enumerates all key/value pairs in the specified hashtable
     * and omits the property if the key or value is not a string.
     *
     * @param h the hashtable
     */
    private synchronized void enumerateStringProperties(Hashtable<String, String> h) {
        if (defaults != null) {
            defaults.enumerateStringProperties(h);
        }
        for (Enumeration<?> e = keys(); e.hasMoreElements(); ) {
            Object k = e.nextElement();
            Object v = get(k);
            if (k instanceof String && v instanceof String) {
                h.put((String) k, (String) v);
            }
        }
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /**
     * A table of hex digits
     */
    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };


    private static class XmlSupport {

        private static XmlPropertiesProvider loadProviderFromProperty(ClassLoader cl) {
            String cn = System.getProperty("sun.util.spi.XmlPropertiesProvider");
            if (cn == null)
                return null;
            try {
                Class<?> c = Class.forName(cn, true, cl);
                return (XmlPropertiesProvider) c.newInstance();
            } catch (ClassNotFoundException |
                    IllegalAccessException |
                    InstantiationException x) {
                throw new ServiceConfigurationError(null, x);
            }
        }

        private static XmlPropertiesProvider loadProviderAsService(ClassLoader cl) {
            Iterator<XmlPropertiesProvider> iterator =
                    ServiceLoader.load(XmlPropertiesProvider.class, cl).iterator();
            return iterator.hasNext() ? iterator.next() : null;
        }

        private static XmlPropertiesProvider loadProvider() {
            return AccessController.doPrivileged(
                    new PrivilegedAction<XmlPropertiesProvider>() {
                        public XmlPropertiesProvider run() {
                            ClassLoader cl = ClassLoader.getSystemClassLoader();
                            XmlPropertiesProvider provider = loadProviderFromProperty(cl);
                            if (provider != null)
                                return provider;
                            provider = loadProviderAsService(cl);
                            if (provider != null)
                                return provider;
                            return new jdk.internal.util.xml.BasicXmlPropertiesProvider();
                        }
                    });
        }

        private static final XmlPropertiesProvider PROVIDER = loadProvider();

        static void load(Properties props, InputStream in)
                throws IOException, InvalidPropertiesFormatException {
//            PROVIDER.load(props, in);
        }

        static void save(Properties props, OutputStream os, String comment,
                         String encoding)
                throws IOException {
//            PROVIDER.store(props, os, comment, encoding);
        }
    }
}
