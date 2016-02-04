package com.game.assist.task;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Socket发送器 用于将放入队列的信息发送入套接字通道之中。
 */
@SuppressWarnings("unchecked")
public class Sender extends Thread {
	private BlockingQueue queue;
	private ObjectOutputStream out;

	public Sender(Socket socket, BlockingQueue queue)
			throws java.io.IOException {
		this.queue = queue;
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	public void run() {
		while (true) {
			try {
				Object object = queue.take();
				out.writeObject(object);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			} catch (java.io.IOException e) {
				break;
			}
		}
	}

}
