package jp.dip.hmy2001.mcbeClient;

import jp.dip.hmy2001.mcbeClient.utils.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static Main instance = null;
    private Config config;
    private final CommandReader console;
    private final MCBEClient mcbeClient;
    private static final Log logger = LogFactory.getLog("Main Logger");

    public static void main(String args[]){
        new Main();
    }

    public static Main getInstance(){
        return instance;
    }

    public Main(){
        instance = this;

        this.console = new CommandReader();

        config = new Config();

        this.console.removePromptLine();
        this.console.start();

        mcbeClient = new MCBEClient(config.get("serverAddress"), Integer.parseInt(config.get("serverPort")), config.get("username"), config.get("clientUUID"));

        CommandReader.getInstance().stashLine();
        logger.info("MCBEClient starting now....");
        CommandReader.getInstance().unstashLine();
    }

    public void onCommand(String command) {
        switch (command){
            case "shutdown":
            case "stop":
                shutdown();
            break;
        }
    }

    public void shutdown() {
        console.shutdown();
        console.interrupt();

        mcbeClient.shutdown();

        CommandReader.getInstance().stashLine();
        logger.info("Shutdown system....");
        CommandReader.getInstance().unstashLine();

        CommandReader.getInstance().removePromptLine();
    }

}
