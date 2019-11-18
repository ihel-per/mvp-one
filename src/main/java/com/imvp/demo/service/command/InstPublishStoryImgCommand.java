package com.imvp.demo.service.command;

import com.imvp.demo.domain.Profile;
import com.imvp.demo.domain.Story;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.StoryRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadStoryPhotoRequest;

public class InstPublishStoryImgCommand implements InstCommand {

    private final StoryRepository storyRepository;
    private final String itemId;

    public InstPublishStoryImgCommand(StoryRepository storyRepository, String itemId) {
        this.storyRepository = storyRepository;
        this.itemId = itemId;
    }

    @Override
    public void execute() {

        Story item = storyRepository.findById(itemId)
            .get();

        try {
            Profile owner = item.getOwner();

            Instagram4j instagram = Instagram4j.builder().username(owner.getNick()).password(owner.getSencha()).build();
            instagram.setup();
            instagram.login();

            File story = File.createTempFile("", "");
            FileOutputStream fos = new FileOutputStream(story);
            fos.write(item.getContent());
            fos.flush();

            instagram.sendRequest(new InstagramUploadStoryPhotoRequest(story, Collections.emptyList()));
            story.delete();
            fos.close();

            item.status(Status.PUBLISHED);
        } catch (Exception e) {
            item.status(Status.REJECTED);
            // logger.error("", e);
        }

        storyRepository.save(item);

    }
}
