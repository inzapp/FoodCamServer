package com.foodcam.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

import com.foodcam.util.Cmd;
import com.foodcam.util.pRes;

/**
 * 실제 사진 검색 이전 서버에 정상적으로 접속했는지 여부를 검사하는 클래스.
 * 클라이언트의 동시접속을 대비해 스레드를 이용해 병렬처리한다 
 * 공개키 방식으로 입력된 키값을 대조해 지정된 App에서 접속요청을 했는지를 판단한다.
 * 또한 서버에 할당된 스레드풀을 모두 사용중인 경우(서버가 바쁜 경우) 접속을 차단해 서버의 부하를 막는 역할도 한다.
 * 
 * @author root
 *
 */
public final class Gate implements Runnable {
	
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Gate(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * 클라이언트 접속시 하는 것
	 * 1. 통신을 위한 스트림 생성
	 * 2. 서버 키 대조를 통해 인증된 앱에서 접속했는지를 확인
	 * 3. 서버 부하상태를 검사해 부하상태라면 접속 차단, 부하상태가 아니라면 서버 접속
	 */
	@Override
	public void run() {
		
		if (!getStream())
			return;

		if (!keyMatching(getRequestKey()))
			return;

		if (serverIsBusy()) {
			sendServerStatus(Cmd.SERVER_IS_BUSY);
			return;
		}

		sendServerStatus(Cmd.SERVER_IS_STABLE);
		enterServer();
	}

	/**
	 * 소켓 통신을 위한 클라이언트 스트림을 얻어온다
	 * 
	 * @return
	 */
	private boolean getStream() {
		
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (Exception e) {
			disconnect();
			pRes.log("Client data stream get failure");
			return false;
		}

		return true;
	}

	/**
	 * 클라이언트로부터 서버 키를 전송받아 미리 약속된 키인지 확인한다
	 * 키값이 다르다면 의도적인 비정상 접속으로 간주해 접속을 차단한다
	 * 
	 * @param requestKey
	 * @return
	 */
	private boolean keyMatching(String requestKey) {
		
		if (!requestKey.equals(Cmd.SERVER_KEY)) {
			disconnect();
			pRes.log("[FATAL] Key matching failure");
			return false;
		}

		return true;
	}

	// 요청정보에서 서버 키 추출
	private String getRequestKey() {
		
		String requestKey = "DEFAULT";
		try {
			JSONObject json = new JSONObject((String) ois.readObject());
			requestKey = (String) json.getString("key");
		} catch (Exception e) {
			pRes.log("Key not found");
			e.printStackTrace();
		}

		return requestKey;
	}

	/**
	 * 현재 서버가 바쁜지 여부를 리턴한다
	 * 서버의 부하상태를 클라이언트 다중접속 처리를 위한 스레드풀의 부하도를 참조한다
	 * 부하도는 서버에서 돌 수 있는 스레드 - 5 개만큼 스레드가 돌고 있으면 바쁜 것으로 간주한다
	 * 
	 * @return
	 */
	private boolean serverIsBusy() {
		return pRes.MAX_THREAD_COUNT - 5 <= pRes.threadCountAlerter.getActiveCount();
	}

	/**
	 * 현재 서버상태를 클라이언트에게 전송한다
	 * 만약 서버 상태가 부하상태라면 연결을 종료한다
	 * 
	 * @param serverStatus
	 */
	private void sendServerStatus(String serverStatus) {
		
		send("serverStatus", serverStatus);

		if (serverStatus.equals(Cmd.SERVER_IS_BUSY))
			disconnect();
	}

	/**
	 * 스트림생성, 키값대조, 서버 부하도를 모두 통과한 경우
	 * 서버에 로그를 남기면서
	 * 실제로 서버에 접속해 이미지를 전송받기 시작한다
	 */
	private void enterServer() {
		
		pRes.log("접속  IP : " + clientSocket.getInetAddress().getHostAddress());
		pRes.serverThreadPool.execute(new RequestPic(oos, ois));
	}

	/**
	 * 초기 접속시 얻은 스트림을 이용해 클라이언트에게 메시지를 전송하는데 사용된다
	 * 
	 * @param request
	 * @param msg
	 */
	private void send(String request, String msg) {
		
		JSONObject json = new JSONObject();
		try {
			json.put(request, msg);
			oos.writeObject(json.toString());
		} catch (Exception e) {
		}
	}

	/**
	 * 클라이언트와 연결을 종료한다
	 */
	private void disconnect() {
		
		try {
			clientSocket.getOutputStream().close();
		} catch (Exception e) {
		}
	}
}
