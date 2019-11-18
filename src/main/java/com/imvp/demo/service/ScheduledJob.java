package com.imvp.demo.service;

import com.imvp.demo.service.command.CommandDispatcher;
import com.imvp.demo.service.command.InstaPost;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob extends QuartzJobBean {

    private final CommandDispatcher commandDispatcher;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public ScheduledJob(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        post(jobDataMap);
    }

    private void post(JobDataMap jobDataMap) {
        InstaPost post = (InstaPost) jobDataMap.get("post");
        commandDispatcher.dispatch(post).execute();
    }

}
