package org.wikitolearn.midtier.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.wikitolearn.midtier.course.client.PageClient;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.Page;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PageService {

    @Autowired
    private PageClient pageClient;

    public EntityList<Page> findAll() {
        return pageClient.findAll();
    }

    public Page find(String pageId, MultiValueMap<String, String> params) {
        return pageClient.find(pageId, params);
    }
}
