package com.imvp.demo.service;


import com.imvp.demo.service.command.InstaPost;
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
public class SchedulerService {

    private final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public String scheduleJob(InstaPost post) {

        JobDetail jobDetail = buildJobDetail(Map.of("post", post));

        Trigger trigger = buildJobTrigger(jobDetail, post.getPublishTime());
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

        return JobBuilder.newJob(ScheduledJob.class)
            .withIdentity("post-job", "post-jobs-group")
            .withDescription("Publish post Job")
            .usingJobData(jobDataMap)
            .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Instant startAt) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "post-triggers")
            .withDescription("Publish post Trigger")
            .startAt(Date.from(startAt))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build();
    }
}
