package com.ism.urm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.ProcessService;
import com.ism.urm.vo.process.LogBatch;
import com.ism.urm.vo.process.LogBatchDetail;
import com.ism.urm.vo.process.LogDeferred;
import com.ism.urm.vo.process.LogDeferredError;
import com.ism.urm.vo.process.LogRealtime;
import com.ism.urm.vo.process.LogRealtimeDetail;
import com.ism.urm.vo.process.MessageField;
import com.ism.urm.vo.process.ProcessStaticsDay;
import com.ism.urm.vo.process.ProcessStaticsHour;

@RestController
@RequestMapping("/api")
public class ProcessController {

    ProcessService service = new ProcessService();

    @GetMapping("/process/stat/day")
    public List<ProcessStaticsDay> getProcessStaticsDay(
            @RequestParam int type,
            @RequestParam String infType,
            @RequestParam Map<String, String> params) throws Exception {
        List<ProcessStaticsDay> rtn = null;
        try {
            if (infType.equals("realtime")) {
                if (type == 1) {
                    rtn = service.getProcessStaticsRealtimeDaySim(type, params);
                } else {
                    rtn = service.getProcessStaticsRealtimeDay(type, params);
                }
            } else if (infType.equals("batch")) {
                rtn = service.getProcessStaticsBatchDay(type, params);
            } else {
                rtn = service.getProcessStaticsDeferredDay(type, params);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/stat/hour")
    public List<ProcessStaticsHour> getProcessStaticsHour(
            @RequestParam int type,
            @RequestParam String infType,
            @RequestParam Map<String, String> params) throws Exception {
        List<ProcessStaticsHour> rtn = null;
        try {
            if (infType.equals("realtime")) {
                if (type == 1) {
                    rtn = service.getProcessStaticsRealtimeHourSim(type, params);
                } else {
                    rtn = service.getProcessStaticsRealtimeHour(type, params);
                }
            } else if (infType.equals("batch")) {
                rtn = service.getProcessStaticsBatchHour(type, params);
            } else {
                rtn = service.getProcessStaticsDeferredHour(type, params);
            }
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/batch")
    public List<LogBatch> getLogBatch(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogBatch> rtn = null;
        try {
            rtn = service.getLogBatch(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/batch/detail")
    public List<LogBatchDetail> getLogBatchDetail(
            @RequestParam int type,
            @RequestParam String batchId) throws Exception {
        List<LogBatchDetail> rtn = null;
        try {
            rtn = service.getLogBatchDetail(type, batchId);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/deferred")
    public List<LogDeferred> getLogDeferred(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogDeferred> rtn = null;
        try {
            rtn = service.getLogDeferred(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/deferred/detail")
    public List<LogDeferred> getLogDeferredDetail(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogDeferred> rtn = null;
        try {
            rtn = service.getLogDeferredDetail(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/deferred/error")
    public List<LogDeferredError> getLogDeferredError(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogDeferredError> rtn = null;
        try {
            rtn = service.getLogDeferredError(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/online")
    public List<LogRealtime> getLogRealtime(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogRealtime> rtn = null;
        try {
            rtn = service.getLogRealtime(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/online/detail")
    public List<LogRealtimeDetail> getLogRealtimeDetail(
            @RequestParam int type,
            @RequestParam Map<String, String> params) throws Exception {
        List<LogRealtimeDetail> rtn = null;
        try {
            rtn = service.getLogRealtimeDetail(type, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/process/log/online/message")
    public List<MessageField> getMessage(
            @RequestParam int type,
            @RequestParam String processDate,
            @RequestParam String interfaceId,
            @RequestParam int serialNumber) throws Exception {
        List<MessageField> rtn = null;
        try {
            rtn = service.getMessage(type, processDate, interfaceId, serialNumber);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

}
