package com.play.pay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.ResultCustomMessage;
import com.play.pay.config.AlipayConfig;
import com.play.pay.dao.IAlipayOrderDao;
import com.play.pay.model.AlipayOrder;
import com.play.pay.model.RechargeItem;
import com.play.pay.service.IAlipayOrderService;
import com.play.pay.service.IRechargeItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by lenovo on 2017/8/6.
 */
@Service(value = "alipayOrderService")
public class AlipayOrderServiceImpl extends BaseServiceImpl<AlipayOrder, Integer> implements IAlipayOrderService {
    @Resource(name = "alipayOrderDao")
    private IAlipayOrderDao alipayOrderDao;
    @Resource
    private IRechargeItemService rechargeItemService;
    private AlipayClient alipayClient;

    @Override
    public IBaseDao<AlipayOrder> getBaseDao() {
        return alipayOrderDao;
    }

    @PostConstruct
    private void initPay() throws Exception {
        alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
    }

    @Transactional
    @Override
    public String order(Long userId, Integer productId) throws ServiceException {
        try {
            AlipayOrder order = new AlipayOrder();
            order.setUserId(userId);
            order.setProductId(productId);
            order.setType(1);

            RechargeItem rechargeItem = rechargeItemService.get(productId);
            order.setWIDsubject("支付宝充值");
            order.setWIDbody(order.getWIDsubject());
            order.setWIDtotalAmount(rechargeItem.getPrice());
            order.setWIDoutTradeNo(Long.toHexString(System.currentTimeMillis()));
            order.setCreateDate(new Date());
            order.setUpdateDate(new Date());
            save(order);

            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest payRequest = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(order.getWIDbody());
            model.setSubject(order.getWIDsubject());
            model.setOutTradeNo(order.getWIDoutTradeNo());
            model.setTimeoutExpress("30m");
            model.setTotalAmount(String.valueOf(order.getWIDtotalAmount()));
            if (order.getUserId().intValue() == 14 || order.getUserId().intValue() == 526 || order.getUserId().intValue() == 541) {
                model.setTotalAmount("0.01");
            }
            model.setProductCode("QUICK_MSECURITY_PAY");
            payRequest.setBizModel(model);
            payRequest.setNotifyUrl(AlipayConfig.notify_url);
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse payResponse = alipayClient.sdkExecute(payRequest);
            return payResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultCustomMessage.F1018);
        }
    }
}
