package com.imvp.demo.service;


import com.imvp.demo.domain.Item;
import com.imvp.demo.domain.Scheduled;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ItemSchedulerService {

    private final Logger log = LoggerFactory.getLogger(ItemSchedulerService.class);

    private final Scheduler scheduler;

    public ItemSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public String scheduleJob(Item item) {

        JobDetail jobDetail = buildJobDetail(Map.of(
            "itemId", item.getId()
        ));
        Trigger trigger = buildJobTrigger(jobDetail, item.getPublishTime());
        try {
            Date date = scheduler.scheduleJob(jobDetail, trigger);
            log.info("PUBLISH TIME : {}", date);
            return date.toInstant().toString();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "";
        }
    }

/*
    JobKey jobKeyB = new JobKey("jobB", "group1");
    JobDetail jobB = JobBuilder.newJob(JobB.class)
        .withIdentity(jobKeyB).build();*/

    private JobDetail buildJobDetail(Map<String, Object> params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return JobBuilder.newJob(ItemJob.class)
            .withIdentity("item-job", "item-jobs-group")
            .withDescription("Publish post Job")
            .usingJobData(jobDataMap)
            //.storeDurably()
            .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Instant startAt) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "item-triggers")
            .withDescription("Publish post Trigger")
            .startAt(Date.from(startAt))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build();
    }
}
