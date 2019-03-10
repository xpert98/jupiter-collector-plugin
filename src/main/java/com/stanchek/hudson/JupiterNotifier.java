package com.stanchek.hudson;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class JupiterNotifier extends Notifier {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger
			.getLogger(JupiterNotifier.class.getName());

	private final String appName;
	private final String primaryOwner;

	@DataBoundConstructor
	public JupiterNotifier(String appName, String primaryOwner) {
		this.appName = appName;
		this.primaryOwner = primaryOwner;
	}

	public String getAppName() {
		return appName;
	}
	
	public String getPrimaryOwner() {
		return primaryOwner;
	}
	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        if (appName != null) {
            jsonObjectBuilder.add("commonName", appName);
            jsonObjectBuilder.add("primaryOwner", primaryOwner);
        }
        JsonObject jsonObject = jsonObjectBuilder.build();
        byte[] payloadBytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);

        final HttpURLConnection conn = (HttpURLConnection) new URL(getDescriptor().getCollectorUrl() + "/inventoryItem/create").openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", Integer.toString(payloadBytes.length));
        conn.setRequestProperty("Authorization", "Bearer " + getDescriptor().getCollectorApiToken());
        conn.connect();

        try (OutputStream os = new BufferedOutputStream(conn.getOutputStream())) {
            os.write(payloadBytes);
            os.flush();
        }

        if (conn.getResponseCode() == 200) {

        	LOGGER.info("Jupiter Collector - successfully added inventory item");

        } else if (conn.getResponseCode() == 400) {
        	LOGGER.info("Jupiter Collector - invalid request");
        } else if (conn.getResponseCode() == 401) {
        	LOGGER.info("Jupiter Collector - unauthorized request");
        } else if (conn.getResponseCode() == 404) {
        	LOGGER.info("Jupiter Collector - service not found");
        } else {
        	LOGGER.info(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
		
		
		return true;
	}

	@Override
	public boolean needsToRunAfterFinalized() {
		return true;
	}

	
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }
	
	
	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {

		private String collectorUrl;

		private String collectorApiToken;

		public DescriptorImpl() {
			super(JupiterNotifier.class);
			load();
		}

		@Override
		public String getDisplayName() {
			return "Jupiter Collector";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws FormException {
			req.bindParameters(this);
			this.collectorUrl = formData.getString("collectorUrl");
			this.collectorApiToken = formData.getString("collectorApiToken");
			save();
			return super.configure(req, formData);
		}

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
			return super.newInstance(req, formData);
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		public String getCollectorUrl() {
			return collectorUrl;
		}

		public void setCollectorUrl(String collectorUrl) {
			this.collectorUrl = collectorUrl;
		}

		public String getCollectorApiToken() {
			return collectorApiToken;
		}

		public void setCollectorApiToken(String collectorApiToken) {
			this.collectorApiToken = collectorApiToken;
		}

	}
}
