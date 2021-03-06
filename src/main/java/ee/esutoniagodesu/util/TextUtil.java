package ee.esutoniagodesu.util;

/**
 * http://www.mkyong.com/java/how-to-convert-hex-to-ascii-in-java/
 */
public final class TextUtil {

    public static String unicodeHexOf(char c) {

        return hexValueOf(Integer.toHexString(c));
    }

    public static String hexValueOf(char c) {
        return String.format("%04x", (int) c);
    }

    public static String hexValueOf(String str) {

        char[] chars = str.toCharArray();

        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }

        return hex.toString();
    }

    public static String stringValueOfHex(String hex) {

        StringBuilder sb = new StringBuilder();
        //StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            //temp.append(decimal);
        }
        //System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }
}
