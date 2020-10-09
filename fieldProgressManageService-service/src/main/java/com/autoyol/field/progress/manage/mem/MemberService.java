package com.autoyol.field.progress.manage.mem;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.exception.MemCoreInfoException;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.autoyol.member.detail.api.MemberDetailFeignService;
import com.autoyol.member.detail.vo.res.MemberCoreInfo;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.CAT_TRANSACTION_GET_MEMBER_CORE_INFO;

@Component
public class MemberService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    MemberDetailFeignService memberDetailFeignService;

    @HystrixCommand(commandKey = "member-getMemberCoreInfo-command", fallbackMethod = "getMemberCoreInfoFailBack", groupKey = "memberGroup", threadPoolKey = "memberThread")
    public Long getMemberCoreInfo(Integer memId) {
        Transaction t = Cat.newTransaction(CAT_TRANSACTION_GET_MEMBER_CORE_INFO, CAT_TRANSACTION_GET_MEMBER_CORE_INFO);
        ResponseData<MemberCoreInfo> response;
        try {
            response = memberDetailFeignService.getMemberCoreInfo(memId);
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logError(e);
            logger.error("member getMemberCoreInfo error e {}", e);
            throw new MemCoreInfoException("getMemberCoreInfo error");
        } finally {
            t.complete();
        }
        logger.info("***PRO*** getMemberCoreInfo 返回 response={}", response);
        if (response.getResCode().equals(ErrorCode.SUCCESS.getCode())) {
            return OprUtil.getObjOrDefault(response.getData(), MemberCoreInfo::getMobile, null);
        }
        return null;
    }

    private Long getMemberCoreInfoFailBack(Integer memId) {
        logger.info("***PRO*** 进入会员信息获取熔断 memId={}", memId);
        return null;
    }
}
