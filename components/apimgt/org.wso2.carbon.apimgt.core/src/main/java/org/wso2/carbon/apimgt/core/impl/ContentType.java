package org.wso2.carbon.apimgt.core.impl;

import javax.ws.rs.core.MediaType;

public enum ContentType {
    TEXT_XML(MediaType.TEXT_XML), APPLICATION_JSON(MediaType.APPLICATION_JSON), TEXT_PLAIN(MediaType.TEXT_PLAIN),
    APPLICATION_XML(MediaType.APPLICATION_XML), TEXT_HTML(MediaType.TEXT_HTML), CHARSET_PARAMETER(MediaType.CHARSET_PARAMETER);

    private String mediaType;

    private ContentType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
