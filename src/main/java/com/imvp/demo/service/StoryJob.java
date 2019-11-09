package com.imvp.demo.service;

import com.imvp.demo.domain.Profile;
import com.imvp.demo.domain.Story;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.StoryRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadStoryPhotoRequest;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class StoryJob extends QuartzJobBean {

    private final StoryRepository storyRepository;

    private static final Logger logger = LoggerFactory.getLogger(StoryJob.class);

    public StoryJob(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        post(jobDataMap);
    }

    private void post(JobDataMap jobDataMap) {

        Story itemId = storyRepository.findById(jobDataMap.getString("itemId"))
            .get();

        try {
            Profile owner = itemId.getOwner();

            Instagram4j instagram = Instagram4j.builder().username(owner.getNick()).password(owner.getSencha()).build();
            instagram.setup();
            instagram.login();

            File story = File.createTempFile("story", ".png");
            FileOutputStream fos = new FileOutputStream(story);
            fos.write(itemId.getContent());
            fos.flush();

            instagram.sendRequest(new InstagramUploadStoryPhotoRequest(story, Collections.emptyList()));
            story.delete();
            fos.close();

            itemId.status(Status.PUBLISHED);
        } catch (Exception e) {
            itemId.status(Status.REJECTED);
            logger.error("", e);
        }

        storyRepository.save(itemId);

    }

}
