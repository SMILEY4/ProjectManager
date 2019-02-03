//package com.ruegnerlukas.taskmanager.architecture;
//
//import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
//import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
//import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
//
//import java.util.Random;
//
//public class ArchitectureTest {
//
//
//	public static void main(String[] args) {
//		System.out.println("START");
//
//
//		// sync getter
//		SyncRequest request = new SyncRequest();
//		getStuff("somename", request);
//		Response syncResponse = request.getResponse();
//		System.out.println("Response A: " + syncResponse);
//
//
//		// async getter
//		getStuff("somename", new Request() {
//			@Override
//			public void onResponse(Response response) {
//				System.out.println("Response B: " + response.state + " - " + response.message);
//			}
//		});
//
//
//		// async setter
//		EventManager.registerListener(new EventListener() {
//			@Override
//			public void onEvent(Event event) {
//				System.out.println("Event: " + event);
//			}
//		}, Event.class);
//		doStuff("somestring");
//
//
//
//
//		System.out.println("END");
//	}
//
//
//
//
//
//
//
//	static void doStuff(String str) {
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(new Random().nextInt(100)*10);
//					System.out.println("do stuff: " + str + "  ");
//					Thread.sleep(new Random().nextInt(100)*10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
////				EventManager.fireEvent(new Event(ArchitectureTest.class) );
//
//			}
//		});
//		t.start();
//
//	}
//
//
//
//
//
//	static void getStuff(String str, Request request) {
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(new Random().nextInt(100)*10);
//					System.out.println("get stuff: " + str + "  " + request);
//					Thread.sleep(new Random().nextInt(100)*10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//				Response response = new Response(Response.State.SUCCESS, "some get message");
//				request.onResponse(response);
//
//			}
//		});
//		t.start();
//
//
//	}
//
//
//
//}
