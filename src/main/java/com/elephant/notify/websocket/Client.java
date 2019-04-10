package com.elephant.notify.websocket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.elephant.notify.service.NotifyService;
import com.jfinal.kit.PropKit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static okhttp3.ws.WebSocket.BINARY;
import static okhttp3.ws.WebSocket.TEXT;
/**
 * clark
 * <p>
 * 4/11/19
 */
public final class Client implements WebSocketListener {
    private static int heartbeat = 30;
    private ExecutorService writeExecutor;
    private OkHttpClient client;
    private WebSocketCall call;
    private boolean cleaned = false;
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public Client(ExecutorService writeExecutor){
        this.writeExecutor = writeExecutor;
    }
    public void clean(){
        call.cancel();
        client.dispatcher().executorService().shutdown();
        writeExecutor.shutdown();
        cleaned = true;
    }

    private void run() throws IOException {
        client = new OkHttpClient.Builder()
                .readTimeout(120,  TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(PropKit.get("websocket_endpoint"))
                .build();
        call = WebSocketCall.create(client, request);
        call.enqueue(this);
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response){
        writeExecutor.execute(new Runnable() {
            @Override
            public void run(){
                try {
                    while(true){
                        webSocket.sendMessage(RequestBody.create(BINARY, "{\"action\":\"heartbeat\"}"));
                        Thread.sleep(heartbeat * 1000);
                    }
                } catch (Exception e) {
                    logger.error("Unable to send messages: " + e.getMessage());
                    clean();
                }
            }
        });
    }

    private NotifyService service = new NotifyService();

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        String msg = "";
        if (message.contentType() == TEXT) {
            msg = message.string();
        } else {
            msg = message.source().readByteString().hex();
        }
        service.handleMsg(msg);
        message.close();
    }

    @Override
    public void onPong(Buffer payload) {
        logger.info("onPong: " + payload.readUtf8());
    }

    @Override
    public void onClose(int code, String reason) {
        logger.info("onClose:" + "CLOSE: " + code + " " + reason);
        clean();
    }

    @Override
    public void onFailure(IOException e, Response response) {
        logger.info("onFailure:" + e.getMessage());
        clean();
    }

    public static void connect() throws Exception {
        Client client = null;
        while (true) {
            try{
                if(client == null || client.cleaned){
                    client = new Client(Executors.newSingleThreadExecutor());
                    client.run();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                client.clean();
            }
            Thread.sleep(heartbeat * 1000);
        }
    }
}