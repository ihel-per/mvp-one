package com.imvp.demo.web.rest;

import com.imvp.demo.domain.Story;
import com.imvp.demo.service.StoryService;
import com.imvp.demo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.imvp.demo.domain.Story}.
 */
@RestController
@RequestMapping("/api")
public class StoryResource {

    private final Logger log = LoggerFactory.getLogger(StoryResource.class);

    private static final String ENTITY_NAME = "story";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoryService storyService;

    public StoryResource(StoryService storyService) {
        this.storyService = storyService;
    }

    /**
     * {@code POST  /stories} : Create a new story.
     *
     * @param story the story to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new story, or with status {@code 400 (Bad Request)} if the story has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stories")
    public ResponseEntity<Story> createStory(@Valid @RequestBody Story story) throws URISyntaxException {
        log.debug("REST request to save Story : {}", story);
        if (story.getId() != null) {
            throw new BadRequestAlertException("A new story cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Story result = storyService.save(story);
        return ResponseEntity.created(new URI("/api/stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stories} : Updates an existing story.
     *
     * @param story the story to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated story,
     * or with status {@code 400 (Bad Request)} if the story is not valid,
     * or with status {@code 500 (Internal Server Error)} if the story couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stories")
    public ResponseEntity<Story> updateStory(@Valid @RequestBody Story story) throws URISyntaxException {
        log.debug("REST request to update Story : {}", story);
        if (story.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Story result = storyService.save(story);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, story.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stories} : get all the stories.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stories in body.
     */
    @GetMapping("/stories")
    public ResponseEntity<List<Story>> getAllStories(Pageable pageable) {
        log.debug("REST request to get a page of Stories");
        Page<Story> page = storyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stories/:id} : get the "id" story.
     *
     * @param id the id of the story to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the story, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stories/{id}")
    public ResponseEntity<Story> getStory(@PathVariable String id) {
        log.debug("REST request to get Story : {}", id);
        Optional<Story> story = storyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(story);
    }

    /**
     * {@code DELETE  /stories/:id} : delete the "id" story.
     *
     * @param id the id of the story to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable String id) {
        log.debug("REST request to delete Story : {}", id);
        storyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
