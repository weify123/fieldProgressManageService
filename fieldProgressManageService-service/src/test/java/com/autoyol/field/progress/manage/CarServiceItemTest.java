package com.autoyol.field.progress.manage;

import com.autoyol.field.progress.manage.request.AddCarServiceItemReqVO;
import com.autoyol.field.progress.manage.service.CarServiceItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarServiceItemTest {


    @Autowired
    private CarServiceItemService carServiceItemService;


    @Test
    public void carServiceItem() {
        AddCarServiceItemReqVO addCarServiceItemReqVO = new AddCarServiceItemReqVO();
        addCarServiceItemReqVO.setServiceTypeKey(5);
        addCarServiceItemReqVO.setServiceProductName("测试数据4");
        addCarServiceItemReqVO.setStorePrice(new BigDecimal(30));
        addCarServiceItemReqVO.setAotuPrice(new BigDecimal(80));
        addCarServiceItemReqVO.setApplicableModel("新的模型");
        addCarServiceItemReqVO.setServiceTime("10.00");
        addCarServiceItemReqVO.setIsEffectiveKey(0);
        int num = carServiceItemService.addCarServiceItem(addCarServiceItemReqVO);
       /* QueryCarServiceItemReqVO queryCarServiceItemReqVO = new QueryCarServiceItemReqVO();
        queryCarServiceItemReqVO.setPageSize(1);
        queryCarServiceItemReqVO.setStart(1);
        PageInfo<QueryCarServiceItemRespVO> pageInfo = carServiceItemService.queryCarServiceItem(queryCarServiceItemReqVO);*/
        System.out.println(num);

    }
}
