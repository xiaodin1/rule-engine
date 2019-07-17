package org.jetlinks.rule.engine.cluster.logger;

import lombok.Getter;
import lombok.Setter;
import org.jetlinks.rule.engine.api.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class ClusterLogger implements Logger {

    private Consumer<LogInfo> logInfoConsumer;
    private Logger parent;
    private Map<String, String> context;

    private String instanceId;

    private String nodeId;

    protected LogInfo createLogInfo(String level, String message, Object... args) {
        LogInfo logInfo = new LogInfo();
        logInfo.setTimestamp(System.currentTimeMillis());
        logInfo.setContext(context);
        logInfo.setLevel(level);
        logInfo.setInstanceId(instanceId);
        logInfo.setNodeId(nodeId);
        logInfo.setMessage(MessageFormatter.arrayFormat(message, args).getMessage());
        logInfo.setArgs(Arrays.asList(args));

        return logInfo;
    }

    protected void publishLog(String level, String message, Object... args) {
        if (null != logInfoConsumer) {
            logInfoConsumer.accept(createLogInfo(level, message, args));
        }
    }

    @Override
    public void info(String message, Object... args) {
        if (null != parent) {
            parent.info(message, args);
        }
        publishLog("info", message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        if (null != parent) {
            parent.debug(message, args);
        }
        publishLog("debug", message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        if (null != parent) {
            parent.warn(message, args);
        }
        publishLog("warn", message, args);
    }

    @Override
    public void error(String message, Object... args) {
        if (null != parent) {
            parent.error(message, args);
        }
        publishLog("error", message, args);
    }
}
