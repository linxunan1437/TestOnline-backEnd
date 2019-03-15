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
	//��̬������������¼��ǰ������������Ӧ�ð�����Ƴ��̰߳�ȫ�ġ�
	private static int onlineCount = 0;
	//concurrent�����̰߳�ȫSet���������ÿ���ͻ��˶�Ӧ��MyWebSocket������Ҫʵ�ַ�����뵥һ�ͻ���ͨ�ŵĻ�������ʹ��Map����ţ�����Key����Ϊ�û���ʶ
	private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
	//�û��ı�ʶ���û������İ༶
	private static Map<MyWebSocket,User> user=new HashMap<MyWebSocket,User>();
	private static Map<MyWebSocket,Classes> classbelong=new HashMap<MyWebSocket,Classes>();
	//��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
	private Session session;
	//���Ự�����༶
	private Classes thisclass;
	/**
	 * ���ӽ����ɹ����õķ���
	 * @param session  ��ѡ�Ĳ�����sessionΪ��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketSet.add(this);		//����set��
		user.put(this, new User());
		addOnlineCount();			//��������1
		System.out.println("�������Ӽ��룡��ǰ��������Ϊ" + getOnlineCount());
		for(Entry<MyWebSocket, User> item: user.entrySet())
		{
			System.out.println(String.valueOf(item.getValue().getUid()));
		}
	}
	
	/**
	 * ���ӹرյ��õķ���
	 */
	@OnClose
	public void onClose(){
		webSocketSet.remove(this);	//��set��ɾ��
		user.remove(this);
		classbelong.remove(this);
		subOnlineCount();			//��������1		s
		//���浱ǰ�ĶԻ���¼
		System.out.println("��һ���ӹرգ���ǰ��������Ϊ" + getOnlineCount());
		
	}
	
	/**
	 * �յ��ͻ�����Ϣ����õķ���
	 * @param message �ͻ��˷��͹�������Ϣ
	 * @param session ��ѡ�Ĳ���
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		if(message.substring(0, 5).equals("name:")){
			//д���û���
			user.get(this).setUsername(message.substring(5));
			System.out.println(user.get(this).getUsername()+"��¼" );
			return;
		}
		if(message.substring(0, 6).equals("class:")){
			//д��༶
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
		
		//����Ӧ�İ༶Ⱥ����Ϣ
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
		//д����Ϣ��¼,�ְ��¼
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
	 * ��������ʱ����
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("��������");
		error.printStackTrace();
	}
	
	/**
	 * ������������漸��������һ����û����ע�⣬�Ǹ����Լ���Ҫ��ӵķ�����
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
