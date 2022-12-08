package client;

public class SimpleClient extends Thread{

    private ClientApp client;

    public SimpleClient(ClientApp client) {
        this.client = client;
    }
}
