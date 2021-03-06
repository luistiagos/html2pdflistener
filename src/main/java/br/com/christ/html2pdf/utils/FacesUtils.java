package br.com.christ.html2pdf.utils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.*;
import java.util.logging.Logger;


public class FacesUtils {

    final static String resourcePrefix = "/javax.faces.resource";

    private Logger logger =java.util.logging.Logger.getLogger(FacesUtils.class.getName());

    public static byte[] getBytesFromReference(String reference) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
		String contextPath = externalContext.getRequestContextPath();
		if(reference.startsWith(contextPath)) {
			reference = reference.substring((contextPath.length()));
        }
		return FacesUtils.getBytesFromResource(reference);
	}


	public static String getStringFromReference(String reference) throws IOException {
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		if(reference.startsWith(contextPath))
			reference = reference.substring((contextPath.length()));
		return FacesUtils.getStringFromResource(reference);
	}

	public static byte[] getBytesFromResource(String resourcePath) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String contextPath = externalContext.getRequestContextPath();
		InputStream stream = getStreamFromResource(resourcePath);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		if (stream == null) {
			return new byte[0];
		}
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}



    private static InputStream getStreamFromResource(String resourcePath) throws IOException {
	    String fullBasePath = getBaseFacesURL();
	    if (resourcePath.startsWith(fullBasePath)) {
		    resourcePath = resourcePath.substring(fullBasePath.length());
	    }
        if (resourcePath.startsWith(resourcePrefix)) {
            return getStreamFromFacesResource(resourcePath);
        } else {
            // For some reason, some JSF resource component is "smart" enough to include parameters
            // on a request to a static resource. Remove anything that goes after a "?" character
            if (resourcePath.contains("?")) {
                resourcePath = resourcePath.substring(0, resourcePath.indexOf('?'));
            }

            return FacesContext.getCurrentInstance().getExternalContext()
                    .getResourceAsStream(resourcePath);
        }

    }
    private static InputStream getStreamFromFacesResource(String resourcePath) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        String resourceName = resourcePath;
        String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        if (resourcePath.startsWith(contextPath))
            resourcePath = resourcePath.substring((contextPath.length()));
        if(resourcePath.startsWith(resourcePrefix))
            resourceName = resourcePath.substring(resourcePrefix.length() + 1);
        String libraryName = null;
        if (resourcePath.contains("ln=")) {
            if(resourcePath.contains(";jsessionid="))
                resourcePath = resourcePath.substring(0, resourcePath.indexOf(";jsessionid=")) + resourcePath.substring(resourcePath.indexOf("?"));
            if (resourceName.contains("?"))
                resourceName = resourceName.substring(0, resourceName.indexOf("?"));
            if(resourceName.endsWith(".jsf"))
                resourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
            int lnIndex = resourcePath.indexOf("ln=");
            libraryName = resourcePath.substring(lnIndex + 3);
            if (libraryName.contains("&")) {
                libraryName = libraryName.substring(0, libraryName.indexOf("&"));
            }
        }
        if (resourceName.endsWith(".jsf"))
            resourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
        Resource resource;
        if (libraryName != null) {
            resource = resourceHandler.createResource(resourceName, libraryName);
        } else {
            resource = resourceHandler.createResource(resourceName);
        }
        if(resource == null) {
            Logger.getLogger(FacesUtils.class.getName()).warning("Could not fetch resource "+resourceName+ "!");
            return null;
        }
        return resource.getInputStream();
    }

	public static String getStringFromResource(String resourcePath) throws IOException {
        InputStream stream = getStreamFromResource(resourcePath);
        if(stream == null)
            return "";
		//read it with BufferedReader
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

		StringBuilder sb = new StringBuilder();
		String linha = bufferedReader.readLine();
		while(linha != null) {
			sb.append(linha).append("\n");
			linha = bufferedReader.readLine();
		}
		return sb.toString();
	}

	public static String getBaseFacesURL() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return externalContext.getRequestScheme() + "://" + externalContext.getRequestServerName() + ":"
				+ externalContext.getRequestServerPort() + externalContext.getRequestContextPath();
	}
}
