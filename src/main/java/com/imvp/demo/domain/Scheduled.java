package com.imvp.demo.domain;

import java.time.Instant;

public interface Scheduled {

    String getId();

    Instant getPublishTime();
}
