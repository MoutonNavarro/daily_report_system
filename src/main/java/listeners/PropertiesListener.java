package listeners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PropertiesListener implements ServletContextListener {

    public PropertiesListener() {
    }

    /**
     * Processing when shutting down Web application
     */
    public void contextDestroyed(ServletContextEvent sce)  {
    }

    /**
     * Processing when engaging Web application
     */
    public void contextInitialized(ServletContextEvent sce)  {
        ServletContext context = sce.getServletContext();

        //Read property file and set at application scope
        try {
            InputStream is = PropertiesListener.class.getClassLoader().getResourceAsStream("application.properties");

            Properties properties = new Properties();
            properties.load(is);
            is.close();

            Iterator<String> pit = properties.stringPropertyNames().iterator();
            while (pit.hasNext()) {
                String pname = pit.next();
                context.setAttribute(pname,  properties.getProperty(pname));

            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
