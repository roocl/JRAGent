package org.jragent.service;

import java.util.List;

public interface RagService {
    float[] embed(String text);

    List<String> similaritySearch(String kbsId, String query);
}
