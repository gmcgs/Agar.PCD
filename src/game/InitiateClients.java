package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class InitiateClients {
    public static void main(String[] args) throws IOException {
        System.out.println("How many players will play? (1/2)");
        BufferedReader numPlayers = new BufferedReader(new InputStreamReader(System.in));
        String num = numPlayers.readLine();
        if (Objects.equals(num, "2")) {
            new Thread(() -> {
                Server server = new Server();
                Server.init();
            }).start();
            new Thread(() -> {
                try {
                    sleep(1000);
                    Client client1 = new Client();
                    client1.start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    sleep(1000);
                    Client client2 = new Client();
                    client2.start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else if (Objects.equals(num, "1")) {
            new Thread(() -> {
                Server server = new Server();
                Server.init();
            }).start();
            new Thread(() -> {
                try {
                    sleep(1000);
                    Client client1 = new Client();
                    client1.start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            System.out.println("Invalid number of players");
        }
    }
}
