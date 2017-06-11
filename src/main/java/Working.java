/**
 *
 */


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author daniel
 *
 */


public class Working {

    private static final String SWITCH_IP="10.99.0.10";    // NEC_C BiO switch
    //private static final String SWITCH_USER="admin";
    private static final String SWITCH_USER="cisco";
    private static final String SWITCH_PASS="mf$zx2sAe";


    /**
     * @param args
     */
    public static void main(String[] args) {
//        sendCommand("10.0.116.12",22,"zeetta","Zeetta123","ls");
        sendCommand(SWITCH_IP, 22,SWITCH_USER,SWITCH_PASS,"display openflow summary");

    }
    public static void sendCommand(String ip, Integer port, String userName, String password, String cmd) {
        Session session = null;
        ChannelExec channel = null;
        try {
            JSch jsch = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");

            session = jsch.getSession(userName, ip, port);
            session.setPassword(password);
            session.connect();

            channel = (ChannelExec)session.openChannel("exec");
            InputStream in = channel.getInputStream();
            channel.setErrStream(System.err);
            channel.setCommand(cmd);
            channel.connect();

            StringBuilder message = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                message.append(line).append("\n");
            }
            channel.disconnect();
            while (!channel.isClosed()) {

            }
            System.out.println(channel.getExitStatus()+message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.disconnect();
                } catch (Exception e) {

                }
            }
        }
    }

}
