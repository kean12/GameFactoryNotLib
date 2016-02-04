package com.game.assist.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Observable;
import java.util.Scanner;

public class UDPClientModel extends Observable implements Runnable {
	private DatagramSocket socket;

	/**
	 * Method UDPClientModel
	 * 
	 * 
	 */
	public UDPClientModel(int port) throws SocketException {

		socket = new DatagramSocket(port);
		new Thread(this).start();
	}

	/**
	 * Method main
	 * 
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) throws Exception {
		UDPClientModel client = new UDPClientModel(8001);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String str = scanner.nextLine();
			client.send(str, new InetSocketAddress("127.0.0.1", 8001));
			System.out.println(client.receive());
		}

	}

	public void close() {
		socket.close();
	}

	public void send(Serializable object, SocketAddress address) throws IOException {
		byte[] buf = getBytes(object);
		byte[] length = new Integer(buf.length).toString().getBytes();
		DatagramPacket bufLength = new DatagramPacket(length, length.length, address);
		DatagramPacket message = new DatagramPacket(buf, buf.length, address);
		socket.send(bufLength);
		socket.send(message);
	}

	protected Object receive() throws Exception {
		byte[] buf = new byte[16];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		socket.receive(dp);
		int length = new Integer(new String(buf, 0, dp.getLength()));
		buf = new byte[length];
		dp = new DatagramPacket(buf, buf.length);
		socket.receive(dp);
		return getObject(buf);
	}

	protected byte[] getBytes(Serializable object) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		objectOut.writeObject(object);
		objectOut.close();
		return out.toByteArray();
	}

	protected Object getObject(byte[] buf) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		ObjectInputStream objectIn = new ObjectInputStream(in);
		Object object = objectIn.readObject();
		objectIn.close();
		return object;
	}

	/**
	 * Method run
	 * 
	 * 
	 */
	public void run() {
		while (true) {
			try {
				Object object = receive();
				setChanged();
				notifyObservers(object);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
