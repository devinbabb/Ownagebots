
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.util.GlobalConfiguration;

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

                // Ok here we are saving the jar in cacheDirectory

                URL url = new URL("http://ownagebots.com/loader/index.php?request=oblogin");
                String location = GlobalConfiguration.Paths.getCacheDirectory()+ File.separator + "Scripts" + File.separator + "OBLogin.jar";
                File file = new File(location);
                URLConnection connect = url.openConnection();
                connect.setRequestProperty("Accept-Encoding", "zip, jar");
                connect.connect();
                BufferedInputStream inp = new BufferedInputStream(connect.getInputStream());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(location));
                byte[] buff = new byte[1024];
                int end;

                while ((end = inp.read(buff)) != -1) {
                    out.write(buff, 0, end);
                }

                log("Saved the class");

                //  Ok here we are loading the class
                try {
                    URL uri = file.toURL();
                    URL[] urls = new URL[]{uri};
                    URLClassLoader cl = new URLClassLoader(urls);
                    this.mainloader = (Script) cl.loadClass("OBLogin").newInstance();
                } catch (MalformedURLException e) {
                } catch (ClassNotFoundException e) {
                }


                out.flush();
                out.close();
                in.close();
                
                delegateTo(this.mainloader);
                return mainloader.onStart();
            } catch (Exception e) {
                this.log.severe("An error occurred, please start the script again .");
                log(e);
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
