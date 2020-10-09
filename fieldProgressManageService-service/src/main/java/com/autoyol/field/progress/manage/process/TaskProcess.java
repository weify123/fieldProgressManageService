package com.autoyol.field.progress.manage.process;

import java.util.Map;

public interface TaskProcess {

    void process(String msgId, String msg);

    void mailProcess(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap);
}
