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

    private static final String[] SIZE_MULTIPLIERS = {"B", "K", "M", "G", "T"};

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
        BigDecimal value;
        for (int i = 0; i < SIZE_MULTIPLIERS.length; i++) {
            value = BigDecimal.valueOf(size).divide(BigDecimal
                    .valueOf(Math.pow(1024, i)), 3, RoundingMode.CEILING);
            if (value.compareTo(BigDecimal.valueOf(1024)) != 1) {
                return value + ""
                        + SIZE_MULTIPLIERS[i]
                        + (i > 0 ? "B" : "");
            }
        }

        return "Very big!";
    }

    //TODO: 24B, 234K, 36M, 34G, 42T
    public static long getSizeFromHumanReadable(String stringSize) {
        String pattern = "([0-9]+)([^0-9]+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(stringSize);

        Map<String, Integer> sizeTypes = new HashMap<>();
        for (int i = 0; i < SIZE_MULTIPLIERS.length; i++) {
            sizeTypes.put(SIZE_MULTIPLIERS[i], (int) Math.pow(1024, i));
        }
        if (m.find()) {
            return Long.parseLong(m.group(1)) * sizeTypes.get(m.group(2));
        } else return 0L;
    }

}
