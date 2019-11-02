package com.imvp.demo.service;

import com.imvp.demo.domain.Item;
import com.imvp.demo.domain.enumeration.Status;
import com.imvp.demo.repository.ItemRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;
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
public class PostJob extends QuartzJobBean {

    private final ItemRepository itemRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostJob.class);

    public PostJob(ItemRepository itemRepository) {
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

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.writeBytes(itemId.getContent());

            InputStream in = new ByteArrayInputStream(itemId.getContent());
            BufferedImage bImageFromConvert = ImageIO.read(in);

            Instagram4j instagram = Instagram4j.builder().username("FIXME").password("FIXME").build();
            instagram.setup();
            instagram.login();

            String tags = Arrays.stream(itemId.getTags().split(" "))
                .map(it -> "#" + it)
                .collect(Collectors.joining(" "));

            String text = itemId.getText() + "\n" + tags;

            instagram.sendRequest(new InstagramUploadPhotoRequest(bImageFromConvert, text, null));

            itemId.status(Status.PUBLISHED);
        } catch (Exception e) {
            itemId.status(Status.CANCELLED);
            logger.error("", e);
        }

        itemRepository.save(itemId);

    }
}
