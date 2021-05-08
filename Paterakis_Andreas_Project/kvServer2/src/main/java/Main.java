public class Main {

    public static void main(String[] args) throws Exception {
        Integer p = null;
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    p = Integer.parseInt(args[i + 1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No parameter -p specified. Try again");
            System.exit(0);
        }

        if (p == null) {
            System.out.println("No parameter -p specified. Try again");
            System.exit(0);
        } else {
            System.out.println("Activating server...");
        }
        Server server = new Server();
        server.start(p);

    }
}
