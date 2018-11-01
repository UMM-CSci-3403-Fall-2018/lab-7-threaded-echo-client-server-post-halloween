package echoserver;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private class ProccessClientThread implements Runnable{
	    InputStream inputStream;
	    OutputStream outputStream;

	    public ProccessClientThread(Socket socket) {
	        try {
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        public void run() {
            int b;
            try {
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

	private void start() throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread clientThread = new Thread(new ProccessClientThread(socket));
            clientThread.start();
		}
	}
}