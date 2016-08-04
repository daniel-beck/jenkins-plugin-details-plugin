package org.jenkinsci.plugins.plugindetails;

import hudson.Extension;
import hudson.PluginWrapper;
import hudson.model.UnprotectedRootAction;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Extension
public class PluginDetailsRootAction implements UnprotectedRootAction {
    @Override
    public String getIconFileName() {
        return "plugin.png";
    }

    @Override
    public String getDisplayName() {
        return "Plugin Details";
    }

    @Override
    public String getUrlName() {
        return "pluginDetails";
    }

    public void doPlugin(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        String name = req.getParameter("plugin");

        req.setAttribute("pluginName", name);
        req.getView(this, "plugin.jelly").forward(req, rsp);
    }

    public String getWikiContent(PluginWrapper pw) throws IOException {
        String text = getUrlContent(new URL(pw.getUrl()));

        text = filterContent(text);

        text = rewriteContent(text);

        return text;
    }

    private String rewriteContent(String text) {
        text = text.replace("href=\"/", "href=\"https://wiki.jenkins-ci.org/");
        text = text.replace("src=\"/", "src=\"https://wiki.jenkins-ci.org/");
        return text;
    }

    private String filterContent(String text) {
        text = text.substring(text.indexOf("<!-- wiki content -->"));
        text = text.substring(0, text.indexOf("        </div>"));
        return text;
    }

    private String getUrlContent(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(true);

        int status = conn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream is = conn.getInputStream();
            return IOUtils.toString(is);
        } else {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                String newUrl = conn.getHeaderField("Location");
                return getUrlContent(new URL(newUrl));
            }
        }

        // error
        throw new IllegalStateException("Error response code: " + status + " for URL: " + url.toString());
    }
}
