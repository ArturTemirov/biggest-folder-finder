import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    private static final BigDecimal KB = new BigDecimal(1024);
    private static final BigDecimal MB = BigDecimal.valueOf(Math.pow(KB.longValue(), 2));
    private static final BigDecimal GB = BigDecimal.valueOf(Math.pow(KB.longValue(), 3));
    private static final BigDecimal TB = BigDecimal.valueOf(Math.pow(KB.longValue(), 4));

    public static void main(String[] args) {

        String folderPath = "D:/The BAT!";
        File file = new File(folderPath);

        long start = System.currentTimeMillis();

        FolderSizeCalculator calculator = new FolderSizeCalculator(file);
        ForkJoinPool pool = new ForkJoinPool();
        long size = pool.invoke(calculator);
        System.out.println(size);

        long duration = System.currentTimeMillis() - start;
        System.out.println(duration + " ms");


        System.out.println(getHumanReadableSize(size));
        System.out.println(getSizeFromHumanReadable("555G"));

    }
    

    //TODO: 24B, 234Kb, 36Mb, 34Gb, 42Tb
    public static String getHumanReadableSize(long size) {
        StringBuilder number = new StringBuilder();
        BigDecimal finalSize;
        if (size < KB.longValue()) {
            number.append(size).append(" B");
        } else if (size < MB.longValue()) {
            finalSize = BigDecimal.valueOf(size).divide(KB, 3, RoundingMode.CEILING);
            number.append(finalSize).append(" KB");
        } else if (size < GB.longValue()) {
            finalSize = BigDecimal.valueOf(size).divide(MB, 3, RoundingMode.CEILING);
            number.append(finalSize).append(" MB");
        } else if (size < TB.longValue()) {
            finalSize = BigDecimal.valueOf(size).divide(GB, 3, RoundingMode.CEILING);
            number.append(finalSize).append(" GB");
        } else {
            finalSize = BigDecimal.valueOf(size).divide(TB, 3, RoundingMode.CEILING);
            number.append(finalSize).append(" TB");
        }
        return number.toString();

//        Map<String, Integer> sizeValues = new HashMap<>();
//        String[] multipliers = {"B", "KB", "MB", "GB", "TB"};
//        for (int i = 0; i < multipliers.length; i++) {
//            sizeValues.put(multipliers[i], (int) Math.pow(1024, i));
//        }
//
//        for (Map.Entry<String, Integer> entry : sizeValues.entrySet()) {
//            Integer temp = entry.getValue();
//            if (size < temp) {
//
//            }
//        }
//
//
//        return "";
    }

    //TODO: 24B, 234K, 36M, 34G, 42T
    public static long getSizeFromHumanReadable(String stringSize) {
        String pattern = "([0-9]+)([^0-9]+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(stringSize);
        String[] multipliers = {"B", "K", "M", "G", "T"};
        Map<String, Integer> sizeTypes = new HashMap<>();
        for (int i = 0; i < multipliers.length; i++) {
            sizeTypes.put(multipliers[i], (int) Math.pow(1024, i));
        }
        if (m.find()) {
            return Long.parseLong(m.group(1)) * sizeTypes.get(m.group(2));
        } else return 0L;
    }

}
