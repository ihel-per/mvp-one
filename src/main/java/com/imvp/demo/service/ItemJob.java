package com.imvp.demo.service;

import com.imvp.demo.domain.Item;
import com.imvp.demo.domain.Profile;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.ItemRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ItemJob extends QuartzJobBean {

    private final ItemRepository itemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ItemJob.class);

    public ItemJob(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        post(jobDataMap);
    }

    private void post(JobDataMap jobDataMap) {



        Item itemId = itemRepository.findById(jobDataMap.getString("itemId"))
            .get();

        try {
            Profile owner = itemId.getOwner();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.writeBytes(itemId.getContent());

            InputStream in = new ByteArrayInputStream(itemId.getContent());
            BufferedImage bImageFromConvert = ImageIO.read(in);

            Instagram4j instagram = Instagram4j.builder().username(owner.getNick()).password(owner.getSencha()).build();
            instagram.setup();
            instagram.login();

         //   instagram.
            instagram.sendRequest(new InstagramUploadPhotoRequest(bImageFromConvert, itemId.getText(), null));

            itemId.status(Status.PUBLISHED);
        } catch (Exception e) {
            itemId.status(Status.REJECTED);
            logger.error("", e);
        }

        itemRepository.save(itemId);

    }
}
