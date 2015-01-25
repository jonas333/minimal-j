package org.minimalj.frontend.html;

import org.minimalj.application.Application;
import org.minimalj.frontend.html.toolkit.HtmlContent;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.toolkit.ClientToolkit;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class HtmlFrontend extends NanoHTTPD {
	
	public HtmlFrontend() {
        super(8082);
        
        ClientToolkit.setToolkit(new HtmlClientToolkit());
    }

    @Override public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String pageLink = session.getUri();

        if ("/favicon.ico".equals(pageLink)) {
        	return new NanoHTTPD.Response(Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "No favicon yet");
        }
        
        if ("/mj.css".equals(pageLink)) {
        	return new NanoHTTPD.Response(Status.OK, NanoHTTPD.MIME_PLAINTEXT, getClass().getResourceAsStream("mj.css"));
        }
        
        if (pageLink.startsWith("/")) {
        	pageLink = pageLink.substring(1);
        }
        Page visiblePage = PageLink.createPage(pageLink);
        
        String msg = "<html><head><title>" + visiblePage.getTitle() + "</title>";
        msg += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/mj.css\"></head><body>";
        msg += new HtmlMenuBar(visiblePage).toHtml();
        if (visiblePage.getContent() != null) {
        	msg += ((HtmlContent) visiblePage.getContent()).toHtml();
        }
        msg += "</body></html>\n";

        return new NanoHTTPD.Response(msg);
    }

    public static void main(String[] args) {
    	Application.initApplication(args);
    	
        ServerRunner.run(HtmlFrontend.class);
    }
}