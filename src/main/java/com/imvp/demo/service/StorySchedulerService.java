package com.imvp.demo.service;


import com.imvp.demo.domain.Story;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
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
public class StorySchedulerService {

    private final Logger log = LoggerFactory.getLogger(StorySchedulerService.class);

    private final Scheduler scheduler;

    public StorySchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public String scheduleJob(Story story) {

        JobDetail jobDetail = buildJobDetail(Map.of(
            "itemId", story.getId()
        ));
        Trigger trigger = buildJobTrigger(jobDetail, story.getPublishTime());
        try {
            Date date = scheduler.scheduleJob(jobDetail, trigger);
            log.info("PUBLISH TIME : {}", date);
            return date.toInstant().toString();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "";
        }
    }

    private JobDetail buildJobDetail(Map<String, Object> params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return JobBuilder.newJob(StoryJob.class)
            .withIdentity("story-job", "story-jobs-group")
            .withDescription("Publish story Job")
            .usingJobData(jobDataMap)
            //.storeDurably()
            .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Instant startAt) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "story-triggers")
            .withDescription("Publish story Trigger")
            .startAt(Date.from(startAt))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build();
    }
}
