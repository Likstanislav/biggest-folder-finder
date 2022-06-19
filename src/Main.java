import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        System.out.println(getHumanReadableSize(1073741824));
        System.out.println(getSizeFromHumanReadamle("235T"));
        String folderPath = "C:/Users/Станислав/Desktop/Мои сайты";
        File file = new File(folderPath);
        long start = System.currentTimeMillis();
        FolderSizeCalculator calculator = new FolderSizeCalculator(file);

        ForkJoinPool pool = new ForkJoinPool();
        long size = pool.invoke(calculator);
        System.out.println(size);
        long duration = (System.currentTimeMillis() - start);
        System.out.println(duration + " ms");
    }

    //TODO: B, Kb, Mb, Gb, Tb
    public static String getHumanReadableSize(long size) {
        String hrSize;
        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);
        DecimalFormat dec = new DecimalFormat("0.00");
        if (t >= 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g >= 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m >= 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k >= 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }
        return hrSize;
    }

    public static long getSizeFromHumanReadamle(String size) {
        HashMap<Character, Integer> char2multiplier = getMultipliers();
        char sizeFactor = size
                .replaceAll("[0-9\\s+]+", "")
                .charAt(0);
        int multipliers = char2multiplier.get(sizeFactor);
        long length = multipliers * Long.valueOf(size.replaceAll("[^0-9]", ""));
        return length;
    }

    private static HashMap<Character, Integer> getMultipliers() {
        char[] multipliers = {'B', 'K', 'M', 'G', 'T'};
        HashMap<Character, Integer> charMultipliers = new HashMap<>();
        for (int i = 0; i < multipliers.length; i++) {
            charMultipliers.put(multipliers[i], (int) Math.pow(1024, i));
        }
        return charMultipliers;
    }

    public static long getFolderSize(File folder)
    {
        if(folder.isFile()) {
            return folder.length();
        }
        long sum = 0;
        File[] files = folder.listFiles();
        for(File file : files) {
            sum +=getFolderSize(file);
        }
        return sum;
}
