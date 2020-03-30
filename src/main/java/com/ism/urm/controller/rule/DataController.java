package com.ism.urm.controller.rule;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ism.urm.service.rule.DataService;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.data.Data;
import com.ism.urm.vo.rule.data.Field;
import com.ism.urm.vo.rule.data.UsedData;

@RestController
@RequestMapping("/api")
public class DataController {

    DataService service = new DataService();

    @GetMapping("/data")
    public List<Data> search(@RequestParam Map<String, String> params) throws Exception {
        List<Data> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);

            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = null;
            if ("type".equals(key)) {
                op = RelationOp.get(key, OpType.EQ, val, ValueType.STRING);
            } else {
                op = RelationOp.get(key, OpType.LIKE, val, ValueType.STRING);
            }
            filter.add(op);
        }
        
        try {
            rtn = service.search(filter);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/data/{id}")
    public Data get(@PathVariable String id) throws Exception {
        Data rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/data")
    public Data save(@RequestBody Data data) throws Exception {
        Data rtn = null;
        try {
            rtn = service.save(data);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/data/delete")
    public int delete(@RequestBody List<String> ids) throws Exception {
        int rtn = 0;
        try {
            rtn = service.delete(ids);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/data/used")
    public List<UsedData> usedList(@RequestParam String id) throws Exception {
        List<UsedData> rtn = null;
        try {
            rtn = service.getUsedList(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/data/field/{dataId}")
    public List<Field> getFields(@PathVariable String dataId) throws Exception {
        List<Field> rtn = null;
        try {
            rtn = service.getFields(dataId);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/data/field/excel")
    public List<Field> getFieldsByTable(MultipartHttpServletRequest mReq) throws Exception {
        String sheetName = URLDecoder.decode(mReq.getParameter("sheetName"), "UTF-8");
        
        MultipartFile item = null;
        Iterator<String> files = mReq.getFileNames();
        while(files.hasNext()) {
            String uploadFile = files.next();
            if(uploadFile != null && uploadFile.length() > 0) {
                item = mReq.getFile(uploadFile);
                break;
            }
        }
        
        List<Field> rtn = null;
        try {
            rtn = service.parseExcel(sheetName, item);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/data/field/query")
    public List<Field> getFieldsByExcel(@RequestParam int type,
            @RequestParam String systemId,
            @RequestParam String query) throws Exception {
        List<Field> rtn = null;
        try {
            rtn = service.getTableInfo(type, systemId, query);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
