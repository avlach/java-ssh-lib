/*
 * Copyright © 2017 Zeetta and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package tasos.sshj;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import org.junit.Test;

/**
 * @author wasp
 *
 */
public class testSimpleSsh {

    private static final String SWITCH_IP="10.99.0.10";    // NEC_C BiO switch
    private static final String SWITCH_USER="cisco";
    private static final String SWITCH_PASS="mf$zx2sAe";

//    private static final String SWITCH_IP="10.0.116.12";
//    private static final String SWITCH_USER="zeetta";
//    private static final String SWITCH_PASS="Zeetta123";

    private static final int SSH_PORT= 22;


    @Test
    public void testSimpleCommand() throws IOException {
        final SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();

        ssh.connect(SWITCH_IP);
        try {
            ssh.authPassword(SWITCH_USER, SWITCH_PASS);
            final Session session = ssh.startSession();
            try {
                final Command cmd = session.exec("ls -l");
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(5, TimeUnit.SECONDS);
                System.out.println("\n** exit status: " + cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
        }
    }

}
