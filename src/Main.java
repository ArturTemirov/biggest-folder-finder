import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ForkJoinPool;

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

        System.out.println(KB + " KB");
        System.out.println(MB + " MB");
        System.out.println(GB + " GB");
        System.out.println(TB + " TB");

        System.out.println(getHumanReadableSize(size));
    }

    public static long getFolderSize(File folder) {
        if (folder.isFile()) {
            return folder.length();
        }
        long sum = 0;
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            sum += getFolderSize(file);
        }
        return sum;
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
    }

    //TODO: 24B, 234Kb, 36Mb, 34Gb, 42Tb
    //TODO: 24B, 234K, 36M, 34G, 42T
    public static long getSizeFromHumanReadable(String size) {
        return 0L;
    }

}
