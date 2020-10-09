package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.FieldProgressApplication;
import com.autoyol.field.progress.manage.request.order.AddPickDeliverReqVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={FieldProgressApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderProcessServiceTest {

    @Resource
    OrderProcessService orderProcessService;

    @Test
    public void addOrderProcess() throws Exception {
        AddPickDeliverReqVo reqVo = new AddPickDeliverReqVo();
        reqVo.setOrdernumber("test0001");
        reqVo.setDeliverycarcity("上海");
        reqVo.setSource("手机");
        reqVo.setSceneName("一键租车");
        reqVo.setOfflineOrderType("三方合同订单");
        reqVo.setTermtime("2020-02-02 12:00:00");
        reqVo.setReturntime("2020-02-03 12:00:00");
        reqVo.setRentAmt("200");
        reqVo.setHolidayAverage("100");
        reqVo.setDepositPayTime("20200203120000");
        reqVo.setVehicletype("个人车辆");
        reqVo.setEngineType("92号汽油");
        reqVo.setOilPrice("10");
        reqVo.setDisplacement("10");
        reqVo.setGuideDayPrice("10");
        reqVo.setDetectStatus("检测通过");
        reqVo.setBeforeTime("2020-02-03 12:00");
        reqVo.setOwnerGetAddr("啊啊啊");
        reqVo.setOwnerGetLon("53");
        reqVo.setOwnerGetLat("20");
        reqVo.setPickupcaraddr("ttttt");
        reqVo.setRealGetCarLon("20");
        reqVo.setRealGetCarLat("12");
        orderProcessService.addOrderProcess(reqVo);
    }
}
