package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient{
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoClient client = new EchoClient();
		client.start();
	}

    private class KeyboardReader implements Runnable {
	    OutputStream stream;

	    public KeyboardReader(OutputStream stream) {
	       this.stream = stream;
        }

	    public void run(){
	        int readByte;
	        try {
                while ((readByte = System.in.read()) != -1) {
                    stream.write(readByte);
                }
            } catch(IOException e) {
                System.out.println("Something went wrong, good luck");
                System.out.println(e);
            }
        }

    }

    private class ScreenWriter implements Runnable {
	    InputStream stream;

	    public ScreenWriter(InputStream stream) {
            this.stream = stream;
        }

	    public void run(){
	        int inByte;
	        try {
                while ((inByte = stream.read()) != -1) {
                    System.out.write(inByte);
                }
            } catch (IOException e) {
                System.out.println("Whoops");
                System.out.println(e);
            }

        }

    }

	private void start() throws IOException, InterruptedException {
		Socket socket = new Socket("localhost", PORT_NUMBER);

		InputStream socketInputStream = socket.getInputStream();
		Thread inputThread = new Thread(new ScreenWriter(socketInputStream));

		OutputStream socketOutputStream = socket.getOutputStream();
		Thread outputThread = new Thread(new KeyboardReader(socketOutputStream));

		outputThread.start();
		inputThread.start();

		outputThread.join();
        socket.shutdownOutput();


        inputThread.join();
        System.out.flush();
        socket.shutdownInput();

    }
}