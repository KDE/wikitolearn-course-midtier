package org.wikitolearn.midtier.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.ChapterClient;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.EntityList;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChapterService {

  @Autowired
  private ChapterClient chapterClient;

  public EntityList<Chapter> findAll() {
    return chapterClient.findAll();
  }

  public Chapter find(String chapterId, MultiValueMap<String, String> params) {
    return chapterClient.find(chapterId, params);
  }
}
