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

	private final String commonName;
	private final String primaryOwner;
	private final String aliases;
	private final String description;
	private final String codeRepoUrl;
	private final String binaryRepoUrl;
	private final String primaryLanguage;
	private final String secondaryLanguages;
	private final String type;
	private final String secondaryOwners;
	private final String businessUnit;
	private final String exposure;
	private final int numUsers;
	private final String dataClassification;
	private final String deploymentEnv;
	private final String deploymentEnvUrl;
	private final String riskLevel;
	private final String regulations;
	private final String chatChannel;
	private final String agileScrumBoardUrl;
	private final String buildServerUrl;
	private final String age;
	private final String lifecycleStage;

	@DataBoundConstructor
	public JupiterNotifier(String commonName, String primaryOwner, 
			String aliases, String description, String codeRepoUrl,
			String binaryRepoUrl, String primaryLanguage,
			String secondaryLanguages, String type, String secondaryOwners,
			String businessUnit, String exposure, int numUsers,
			String dataClassification, String deploymentEnv,
			String deploymentEnvUrl, String riskLevel,
			String regulations, String chatChannel,
			String agileScrumBoardUrl, String buildServerUrl,
			String age, String lifecycleStage) {
		this.commonName = commonName;
		this.primaryOwner = primaryOwner;
		this.aliases = aliases;
		this.description = description;
		this.codeRepoUrl = codeRepoUrl;
		this.binaryRepoUrl = binaryRepoUrl;
		this.primaryLanguage = primaryLanguage;
		this.secondaryLanguages = secondaryLanguages;
		this.type = type;
		this.secondaryOwners = secondaryOwners;
		this.businessUnit = businessUnit;
		this.exposure = exposure;
		this.numUsers = numUsers;
		this.dataClassification = dataClassification;
		this.deploymentEnv = deploymentEnv;
		this.deploymentEnvUrl = deploymentEnvUrl;
		this.riskLevel = riskLevel;
		this.regulations = regulations;
		this.chatChannel = chatChannel;
		this.agileScrumBoardUrl = agileScrumBoardUrl;
		this.buildServerUrl = buildServerUrl;
		this.age = age;
		this.lifecycleStage = lifecycleStage;
	}

	public String getCommonName() {
		return commonName;
	}
	
	public String getPrimaryOwner() {
		return primaryOwner;
	}
	
	public String getAliases() {
		return aliases;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCodeRepoUrl() {
		return codeRepoUrl;
	}

	public String getBinaryRepoUrl() {
		return binaryRepoUrl;
	}
	
	public String getPrimaryLanguage() {
		return primaryLanguage;
	}
	
	public String getSecondaryLanguages() {
		return secondaryLanguages;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSecondaryOwners() {
		return secondaryOwners;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	
	public String getExposure() {
		return exposure;
	}
	
	public int getNumUsers() {
		return numUsers;
	}
	
	public String getDataClassification() {
		return dataClassification;
	}
	
	public String getDeploymentEnv() {
		return deploymentEnv;
	}
	
	public String getDeploymentEnvUrl() {
		return deploymentEnvUrl;
	}
	
	public String getRiskLevel() {
		return riskLevel;
	}
	
	public String getRegulations() {
		return regulations;
	}
	
	public String getChatChannel() {
		return chatChannel;
	}
	
	public String getAgileScrumBoardUrl() {
		return agileScrumBoardUrl;
	}
	
	public String getBuildServerUrl() {
		return buildServerUrl;
	}
	
	public String getAge() {
		return age;
	}
	
	public String getLifecycleStage() {
		return lifecycleStage;
	}
	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        if (commonName != null) {
            jsonObjectBuilder.add("commonName", commonName);
            jsonObjectBuilder.add("primaryOwner", primaryOwner);
            jsonObjectBuilder.add("aliases", aliases);
            jsonObjectBuilder.add("description", description);
            jsonObjectBuilder.add("codeRepoUrl", codeRepoUrl);
            jsonObjectBuilder.add("binaryRepoUrl", binaryRepoUrl);
            jsonObjectBuilder.add("primaryLanguage", primaryLanguage);
            jsonObjectBuilder.add("secondaryLanguages", secondaryLanguages);
            jsonObjectBuilder.add("type", type);
            jsonObjectBuilder.add("secondaryOwners", secondaryOwners);
            jsonObjectBuilder.add("businessUnit", businessUnit);
            jsonObjectBuilder.add("exposure", exposure);
            jsonObjectBuilder.add("numUsers", numUsers);
            jsonObjectBuilder.add("dataClassification", dataClassification);
            jsonObjectBuilder.add("deploymentEnv", deploymentEnv);
            jsonObjectBuilder.add("deploymentEnvUrl", deploymentEnvUrl);
            jsonObjectBuilder.add("riskLevel", riskLevel);
            jsonObjectBuilder.add("regulations", regulations);
            jsonObjectBuilder.add("chatChannel", chatChannel);
            jsonObjectBuilder.add("agileScrumBoardUrl", agileScrumBoardUrl);
            jsonObjectBuilder.add("buildServerUrl", buildServerUrl);
            jsonObjectBuilder.add("age", age);
            jsonObjectBuilder.add("lifecycleStage", lifecycleStage);
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
