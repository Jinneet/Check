/*    */package utils;

/*    */
/*    */import java.security.MessageDigest;

/*    */
/*    */
/*    */public class MD5Utils
/*    */{

    /*    */public static String md5(String string)
    /*    */{
        /* 13 */char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        /*    */try
        /*    */{
            /* 16 */byte[] bytes = string.getBytes();
            /* 17 */MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            /* 18 */messageDigest.update(bytes);
            /* 19 */byte[] updateBytes = messageDigest.digest();
            /* 20 */int len = updateBytes.length;
            /* 21 */char[] myChar = new char[len * 2];
            /* 22 */int k = 0;
            /* 23 */for (int i = 0; i < len; i++) {
                /* 24 */byte byte0 = updateBytes[i];
                /* 25 */myChar[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                /* 26 */myChar[(k++)] = hexDigits[(byte0 & 0xF)];
                /*    */}
            /* 28 */return new String(myChar);
        } catch (Exception e) {
            /*    */}
        /* 30 */return null;
        /*    */}

    /*    */
    /*    */public static void main(String[] args)
    /*    */{
        /* 35 */System.out.println(md5("0125qa01test"));
        /*    */}
    /*    */
}
