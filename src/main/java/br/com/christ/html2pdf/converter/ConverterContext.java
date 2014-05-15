package br.com.christ.html2pdf.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.RequestScoped;

import br.com.christ.html2pdf.loader.FacesResourceLoader;
import br.com.christ.html2pdf.loader.ResourceLoader;

@RequestScoped
public class ConverterContext {
    private List<? extends ConversionListener> listeners;
	private ResourceLoader resourceLoader;
	private boolean preloadResources;
	private String htmlContent;
	private String url;
    private boolean removeStyles;

	private String inputEncoding;

	public ConverterContext() {
        setListeners(new ArrayList<ConversionListener>());
        preloadResources = false;
        url = "";
        htmlContent = "";
        resourceLoader = new FacesResourceLoader();
	}

	public boolean isPreloadResources() {
		return preloadResources;
	}

	public void setPreloadResources(boolean preloadResources) {
		this.preloadResources = preloadResources;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public String getInputEncoding() {
		return inputEncoding;
	}

	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

    public List<? extends ConversionListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<? extends ConversionListener> listeners) {
        this.listeners = listeners;
    }

    public boolean isRemoveStyles() {
        return removeStyles;
    }

    public void setRemoveStyles(boolean removeStyles) {
        this.removeStyles = removeStyles;
    }

}
