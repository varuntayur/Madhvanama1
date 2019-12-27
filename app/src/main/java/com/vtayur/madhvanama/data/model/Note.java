package com.vtayur.madhvanama.data.model;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by varuntayur on 5/19/2014.
 */
@Root
public class Note implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    public Note() {
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public String getFormattedNote() {
        String title = "";
        if (getTitle() != null)
            title = "<b>".concat(getTitle()).concat("</b>");

        String trim = getText().trim();
        StringBuilder content = new StringBuilder();
        for (String splitNewlines : trim.split("\n")) {
            content.append(splitNewlines).append("<br/>");
        }
        return title.concat("<p align='justify'>").concat(content.toString()).concat("</p>");
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
