package com.ism.urm.controller.manage;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.manage.CodeService;
import com.ism.urm.vo.manage.BusinessCode;
import com.ism.urm.vo.manage.CommonCode;

@RestController
public class CodeController {

    CodeService service = new CodeService();

    @GetMapping("/code/common")
    public List<CommonCode> getCommonCode() {
        List<CommonCode> rtn = null;
        try {
            rtn = service.getCommonCodeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/code/business")
    public List<BusinessCode> getBusinessCode() {
        List<BusinessCode> rtn = null;
        try {
            rtn = service.getBusinessCodeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
}
