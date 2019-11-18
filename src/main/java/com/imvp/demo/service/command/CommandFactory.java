package com.imvp.demo.service.command;

import com.imvp.demo.repository.ItemRepository;
import com.imvp.demo.repository.StoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CommandFactory {

    private final StoryRepository storyRepository;
    private final ItemRepository itemRepository;

    public CommandFactory(StoryRepository storyRepository, ItemRepository itemRepository) {
        this.storyRepository = storyRepository;
        this.itemRepository = itemRepository;
    }

    public InstCommand publishPostImgCommand(String itemId) {
        return new InstPublishPostImgCommand(itemRepository, itemId);
    }

    public InstCommand publishPostVideoCommand(String itemId) {
        return new InstPublishPostVideoCommand(itemRepository, itemId);
    }

    public InstCommand postStoryImgCommand(String itemId) {
        return new InstPublishStoryImgCommand(storyRepository, itemId);
    }

}
