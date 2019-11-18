package com.imvp.demo.service.command;

import com.imvp.demo.domain.Item;
import com.imvp.demo.domain.Profile;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.ItemRepository;
import com.imvp.demo.service.impl.ItemServiceImpl;
import java.io.File;
import java.io.FileOutputStream;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstPublishPostImgCommand implements InstCommand {

    private final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final String itemId;

    public InstPublishPostImgCommand(ItemRepository itemRepository, String itemId) {
        this.itemRepository = itemRepository;
        this.itemId = itemId;
    }


    @Override
    public void execute() {

        Item item = itemRepository.findById(itemId)
            .get();

        try {
            Profile owner = item.getOwner();

            Instagram4j instagram = Instagram4j.builder().username(owner.getNick()).password(owner.getSencha()).build();
            instagram.setup();
            instagram.login();

            File file = File.createTempFile("post", "");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(item.getContent());
            fos.flush();

            instagram.sendRequest(new InstagramUploadPhotoRequest(file, "capt"));
            file.delete();
            fos.close();

            item.status(Status.PUBLISHED);
        } catch (Exception e) {
            item.status(Status.REJECTED);
            logger.error("", e);
        }

        itemRepository.save(item);

    }
}
