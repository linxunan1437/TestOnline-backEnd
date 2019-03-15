package socket;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import datatype.Classes;
import datatype.User;
import staticThings.Setting;
@ServerEndpoint("/websocket")

public class MyWebSocket {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
	//用户的标识和用户所属的班级
	private static Map<MyWebSocket,User> user=new HashMap<MyWebSocket,User>();
	private static Map<MyWebSocket,Classes> classbelong=new HashMap<MyWebSocket,Classes>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	//本会话所属班级
	private Classes thisclass;
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketSet.add(this);		//加入set中
		user.put(this, new User());
		addOnlineCount();			//在线数加1
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
		for(Entry<MyWebSocket, User> item: user.entrySet())
		{
			System.out.println(String.valueOf(item.getValue().getUid()));
		}
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		webSocketSet.remove(this);	//从set中删除
		user.remove(this);
		classbelong.remove(this);
		subOnlineCount();			//在线数减1		s
		//保存当前的对话记录
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
		
	}
	
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		if(message.substring(0, 5).equals("name:")){
			//写入用户名
			user.get(this).setUsername(message.substring(5));
			System.out.println(user.get(this).getUsername()+"登录" );
			return;
		}
		if(message.substring(0, 6).equals("class:")){
			//写入班级
			Classes c=new Classes();c.setId(message.substring(6));
			classbelong.put(this, c);
			thisclass=c;
			return;
		}
		if(message.substring(0,8).equals("history:")){
			File file=new File(Setting.chatLog+this.thisclass.getId()+".log");
			if(!file.exists()){
				return;
			}
			try {
				Scanner scan=new Scanner(file);
				while(scan.hasNextLine())
					this.sendMessage(scan.nextLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}
		if(message.substring(0, 8).equals("message:")){message=message.substring(8);}
		else return;
		String msg=user.get(this).getUsername()+":" + message+"\n";
		System.out.println(msg);
		
		//对相应的班级群发消息
		for(MyWebSocket item:webSocketSet){				
			try {
				if(classbelong.get(item).getId().equals(thisclass.getId()))
				{
					item.sendMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		//写入消息记录,分班记录
		File file=new File(Setting.chatLog+this.thisclass.getId()+".log");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Writer writer = new FileWriter(file,true);
			writer.write(msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("发生错误");
		error.printStackTrace();
	}
	
	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
		//this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MyWebSocket.onlineCount++;
	}
	
	public static synchronized void subOnlineCount() {
		MyWebSocket.onlineCount--;
	}

}
