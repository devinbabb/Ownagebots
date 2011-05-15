import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
@ScriptManifest(authors = {"Ownageful"}, keywords = {"Ownageful"}, name = "Ownagebots Script Loader [1]", version = 1.0, description = "Access to all OwnageBots")
public class OBLoader extends Script {
    public Script mainloader;
    @Override
    public boolean onStart() {
        BufferedReader in = null;
        String queryStatus = null;
        try {
            URL dll = new URL("http://ownagebots.com/status.php");
            URLConnection dllConnection = dll.openConnection();
            dllConnection.connect();
            in = new BufferedReader(new InputStreamReader(dllConnection.getInputStream()));
            queryStatus = in.readLine();
            in.close();
        } catch (IOException iOException) {
            this.log.severe("An error occurred, please start the script again or redownload the loader if problem persists .");
            return false;
        }
        if (queryStatus.equals("Running")) {
            try {
                URL url = new URL("http://ownagebots.com/loader/index.php?request=oblogin");
                URLClassLoader cl = new URLClassLoader(new URL[]{url});
                this.mainloader = (Script) cl.loadClass("OBLogin").newInstance();
                delegateTo(this.mainloader);
                return mainloader.onStart();
            } catch (Exception e) {
                log(e);
                this.log.severe("An error occurred, please start the script again .");
                return false;
            }
        } else {
            this.log.severe("Server is currently offline, please try again later .");
            return false;
        }
    }
    public int loop() {
        return this.mainloader.loop();
    }
    @Override
    public void onFinish() {
        this.mainloader.onFinish();
    }
}
