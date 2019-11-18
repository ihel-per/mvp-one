package com.imvp.demo.service.command;

import com.imvp.demo.domain.Item;
import com.imvp.demo.domain.Profile;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.ItemRepository;
import com.imvp.demo.service.impl.ItemServiceImpl;
import java.io.File;
import java.io.FileOutputStream;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadVideoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstPublishPostVideoCommand implements InstCommand {

    private final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final String itemId;

    public InstPublishPostVideoCommand(ItemRepository itemRepository, String itemId) {
        this.itemRepository = itemRepository;
        this.itemId = itemId;
    }


    @Override
    public void execute() {

        Item item = itemRepository.findById(itemId)
            .get();

        try {
            Profile owner = item.getOwner();

            Instagram4j inst = Instagram4j.builder().username(owner.getNick()).password(owner.getSencha()).build();
            inst.setup();
            inst.login();

            File story = File.createTempFile("post", "");
            FileOutputStream fos = new FileOutputStream(story);
            fos.write(item.getContent());
            fos.flush();

            inst.sendRequest(new InstagramUploadVideoRequest(story, "capt"));
            story.delete();
            fos.close();

            item.status(Status.PUBLISHED);
        } catch (Exception e) {
            item.status(Status.REJECTED);
            logger.error("", e);
        }

        itemRepository.save(item);

    }
}
