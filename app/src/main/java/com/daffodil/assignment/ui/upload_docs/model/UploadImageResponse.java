
package com.daffodil.assignment.ui.upload_docs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageResponse {

    @SerializedName("documents")
    @Expose
    private String documents;

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

}
