package com.leon.helloworld.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

@ServerEndpoint("/websocket")
public class TestWebSocket {

	@OnMessage
	public void onMessage(String message, Session session) throws IOException{
		
		// Print the client message for testing purposes
		System.out.println("Received: " + message);
		// Send the message to the client
		session.getBasicRemote().sendText("This is the server message");
		/*ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runs = engine.getRuntimeService();
		TaskService ts = engine.getTaskService();
		runs.startProcessInstanceByKey("testProcess");
		session.getBasicRemote().sendText("process started");
		System.out.println("== process started ==");
		Task task = ts.createTaskQuery().singleResult();
		session.getBasicRemote().sendText("first task is running");
		System.out.println("= first task is running =");
		
		ts.complete(task.getId());
		session.getBasicRemote().sendText("first task is ended");
		System.out.println("== first task is ended ==");
		task =ts.createTaskQuery().singleResult();
		session.getBasicRemote().sendText("next task : " + task);
		System.out.println("== next task : " + task + " ==");
		session.getBasicRemote().sendText("the process ended");*/
	}
	@OnOpen
    public void onOpen () {
        System.out.println("Client connected");
    }

    @OnClose
    public void onClose () {
    	System.out.println("Connection closed");
    }
	
}