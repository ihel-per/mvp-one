package com.imvp.demo.service.impl;

import com.imvp.demo.domain.Item;
import com.imvp.demo.repository.ItemRepository;
import com.imvp.demo.service.ItemService;
import com.imvp.demo.service.TestScheduler;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final TestScheduler testScheduler;

    public ItemServiceImpl(ItemRepository itemRepository, TestScheduler testScheduler) {
        this.itemRepository = itemRepository;
        this.testScheduler = testScheduler;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        Item save = itemRepository.save(item);
        testScheduler.scheduleJob(save);

        return save;
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable);
    }


    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Item> findOne(String id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }
}
