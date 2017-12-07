package dymn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.istack.internal.NotNull;

public class SftpUtil {

	@NotNull
	private String host;

	@NotNull
	private String password;

	@NotNull
	private String username;

	@NotNull
	private int port;
	
	private ChannelSftp sftp = null;
	
	public void init() throws Exception {
		JSch ssh = new JSch();

		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		
		Session session = ssh.getSession(username, host, port);
		session.setConfig(config);
		session.setPassword(password);
		session.connect();
		
		Channel channel = session.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
	}
	
	public byte[] getFile(String remote, String local) throws Exception {
//		OutputStream dest = new FileOutputStream(new File(local));
		
		
//		sftp.get(remote, dest);
//		int idx = remote.lastIndexOf("/");
//		String tempDir = remote.substring(0, idx);
//		sftp.cd(tempDir);
//		String tempFile = remote.substring(idx + 1);
		sftp.get(remote, local);	
		File file = new File(local);
		int size = (int)file.length();
		byte[] resultByte = new byte[size];
		InputStream is = new FileInputStream(new File(local));
		is.read(resultByte, 0, size);
		
		return resultByte;
	}
	
	public void putFile(String remote, String local) throws Exception {
		sftp.put(local, remote);
	}
	
	public void close() throws Exception {
		if (!sftp.isClosed()) {
			sftp.disconnect();
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
