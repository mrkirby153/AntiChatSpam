package me.mrkirby153.AntiChatSpam.version;

import me.mrkirby153.AntiChatSpam.reference.Strings;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

class ThreadVersionChecker extends Thread{

    private static ThreadVersionChecker instance;

    protected static void checkVersion(){
        if(instance == null)
            instance = new ThreadVersionChecker();
        instance.start();
    }

    @Override
    public void run() {
        Logger l = LogManager.getLogger("ACS-Update Checker");
        try{
            String checkerVersion = Strings.MC_VERSION;
            if(checkerVersion.equals("@MC_VERSION@")){
                checkerVersion = MinecraftForge.MC_VERSION;
                l.warn("Using forge minecraft version for version check...");
            }
            URL url = new URL("https://raw.githubusercontent.com/mrkirby153/AntiChatSpam/master/version/"+ checkerVersion+".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String newVersion = reader.readLine();
            l.info("Found version "+newVersion);
            VersionChecker.remoteVersion = newVersion;
        } catch (IOException e){
            l.error("An error occurred when attempting to check version", e);
            VersionChecker.status = VersionChecker.VersionStatus.ERRORED;
        }
        VersionChecker.doneChecking = true;
        l.info("Done checking version!");
    }
}
