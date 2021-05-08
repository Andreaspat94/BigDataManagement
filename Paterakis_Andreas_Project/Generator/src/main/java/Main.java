import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Integer d = null;
        Integer m = null;
        Integer l = null;
        Integer n = null;
        String k = null;
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-d")) {
                    d = Integer.parseInt(args[i + 1]);
                }
                if (args[i].equals("-n")) {
                    n = Integer.parseInt(args[i + 1]);
                }
                if (args[i].equals("-l")) {
                    l = Integer.parseInt(args[i + 1]);
                }
                if (args[i].equals("-m")) {
                    m = Integer.parseInt(args[i + 1]);
                }
                if (args[i].equals("-k")) {
                    k = args[i + 1];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("An empty parameter detected.\nPlease give a value to each of the parameters");
            System.exit(0);
        }


        GeneratorService service = new GeneratorService();
        service.generateData(k, n, d, m, l);

        System.out.println("'dataToIndex.txt' is created");

    }
}

