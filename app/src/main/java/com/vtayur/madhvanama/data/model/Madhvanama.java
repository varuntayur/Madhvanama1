package com.vtayur.madhvanama.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vtayur on 8/19/2014.
 */
public class Madhvanama implements Serializable {

    @SerializedName("lang")
    private String lang;

    @SerializedName("sections")
    List<com.vtayur.madhvanama.data.model.Section> sections;

    private Map<String, com.vtayur.madhvanama.data.model.Section> mapSecName2Sec = new LinkedHashMap<String, Section>();

    public Madhvanama() {
    }

    @Override
    public String toString() {
        return "Madhvanama{" +
                "sections=" + sections +
                '}';
    }


    public Section getSection(String sectionName) {

        buildMap();

        return mapSecName2Sec.get(sectionName);
    }

    public Collection<String> getSectionNames() {

        buildMap();

        return mapSecName2Sec.keySet();
    }

    public String getLang() {
        return lang;
    }


    private void buildMap() {
        if (mapSecName2Sec.keySet().isEmpty())
            for (Section section : sections) {
                mapSecName2Sec.put(section.getName(), section);
            }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Madhvanama madhvanama = (Madhvanama) o;

        if (lang != null ? !lang.equals(madhvanama.lang) : madhvanama.lang != null) return false;
        return sections != null ? sections.equals(madhvanama.sections) : madhvanama.sections == null;
    }

    @Override
    public int hashCode() {
        int result = lang != null ? lang.hashCode() : 0;
        result = 31 * result + (sections != null ? sections.hashCode() : 0);
        return result;
    }
}
