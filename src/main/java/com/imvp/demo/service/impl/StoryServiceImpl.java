package com.imvp.demo.service.impl;

import com.imvp.demo.domain.Story;
import com.imvp.demo.repository.StoryRepository;
import com.imvp.demo.service.SchedulerService;
import com.imvp.demo.service.StoryService;
import com.imvp.demo.service.command.InstaPost;
import com.imvp.demo.service.command.InstaPost.PostMimeType;
import com.imvp.demo.service.command.InstaPost.PostType;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Story}.
 */
@Service
public class StoryServiceImpl implements StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    private final StoryRepository storyRepository;
    private final SchedulerService schedulerService;

    public StoryServiceImpl(StoryRepository storyRepository, SchedulerService schedulerService) {
        this.storyRepository = storyRepository;
        this.schedulerService = schedulerService;
    }

    /**
     * Save a story.
     *
     * @param story the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Story save(Story story) {
        log.debug("Request to save Story : {}", story);
        Story save = storyRepository.save(story);

        PostMimeType type = story.getContentContentType().contains("video") ? PostMimeType.VIDEO : PostMimeType.PHOTO;
        schedulerService
            .scheduleJob(new InstaPost(PostType.STORY, type, save.getId(), save.getPublishTime()));

        return save;
    }

    /**
     * Get all the stories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Story> findAll(Pageable pageable) {
        log.debug("Request to get all Stories");
        return storyRepository.findAll(pageable);
    }


    /**
     * Get one story by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Story> findOne(String id) {
        log.debug("Request to get Story : {}", id);
        return storyRepository.findById(id);
    }

    /**
     * Delete the story by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.deleteById(id);
    }
}
